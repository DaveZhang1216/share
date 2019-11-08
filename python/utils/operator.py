from pykeyboard import *
from pymouse import *
import pyautogui as pag
import time, os
import datetime
import threading
from pynput import mouse

m = PyMouse()
k = PyKeyboard()
lock = threading.Lock()


def on_click(x, y, button, pressed):
    print('{0} at {1}'.format('Pressed' if pressed else 'Released', (x, y)))
    print(button)

def func(position, delt_time, times):
    for i in range(0, times):
        lock.acquire()
        m.click(position['x'], position['y'])
        lock.release()
        time.sleep(delt_time)


class Operator:
    def __init__(self):
        self.keyboard = PyKeyboard()
        self.mouse = PyMouse()

    def print_mouse_position(self):
        try:
            while True:
                print("Press Ctrl-C to End")
                x, y = pag.position()
                print("Position: "+str(x).rjust(4)+", "+str(y).rjust(4))
                time.sleep(2)
                os.system("cls")
        except KeyboardInterrupt as ke:
            pass

    def print_mouse_click_event(self):
        mouse_listener = mouse.Listener(on_click=on_click)
        mouse_listener.start()
        mouse_listener.join()

    def time_click(self, time, position, delttime, times):
        """
        :param time: 指定时间点击 yyyy-MM-dd HH:mm:ss
        :param position: 点击位置dict(x,y)
        :param delttime: 间隔时间
        :param times: 点击次数
        :return:
        """
        now_time = datetime.datetime.now()
        click_time = datetime.datetime.strptime(time, "%Y-%m-%d %H:%M:%S")
        delt = (click_time-now_time).total_seconds()
        timer = threading.Timer(delt, function=func, args=[position, delttime, times])
        timer.start()

    def batch_time_click(self, time, positions, delttime, times):
        for position in positions:
            self.time_click(time, position, delttime, times)


if __name__ == "__main__":
    robot = Operator()
    # robot.print_mouse_position()
    robot.print_mouse_click_event()
    print("end")

    """
    robot.time_click("2019-11-7 16:18:50",
                     dict(x=288, y=590),
                     0.1, 8)
    
    positions = [dict(x=137, y=779), dict(x=288, y=698),
                 dict(x=283, y=599), dict(x=138, y=527),
                 dict(x=142, y=603), dict(x=142, y=696),
                 dict(x=288, y=527)]
    robot.batch_time_click("2019-11-7 16:49:10",
                           positions,
                           0.1, 1)
    """
    # time.sleep(1000)
