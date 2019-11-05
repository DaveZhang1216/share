import cv2 as cv
import numpy as np
from abc import ABCMeta, abstractclassmethod




class Video:
    def __init__(self, filePath=None, camera=None):
        if filePath is not None:
            self.cap = cv.VideoCapture(filePath)
        if camera is not None:
            self.cap = cv.VideoCapture(camera)
        else:
            self.cap = cv.VideoCapture(0)

    def show(self, name=None):
        while self.cap.isOpened():
            ret, frame = self.cap.read()
            cv.waitKey(30)
            if not ret:
                print("Can't receive frame (stream end?). Exiting ...")
                break
            if name is None:
                cv.imshow("frame", frame)
            else:
                cv.imshow(name, frame)





