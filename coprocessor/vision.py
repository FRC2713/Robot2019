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
from stream import WebcamVideoStream
import os

isRunningOnPi = True
try:
  import RPi.GPIO as GPIO
except ImportError:
  print("Not running on coproc...")
  isRunningOnPi = False
import sys


class CamHandler(BaseHTTPRequestHandler):
  def do_GET(self):

    if self.path.endswith('.mjpg'):
      self.send_response(200)
      self.send_header('Content-type', 'multipart/x-mixed-replace; boundary=--jpgboundary')
      self.end_headers()
      while True:
        try:
          global final, port
          img = final
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


server = 0

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
  vt.putNumber("ipp", -1)


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


def drawBox(frame, rect, color=(0, 0, 255)):
  box = cv2.boxPoints(rect)
  box = np.array(box).reshape((-1, 1, 2)).astype(np.int32)
  cv2.drawContours(frame, [box], -1, color, 1)


if __name__ == '__main__':
  start_t = time.time()

  NetworkTables.initialize(server="roboRIO-2713-frc.local")
  vt = NetworkTables.getTable("VisionProcessing")
  vt.putNumber("heartbeat", 0)
  resetTable(vt)

  vs = WebcamVideoStream().start()
  final = vs.read()
  # vt.putNumber("screen_width", 1920)
  displayDebugWindow = (os.name == 'nt') or ("DISPLAY" in os.environ)



  h = 78
  s = 106
  v = 218

  hh = 99
  sh = 255
  vh = 255
  # Range of color in hsv
  lower_c = (h,s,v)
  upper_c = (hh,sh,vh)

  server_thread = Thread(target=serve, args=())
  server_thread.start()
  if isRunningOnPi:
    GPIO.setwarnings(False)
    GPIO.setmode(GPIO.BCM)
    indicatorLED = 21
    GPIO.setup(indicatorLED, GPIO.OUT, initial=GPIO.LOW)
  try:
    while 1:
      if isRunningOnPi:
        if vs.grabbed:
          GPIO.output(indicatorLED, GPIO.HIGH)
        else:
          GPIO.output(indicatorLED, GPIO.LOW)
      frame = vs.read()
      time.sleep(0.1)
      # ---- FILTER OUT THINGS WE DON'T WANT ----
      hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
      mask = cv2.inRange(hsv, lower_c, upper_c)
      rgb = cv2.cvtColor(mask, cv2.COLOR_BAYER_BG2RGB)
      res = cv2.bitwise_and(frame, frame, mask=mask)
      cv2.imshow("res",res)
      gray = cv2.cvtColor(rgb, cv2.COLOR_BGR2GRAY)
      # gray = cv2.GaussianBlur(gray, (5, 5), 3)
      edged = cv2.Canny(gray, 200, 300)
      if displayDebugWindow:
        cv2.imshow("contours", gray)

      _, cnts, _ = cv2.findContours(edged, cv2.RETR_LIST, cv2.CHAIN_APPROX_SIMPLE)
      rectborders = [cv2.minAreaRect(c) for c in cnts]
      rounded = []
      pairs = []

      # ---- FILTER OUT REPEAT CONTOURS ----

      for rect in rectborders:
        n = -1
        rnd_rect = ((round(rect[0][0], n), round(rect[0][1], n)), (round(rect[1][0], n), round(rect[1][1], n)),
                    round(rect[2], n))
        if isCorrectShape(rect):
          drawBox(frame, rect)

        for r2 in rounded:
          if rnd_rect == r2:
            rectborders.remove(rect)
            rounded.remove(rnd_rect)
        rounded.append(rnd_rect)

      # ---- GET PAIRS OF SIMILAR CONTOURS THAT MAY BE TARGET ----
      for r in rectborders:
        # sim_* resembles range of difference between rectangles that is deemed "acceptable" for them to be a pair
        sim_ratio = 3
        sim_angle = 4
        sim_area = 1
        if isCorrectShape(r):
          ratio_r = round(r[1][1] / r[1][0], 2)
          width = r[1][0]
          if ratio_r != getRegularRatio(ratio_r):
            ratio_r = getRegularRatio(ratio_r)
            width = r[1][1]
          angle_r = round(r[2], 1)
          area_r = r[1][1] * r[1][0]
          x_r = r[0][0]
          y_r = r[0][1]
          for r2 in rectborders:
            if r == r2 or haveSameCoordinates(r, r2):
              break
            elif isCorrectShape(r2):
              ratio_r2 = round(r2[1][1] / r2[1][0], 2)
              ratio_r2 = getRegularRatio(ratio_r2)
              angle_r2 = round(r2[2], 1)
              area_r2 = r2[1][1] * r2[1][0]
              x_r2 = r2[0][0]
              y_r2 = r2[0][1]
              distance = math.sqrt((y_r2 - y_r) ** 2 + (x_r2 - x_r) ** 2)

              if 5 * width < distance < 8 * width:
                cv2.line(frame, (int(x_r2), int(y_r2)), (int(x_r), int(y_r)), (0, 255, 200), 1)
                cv2.putText(frame, "angle: " + str(round(angle_r, 0)) + "deg", (int(x_r), int(y_r + 60)),
                            cv2.FONT_HERSHEY_SIMPLEX, 0.4, (0,0,0), 1)
                cv2.putText(frame, "angle: " + str(round(angle_r2, 0)) + "deg", (int(x_r2), int(y_r2 + 60)),
                            cv2.FONT_HERSHEY_SIMPLEX, 0.4, (0, 0, 0), 1)
                # if (ratio_r2 < ratio_r + sim_ratio and ratio_r2 > ratio_r - 1):
                if angle_r + sim_angle > angle_r2 + 61> angle_r - sim_angle:
                  if abs(area_r / area_r2 - 1) < sim_area:
                    pairs.append([r, r2])

      # print(pairs)

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

      # ---- DISPLAY VISUALIZATIONS FOR CONTOURS ----
      min_x = 1920
      max_x = 0
      min_y = 1080
      max_y = 0
      distances = []
      lmost = -1
      if len(pairs) > 0:
        pair = pairs[0]
        for rect in pair:
          color = (255, 0, 255)

          width = rect[1][0]
          height = rect[1][1]

          x = rect[0][0]
          y = rect[0][1]

          angle = rect[2]

          ratio = round(height / width, 3)
          if ratio != getRegularRatio(ratio):
            ratio = round(getRegularRatio(ratio), 3)
            tmp = height
            height = width
            width = tmp

          # Bounds of target in frame (Not currently used, meant for commented tracking loop)
          """
          if x - width / 2 < min_x:
            min_x = int(x - width / 2)
            lmost += 1
          if x + width / 2 > max_x:
            max_x = int(x + width / 2)
          if y - height / 2 < min_y:
            min_y = int(y - height / 2)
          if y + height / 2 > max_y:
            max_y = int(y + height / 2)
          """

          black = (0, 0, 0)

          cv2.putText(frame, str(ratio), (int(x), int(y)), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (0, 0, 255), 1)
          cv2.putText(frame, "w: " + str(round(width, 0)), (int(x), int(y + 20)), cv2.FONT_HERSHEY_SIMPLEX, 0.4,
                      black, 1)
          cv2.putText(frame, "h: " + str(round(height, 0)), (int(x), int(y + 40)), cv2.FONT_HERSHEY_SIMPLEX, 0.4,
                      black, 1)
          cv2.putText(frame, "angle: " + str(round(angle, 0)) + "deg", (int(x), int(y + 60)),
                      cv2.FONT_HERSHEY_SIMPLEX, 0.4, black, 1)

          inches = distance_to_camera(height)
          distances.append(inches)

          cv2.circle(frame, (int(round(x, 0)), int(round(y, 0))), 2, (0, 0, 0), 1)
          drawBox(frame, rect, color)

        # Tracking stuff: Would follow the target through mean shift
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

        # ---- FINDS AVERAGE DISTANCE OF TARGET AND PERSPECTIVE ANGLE ----

        if len(distances) == 2:
          diff = distances[0] - distances[1]  # this gives us the opposite for the triangle
          if lmost == 0:
            diff *= -1
          distance = round((distances[0] + distances[1]) / 24, 1)
          cv2.putText(frame, "%.2fft" % distance, (frame.shape[1] - 200, frame.shape[0] - 100),
                      cv2.FONT_HERSHEY_SIMPLEX, 2.0, (0, 0, 0), 3)
          pairs = pairs[0]
          center = ((pairs[0][0][0] + pairs[1][0][0]) / 2, (pairs[0][0][1] + pairs[1][0][1]) / 2)
          if abs(diff) < 6:  # 6 is the length in inches of the target, this gives u the hypotenuse
            perspective_angle = round(math.degrees(math.asin(diff / 6)), 3)
            vt.putNumber("angle", perspective_angle)
            vt.putNumber("distance", distance)
            vt.putNumber("x", center[0])
            vt.putNumber("y", center[1])
            vt.putNumber("ipp", width_to_pixel_width(pairs[0][1][0] * 2 + pairs[1][1][0] * 2))
            cv2.putText(frame, str(perspective_angle), (frame.shape[1] - 200, frame.shape[0]), cv2.FONT_HERSHEY_SIMPLEX,
                        2.0, (0, 0, 0), 3)
        else:
          resetTable(vt)
      # vt.putNumber("heartbeat", vt.getNumber("heartbeat") + 1)
      final = frame

      cv2.putText(frame, str(int(1 / (time.time() - start_t))) + " FPS", (frame.shape[1] - 130, 40),
                  cv2.FONT_HERSHEY_SIMPLEX, 1.0, (0, 255, 0), 2)
      if displayDebugWindow:
        cv2.imshow("image", frame)  # cv2.resize(image, (960, 540))
      start_t = time.time()

      k = cv2.waitKey(1) & 0xFF
      if k == ord('q'):
        break

  except KeyboardInterrupt:
    vs.stop()
    cv2.destroyAllWindows()
    GPIO.output(21, GPIO.LOW)
    GPIO.cleanup()
    stop_server()
    print(" KeyboardInterrupt: Exiting Vision.")
