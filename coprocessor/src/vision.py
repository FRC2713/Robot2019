from socketserver import ThreadingMixIn
from threading import Thread
from io import BytesIO
from PIL import Image
import numpy as np
import cv2
import math
from networktables import NetworkTables
from http.server import BaseHTTPRequestHandler, HTTPServer
import time
from coprocessor.src.stream import WebcamVideoStream
import os

isRunningOnPi = True
try:
  import RPi.GPIO as GPIO
except ImportError:
  print("Not running on coproc...")
  isRunningOnPi = False

BLACK = (0, 0, 0)
RED = (0, 0, 255)
GREEN = (0, 255, 0)
BLUE = (255, 0, 0)
TURQ = (208, 224, 64)
ORANGE = (0, 127, 255)
YELLOW = (0, 255, 255)
PURPLE = (128, 0, 128)
WHITE = (255, 255, 255)


class CamHandler(BaseHTTPRequestHandler):
  def do_GET(self):

    if self.path.endswith('.mjpg'):
      self.send_response(200)
      self.send_header('Content-type', 'multipart/x-mixed-replace; boundary=--jpgboundary')
      self.end_headers()
      while True:
        try:
          global frame_filtered, port
          img = frame_filtered
          imgRGB = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
          jpg = Image.fromarray(imgRGB)
          tmpFile = BytesIO()
          jpg.save(tmpFile, 'JPEG')
          self.wfile.write("--jpgboundary".encode())
          self.send_header('Content-type', 'image/jpeg')
          self.send_header('Content-length', str(tmpFile.getbuffer().nbytes))
          self.end_headers()
          self.wfile.write(tmpFile.getvalue())
          # jpg.save(self.wfile, 'JPEG')
          time.sleep(0.05)
        except KeyboardInterrupt:
          break
      return
    if self.path.endswith('.html'):
      self.send_response(200)
      self.send_header('Content-type', 'text/html')
      self.end_headers()
      self.wfile.write('<html><head></head><body>'.encode())
      self.wfile.write(
        ('<img src="http:// ' + self.client_address[0] + ':' + str(port) + '/cam.mjpg"/>').encode())
      self.wfile.write('</body></html>'.encode())
      return


class ThreadedHTTPServer(ThreadingMixIn, HTTPServer):
  """Handle requests in a separate thread."""


def serve():
  global server
  port = 8087
  server = ThreadedHTTPServer(("", port), CamHandler)
  print("started server on port", port)
  server.serve_forever()


def stop_server():
  global server
  server.server_close()
  server.stopped = True
  server.shutdown()


def resetTable(vt):
  vt.putNumber("angle", -1)
  vt.putNumber("distance", -1)
  vt.putNumber("x", -1)
  vt.putNumber("y", -1)
  vt.putNumber("heartbeat", -1)


def haveSameCoordinates(rect1, rect2):
  if round(rect1[0][0], 0) == round(rect2[0][0], 0) and round(rect1[0][1], 0) == round(rect2[0][1], 0):
    return True
  else:
    return False


def isCorrectShape(rect):
  if rect[1][0] > 12 and rect[1][1] > 12:
    correct_ratio = 5.5 / 2.0  # 5.5" by 2" tape strip
    err = 5
    width = rect[1][0]
    height = rect[1][1]
    ratio = round(height / width, 2)
    if ratio < 1:
      ratio = 1 / ratio
    if (correct_ratio - 1) < ratio < (correct_ratio + err):
      return True
  return False


def getRegularRatio(ratio):
  r = ratio
  if r < 1:
    r = 1 / r
  return r


def distance_to_camera(pixHeight):
  # KNOWN_DISTANCE = 77
  # KNOWN_HEIGHT = 183
  # focalHeight = 51.333336
  # return (KNOWN_HEIGHT * focalHeight) / pixHeight
  return 9394 / pixHeight


def width_to_pixel_width(width):
  return 8 / width


def drawBox(frame, rect, color=RED):
  box = cv2.boxPoints(rect)
  box = np.array(box).reshape((-1, 1, 2)).astype(np.int32)
  cv2.drawContours(frame, [box], -1, color, 1)


def getPairs(rectborders):
  """
          This may help in understanding some of the code:
           _____
          |     |
          |     |  * = center, (x, y)
          |     |  _ and | = side of minimum area rect of contour
          |     |
          |     |  h / w = approx 15.3 / 2.0 (dimension of single target rectangle)
          h  *  |
          |     |
          |     |
          |     |
          |     |
          |__w__|

          cv2.minAreaRect(contour) = ((x, y), (w, h), angle)

          Reason for inverting values at times is due to the fact that width and height may not correlate from one rectangle to another (width and height may be switched)

  """
  pairs = []
  for r in rectborders:
    # sim_* resembles range of difference between rectangles that is deemed "acceptable" for them to be a pair
    sim_ratio = 3
    sim_angle = 7
    sim_area = 1
    if isCorrectShape(r):
      ratio_r = round(r[1][1] / r[1][0], 2)
      width = r[1][0]
      angle_r = round(r[2], 1)
      if ratio_r < 1:
        ratio_r = 1 / ratio_r
        width = r[1][1]
        angle_r += 90

      area_r = r[1][1] * r[1][0]
      x_r = r[0][0]
      y_r = r[0][1]

      for r2 in rectborders:
        if r == r2 or haveSameCoordinates(r, r2):
          break
        elif isCorrectShape(r2):
          ratio_r2 = round(r2[1][1] / r2[1][0], 2)
          angle_r2 = round(r2[2], 1)
          if ratio_r2 < 1:
            ratio_r2 = 1 / ratio_r2
            width = r2[1][1]
            angle_r2 += 90

          area_r2 = r2[1][1] * r2[1][0]
          x_r2 = r2[0][0]
          y_r2 = r2[0][1]
          distance = math.sqrt((y_r2 - y_r) ** 2 + (x_r2 - x_r) ** 2)
          cv2.line(frame_filtered, (int(x_r2), int(y_r2)), (int(x_r), int(y_r)), YELLOW, 1)
          cv2.putText(frame_filtered, "angle: " + str(round(angle_r2, 0)) + "deg", (int(x_r2), int(y_r2 + 60)),
                      cv2.FONT_HERSHEY_SIMPLEX, 0.4, WHITE, 1)
          cv2.putText(frame_filtered, "angle: " + str(round(angle_r, 0)) + "deg", (int(x_r), int(y_r + 60)),
                      cv2.FONT_HERSHEY_SIMPLEX, 0.4, WHITE, 1)
          if 5 * width < distance < 8 * width:

            if (ratio_r2 < ratio_r + sim_ratio and ratio_r2 > ratio_r - 1):

              for inv in range(-1,2,2):
                print(inv)
                if angle_r + sim_angle > angle_r2 - 29 * inv> angle_r - sim_angle:
                  if abs(area_r / area_r2 - 1) < sim_area:
                    pairs.append([r, r2])

  return pairs


def removeRepeatContours(rectborders):
  # ---- FILTER OUT REPEAT CONTOURS ----
  # Is this necessary???
  rounded = []
  for rect in rectborders:
    n = -1
    rnd_rect = ((round(rect[0][0], n), round(rect[0][1], n)), (round(rect[1][0], n), round(rect[1][1], n)),
                round(rect[2], n))
    if isCorrectShape(rect):
      drawBox(frame_filtered, rect)

    for r2 in rounded:
      if rnd_rect == r2:
        rectborders.remove(rect)
        rounded.remove(rnd_rect)
    rounded.append(rnd_rect)
  return rectborders


def holeTargetCalculations(frame, pair):
  # ---- DISPLAY VISUALIZATIONS FOR CONTOURS ----

  distances = []

  for rect in pair:

    width = rect[1][0]
    height = rect[1][1]

    x = rect[0][0]
    y = rect[0][1]

    angle = rect[2]

    ratio = round(height / width, 3)
    if ratio != getRegularRatio(ratio):
      ratio = round(getRegularRatio(ratio), 3)
      width = rect[1][1]
      height = rect[1][0]

    cv2.putText(frame, str(ratio), (int(x), int(y)), cv2.FONT_HERSHEY_SIMPLEX, 0.5, RED, 1)
    cv2.putText(frame, "w: " + str(round(width, 0)), (int(x), int(y + 20)), cv2.FONT_HERSHEY_SIMPLEX, 0.4,
                WHITE, 1)
    cv2.putText(frame, "h: " + str(round(height, 0)), (int(x), int(y + 40)), cv2.FONT_HERSHEY_SIMPLEX, 0.4,
                WHITE, 1)
    cv2.putText(frame, "angle: " + str(round(angle, 0)) + "deg", (int(x), int(y + 60)),
                cv2.FONT_HERSHEY_SIMPLEX, 0.4, WHITE, 1)

    inches = distance_to_camera(height)
    distances.append(inches)

    cv2.circle(frame, (int(round(x, 0)), int(round(y, 0))), 2, PURPLE, 1)

    drawBox(frame, rect, PURPLE)

  # ---- FIND AVERAGE DISTANCE OF TARGET AND PERSPECTIVE ANGLE ----

  diff = distances[0] - distances[1]  # this gives us the opposite for the triangle
  distance = round((distances[0] + distances[1]) / 24, 1)

  center = ((pair[0][0][0] + pair[1][0][0]) / 2, (pair[0][0][1] + pair[1][0][1]) / 2)
  if abs(diff) < 6:  # 6 is the length in inches of the target, this gives u the hypotenuse
    perspective_angle = round(math.degrees(math.asin(diff / 6)), 3)
    if enableNetworkTables:
      vt.putNumber("angle", perspective_angle)
      vt.putNumber("distance", distance)
      vt.putNumber("x", center[0])
      vt.putNumber("y", center[1])
      vt.putNumber("ipp", width_to_pixel_width(pair[0][1][0] * 2 + pair[1][1][0] * 2))
    # Display Perspective Angle
    cv2.putText(frame, str(perspective_angle), (frame.shape[1] - 200, frame.shape[0]), cv2.FONT_HERSHEY_SIMPLEX,
                2.0, BLACK, 3)
  # Display Distance
  cv2.putText(frame, "%.2fft" % distance, (frame.shape[1] - 200, frame.shape[0] - 100),
              cv2.FONT_HERSHEY_SIMPLEX, 2.0, BLACK, 3)

  # Tracking stuff: Would follow the target through mean shift (not in use)
  """

  track_window = (min_x, min_y, max_x - min_x, max_y - min_y)
  target_window = ((min_x, min_y),(max_x - min_x, max_y - min_y), 0.0)
  drawBox(frame, target_window, (255,100,0))
  print(track_window)
  roi = frame[min_y:max_y, min_x:max_x]
  #print(roi)
  hsv_roi = cv2.cvtColor(roi, cv2.COLOR_BGR2HSV)
  mask_roi = cv2.inRange(hsv_roi, lower_c, upper_c)
  #mask_roi = mask[min_y:max_y, min_x:max_x]
  roi_hist = cv2.calcHist([hsv_roi], [0], mask_roi, [256], [0, 256])
  cv2.normalize(roi_hist, roi_hist, 0, 255, cv2.NORM_MINMAX)
  term_crit = (cv2.TERM_CRITERIA_EPS | cv2.TERM_CRITERIA_COUNT, 80, 1)
  cv2.imshow("mask", mask_roi)

  while True:
      # grab the frame from the threaded video stream and resize it
      # to have a maximum width of 400 pixels
      frame = vs.read()
      hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
      dst = cv2.calcBackProject([hsv], [0], roi_hist, [0, 180], 1)
      ret, track_window = cv2.meanShift(dst, track_window, term_crit)
      filter = cv2.inRange(hsv, np.array((0, 146, 149)), np.array((102, 178, 213)))
      x, y, w, h = track_window
      cv2.rectangle(frame, (x, y), (x + w, y + h), 255, 2)
      cv2.rectangle(filter, (x, y), (x + w, y + h), 255, 2)
      cv2.putText(frame, 'Tracked', (x - 25, y - 10), cv2.FONT_HERSHEY_SIMPLEX,
                  1, (255, 255, 255), 2)

      # frame = imutils.resize(frame, width=400)

      # check to see if the frame should be displayed to our screen
      obj = frame[y:y + h, x:x + w]
      cv2.imshow("Frame", frame)
      cv2.imshow("obj", obj)

      cv2.imshow("filter", filter)
      if cv2.waitKey(1) & 0xFF == ord('q'):
          break
  """


def getFPS(frame_counter):
  global start_t
  dt = (time.time() - start_t)
  FPS = int(frame_counter / dt)
  start_t = time.time()
  return FPS


def halt():
  vs.stop()
  cv2.destroyAllWindows()
  if isRunningOnPi:
    GPIO.output(21, GPIO.LOW)
    GPIO.cleanup()
  stop_server()


if __name__ == '__main__':
  start_t = time.time()

  # Tracking Setings
  ballTracking = True
  targetTracking = True
  # FPS settings
  displayFPS = True
  FPS = 0
  frame_counter = 0
  FPS_update_period = 0.2
  FPSColors = [RED, YELLOW, GREEN]

  # Enable or Disable network tables
  enableNetworkTables = True

  # Display settings
  displayWindows = (os.name == 'nt') or ("DISPLAY" in os.environ)
  debugWindows = True
  unfilteredWindow = False


  vs = WebcamVideoStream().start()
  # vt.putNumber("screen_width", 1920)

  # Range of color in hsv (Target)
  lower_c_hole = np.array([60, 0, 100])
  upper_c_hole = np.array([110, 255, 255])
  #at home
  lower_c_hole = np.array([28, 22, 121])
  upper_c_hole = np.array([64, 255, 255])
  # Range of color in hsv (Ball)
  lower_c_ball = np.array([0, 22, 0])
  upper_c_ball = np.array([32, 255, 255])

  server_thread = Thread(target=serve, args=())
  server_thread.start()

  indicatorLED = 21

  if isRunningOnPi:
    GPIO.setwarnings(False)
    GPIO.setmode(GPIO.BCM)
    GPIO.setup(indicatorLED, GPIO.OUT, initial=GPIO.LOW)

  if enableNetworkTables:
    NetworkTables.initialize(server="roboRIO-2713-frc.local")
    vt = NetworkTables.getTable("VisionProcessing")
    resetTable(vt)
    vt.putNumber("heartbeat", 0)
  running = 1
  try:
    while running:
      if isRunningOnPi:
        if vs.grabbed:
          GPIO.output(indicatorLED, GPIO.HIGH)
        else:
          GPIO.output(indicatorLED, GPIO.LOW)

      frame_filtered = vs.readFiltered()
      frame_unfiltered = vs.read()

      hsv = cv2.cvtColor(frame_filtered, cv2.COLOR_BGR2HSV)

      # Hole Target Processing
      if targetTracking:
        holeTargetMask = cv2.inRange(hsv, lower_c_hole, upper_c_hole)
        rgb_hole = cv2.cvtColor(holeTargetMask, cv2.COLOR_BAYER_BG2RGB)
        res_hole = cv2.bitwise_and(frame_filtered, frame_filtered, mask=holeTargetMask)
        gray_hole = cv2.cvtColor(rgb_hole, cv2.COLOR_BGR2GRAY)
        gray_hole = cv2.GaussianBlur(gray_hole, (5, 5), 3)
        edged = cv2.Canny(gray_hole, 300, 400, L2gradient=True)
        frame_filtered = frame_filtered.copy()
        _, cnts, _ = cv2.findContours(edged, cv2.RETR_LIST, cv2.CHAIN_APPROX_SIMPLE)
        rectborders = [cv2.minAreaRect(c) for c in cnts]
        rectborders = removeRepeatContours(rectborders)
        pairs = getPairs(rectborders)
        if len(pairs) > 0:
          holeTargetCalculations(frame_filtered, pairs[0])
        if displayWindows:
          if debugWindows:
            cv2.imshow("res", cv2.resize(res_hole, (350, 300)))
            cv2.imshow("contours", cv2.resize(edged, (350, 300)))
          cv2.imshow("image", frame_filtered)

      if ballTracking:

        # Ball processing
        orangeBallMask = cv2.inRange(hsv, lower_c_ball, upper_c_ball)
        rgb_ball = cv2.cvtColor(orangeBallMask, cv2.COLOR_BAYER_BG2BGR)
        res_ball = cv2.bitwise_and(frame_unfiltered, frame_unfiltered, mask=orangeBallMask)
        gray_ball = cv2.cvtColor(rgb_ball, cv2.COLOR_BGR2GRAY)
        frame_unfiltered = frame_unfiltered.copy()
        circles = cv2.HoughCircles(gray_ball, cv2.HOUGH_GRADIENT, 1.0, 300)
        if circles is not None:
          circles = np.round(circles[0, :]).astype("int")
          for (x, y, r) in circles:
            cv2.circle(frame_filtered, (x, y), r, GREEN, 2)
            cv2.rectangle(frame_filtered, (x - 5, y - 5), (x + 5, y + 5), ORANGE, -1)
        if displayWindows:
          if debugWindows:
            cv2.imshow("res_ball", cv2.resize(res_ball, (350, 300)))
          if unfilteredWindow:
            cv2.imshow("image_ball", frame_unfiltered)


      # Display FPS
      frame_counter += 1
      if displayFPS == True:

        if time.time() - start_t >= FPS_update_period:
          FPS = getFPS(frame_counter)
          frame_counter = 0
          print(FPS)

        c = 0
        if FPS > 9:
          c = 1
        if FPS > 29:
          c = 2
        cv2.putText(frame_filtered, str(FPS) + " FPS", (frame_filtered.shape[1] - 130, 40),
                    cv2.FONT_HERSHEY_SIMPLEX, 1.0, FPSColors[c], 2)

      if enableNetworkTables:
        # If roboRIO sets running to 0, stop running.
        vt.putNumber("heartbeat", vt.getNumber("heartbeat", 0) + 1)
        running = vt.getNumber("running", 1)

      if (cv2.waitKey(1) & 0xFF) == ord('q'):
        break

    halt()
    print("\nEnding")

  except KeyboardInterrupt:
    halt()
    print("\nKeyboardInterrupt: Exiting Vision.")
