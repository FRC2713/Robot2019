import cv2
import numpy as np
def nothing(x):
    pass
cap = cv2.VideoCapture(0)
cv2.namedWindow('image')
cv2.createTrackbar('H','image',0,180,nothing)
cv2.createTrackbar('S','image',0,255,nothing)
cv2.createTrackbar('V','image',0,255,nothing)

cv2.createTrackbar('HH','image',0,180,nothing)
cv2.createTrackbar('SH','image',0,255,nothing)
cv2.createTrackbar('VH','image',0,255,nothing)


def adjust_gamma(image, gamma=1.0):
    # build a lookup table mapping the pixel values [0, 255] to
    # their adjusted gamma values
    invGamma = 1.0 / gamma
    table = np.array([((i / 255.0) ** invGamma) * 255
                      for i in np.arange(0, 256)]).astype("uint8")

    # apply gamma correction using the lookup table
    return cv2.LUT(image, table)

while(1):

    # Take each frame
    _, frame = cap.read()
    frame = adjust_gamma(frame, 0.1)
    # Convert BGR to HSV

    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
    print(hsv)
    h = cv2.getTrackbarPos('H', 'image')
    s = cv2.getTrackbarPos('S', 'image')
    v = cv2.getTrackbarPos('V', 'image')

    hh = cv2.getTrackbarPos('HH', 'image')
    sh = cv2.getTrackbarPos('SH', 'image')
    vh = cv2.getTrackbarPos('VH', 'image')
    print("h = " + str(h))
    print("s = " + str(s))
    print("v = " + str(v))
    print("")
    print("hh = " + str(hh))
    print("sh = " + str(sh))
    print("vh = " + str(vh))

    # define range of blue color in HSV
    lower_c = (h, s, v)
    upper_c = (hh, sh, vh)

    # Threshold the HSV image to get only blue colors
    mask = cv2.inRange(hsv, lower_c, upper_c)

    # Bitwise-AND mask and original image
    res = cv2.bitwise_and(frame,frame, mask= mask)

    # Convert the img to grayscale
    gray = cv2.cvtColor(res, cv2.COLOR_BGR2GRAY)

    # Apply edge detection method on the image
    edges = cv2.Canny(gray, 50, 150, apertureSize=3)

    # This returns an array of r and theta values
    #lines = cv2.HoughLines(edges, 1, np.pi / 180, 100)

    """First parameter, Input image should be a binary image, so apply threshold edge detection before finding applying hough transform.
    Second and third parameters are r and θ(theta) accuracies respectively.
    Fourth argument is the threshold, which means minimum vote it should get for it to be considered as a line.
    Remember, number of votes depend upon number of points on the line. So it represents the minimum length of line that should be detected.
    """
    # The below for loop runs till r and theta values
    # are in the range of the 2d array np.ndarray
    leeway = 0.1
    minLineLength = 0.1
    maxLineGap = 20

    lines = cv2.HoughLinesP(edges, 1, np.pi / 90, 1, minLineLength, maxLineGap)
    if type(lines) == np.ndarray:
        for l in lines:
            for x1, y1, x2, y2 in l:
                cv2.line(res, (x1, y1), (x2, y2), (0, 255, 0), 2)
    """
    if type(lines) == np.ndarray:
        if len(lines) >= 4:
            line_positions = []
            for l in lines:
                for r, theta in l:
                    if (theta > (np.pi - leeway) and theta < (np.pi + leeway)) or (theta > (np.pi/2 - leeway) and theta < (np.pi/2 + leeway)):
                        # Stores the value of cos(theta) in a
                        a = np.cos(theta)

                        # Stores the value of sin(theta) in b
                        b = np.sin(theta)

                        # x0 stores the value rcos(theta)
                        x0 = a * r

                        # y0 stores the value rsin(theta)
                        y0 = b * r

                        # x1 stores the rounded off value of (rcos(theta)-1000sin(theta))
                        x1 = int(x0 - 1000 * b)

                        # y1 stores the rounded off value of (rsin(theta)+1000cos(theta))
                        y1 = int(y0 + 1000 * a)

                        # x2 stores the rounded off value of (rcos(theta)+1000sin(theta))
                        x2 = int(x0 + 1000 * b)

                        # y2 stores the rounded off value of (rsin(theta)-1000cos(theta))
                        y2 = int(y0 - 1000 * a)

                        # cv2.line draws a line in img from the point(x1,y1) to (x2,y2).
                        # (0,0,255) denotes the colour of the line to be
                        # drawn. In this case, it is red.

                        cv2.line(res, (x1, y1), (x2, y2), (0, 0, 255), 1)

    """
    cv2.imshow('edges', edges)
    cv2.imshow('image',frame)
    cv2.imshow('res',res)
    k = cv2.waitKey(1) & 0xFF
    if k == ord('q'):
        break
cv2.destroyAllWindows()
