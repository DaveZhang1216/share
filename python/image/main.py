import cv2
from image import Image
import numpy as np

def imageTest():
    imge = cv2.imread("C:\\Users\\DaveZhang\\Desktop\\timg.jpg")
    img = Image(nparray=imge)
    img.plotShow()
    cv2.waitKey()


def background():
    img = cv2.imread("E:\\wechat\\python\\a.jpg")
    print(img.shape)
    img = img[320:1440, 290:560]


    # img = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
    # img = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
    return img


if __name__ == "__main__":
    img = Image("E:\\wechat\\python\\a.jpg")
    img.chooseArea((331, 1080), (296, 554))
    x, y = img.tonparray().shape[0:2]
    a = cv2.resize(img.tonparray(), (int(y/2), int(x/2)))
    mask = 255*np.ones(a.shape, a.dtype)
    #width, height, channels = a.shape
    tam = Image("E:\\wechat\\python\\tam.jpg")
    width, height = tam.tonparray().shape[0:2]
    center = (int(height/2), int(width/2))
    print(a.shape)
    print(tam.tonparray().shape)
    # cv2.waitKey()
    # Image(nparray=tam).show()
    clone = cv2.seamlessClone(a, tam.tonparray(), mask, center, cv2.NORMAL_CLONE)
    Image(nparray=clone).plotShow()
    # img.chooseArea((331, 1440), (296, 554))
    # img.show()
    #img.hsv()
    #img.inRangeChoose()
    cv2.waitKey()
    # img.plotShow()



