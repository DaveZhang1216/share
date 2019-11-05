import cv2 as cv
import numpy as np

"""
    抠图工具
"""
class ImageUtil:
    def __init__(self, filePath):
        self.img = cv.imread("E:\\wechat\\python\\a.jpg")



gray_trackbar_min = "gray_min"
gray_trackbar_max = "gray_max"
win_name = "image"


def grayOnchanged(arg):
    gray_min = cv.getTrackbarPos(gray_trackbar_min, win_name)
    gray_max = cv.getTrackbarPos(gray_trackbar_max, win_name)
    h, s, v = cv.split(img)
    mask = cv.inRange(s, gray_min, gray_max)
    print(mask.shape)
    cv.imshow(win_name, mask)

def grayThreshold():
    cv.namedWindow(win_name)
    cv.createTrackbar(gray_trackbar_min, win_name, 0, 255, grayOnchanged)
    cv.createTrackbar(gray_trackbar_max, win_name, 0, 255, grayOnchanged)

if __name__=="__main__":
    img = cv.imread("E:\\wechat\\python\\a.jpg")
    img = img[320:1440, 290:560]
    # cv.calcHist(img, [0], None, 256, [0, 256])
    cv.cvtColor(img, cv.COLOR_BGR2HSV)
    grayThreshold()
    grayOnchanged(0)
    cv.waitKey()


