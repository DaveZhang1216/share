import cv2 as cv
import numpy as np
from matplotlib import pyplot as plt

midImage = None
midWinName = None
showWinName = "showWin"


def onmouse(event, x, y, flags, param):
    if event == cv.EVENT_MOUSEMOVE:
        print(midImage[y, x])


def hwchange(arg):
    w_min = cv.getTrackbarPos("w_min", midWinName)
    w_max = cv.getTrackbarPos("w_max", midWinName)
    h_min = cv.getTrackbarPos("h_min", midWinName)
    h_max = cv.getTrackbarPos("h_max", midWinName)
    i = midImage[h_min:h_max, w_min:w_max]
    cv.imshow(showWinName, i)


def onchange(arg):
    if midImage.ndim == 3:
        if midImage.shape[2] == 3:
            min0 = cv.getTrackbarPos("min[0]", midWinName)
            max0 = cv.getTrackbarPos("max[0]", midWinName)
            min1 = cv.getTrackbarPos("min[1]", midWinName)
            max1 = cv.getTrackbarPos("max[1]", midWinName)
            min2 = cv.getTrackbarPos("min[2]", midWinName)
            max2 = cv.getTrackbarPos("max[2]", midWinName)
            mask = cv.inRange(midImage, np.array([min0, min1, min2]), np.array([max0, max1, max2]))
            cv.imshow(showWinName, mask)
            return
    else:
        min0 = cv.getTrackbarPos("min[0]", midWinName)
        max0 = cv.getTrackbarPos("max[0]", midWinName)
        mask = cv.inRange(midImage, min0, max0)
        cv.imshow(showWinName, mask)


class Image:
    def __init__(self, filePath=None, nparray=None):
        if filePath is not None:  # 不为空
            self.img = cv.imread(filePath)
        if nparray is not None:
            self.img = nparray

    def gray(self):
        self.img = cv.cvtColor(self.img, cv.COLOR_BGR2GRAY)

    def hsv(self):
        self.img = cv.cvtColor(self.img, cv.COLOR_BGR2HSV)

    def tonparray(self):
        return self.img

    def threshold(self, min, max):
        """
        :param min:
        :param max:
        :return:  过滤出在 数值在min 和max 之间mask
        """
        ret, mask = cv.threshold(self.img, min, max, cv.THRESH_BINARY)

    def show(self, name=None):
        if name is None:
            cv.imshow("display", self.img)
        else:
            cv.imshow(name, self.img)

    def chooseArea(self, updown, leftRight):
        self.img = self.img[updown[0]:updown[1], leftRight[0]:leftRight[1]]

    def areaChoose(self, win_name="default"):
        global midWinName
        global midImage
        midWinName = win_name
        midImage = self.img
        cv.namedWindow(win_name)
        cv.createTrackbar("w_min", midWinName, 0, midImage.shape[0], hwchange)
        cv.createTrackbar("w_max", midWinName, 0, midImage.shape[0], hwchange)
        cv.createTrackbar("h_min", midWinName, 0, midImage.shape[1], hwchange)
        cv.createTrackbar("h_max", midWinName, 0, midImage.shape[1], hwchange)

    def inRangeChoose(self, win_name="default"):
        global midWinName
        global midImage
        global showWinName
        midWinName = win_name
        cv.namedWindow(win_name)
        cv.namedWindow(showWinName)

        if self.img.ndim == 3:
            if self.img.shape[2] == 3:
                cv.createTrackbar("min[0]", win_name, 0, 255, onchange)
                cv.createTrackbar("max[0]", win_name, 0, 255, onchange)
                cv.createTrackbar("min[1]", win_name, 0, 255, onchange)
                cv.createTrackbar("max[1]", win_name, 0, 255, onchange)
                cv.createTrackbar("min[2]", win_name, 0, 255, onchange)
                cv.createTrackbar("max[2]", win_name, 0, 255, onchange)
                midImage = self.img
                cv.setMouseCallback(showWinName, onmouse)
                return
        cv.createTrackbar("min[0]", win_name, 0, 255, onchange)
        cv.createTrackbar("max[0]", win_name, 0, 255, onchange)
        midImage = self.img
        cv.setMouseCallback(showWinName, onmouse)

    def save(self, filePath):
        cv.imwrite(filePath, self.img)

    def plotShow(self):
        show = self.img
        if self.img.ndim == 3:
            if self.img.shape[2] == 3:
                b, g, r = cv.split(self.img)
                show = cv.merge([r, g, b])
        plt.imshow(show)
        plt.xticks([])
        plt.yticks([])
        plt.show()





