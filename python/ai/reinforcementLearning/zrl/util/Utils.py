import numpy as np
import time
class Time:
    def start(self):
        self._time_start = time.time()
    def deltTime(self):
        return time.time()-self._time_start
    def printDeltTime(self):
        print(time.time()-self._time_start)

def getActionByActionPro(action_probs):
    """
        根据动作概率分布给出动作值
    :return:
    """
    return np.random.choice(np.arange(len(action_probs)), p=action_probs)
