import numpy as np
from zrl.util import Display

class LearningListerner:

    def __init__(self):
        self.dict = {}
        self.stats = "stats"
        self.display = Display.Display()
        self. _i_episode = 0

    def listen(self, num_episodes, i_episode, step, state, action, next_state, reward, done, info, qtable):
        if self.stats not in self.dict.keys():
            self.dict[self.stats] = np.zeros(shape=(2, num_episodes))
        if i_episode != self._i_episode:
            self.display.printProcess(i_episode, num_episodes)
        self.dict[self.stats][0][i_episode] += reward  # 获得的总分
        self.dict[self.stats][1][i_episode] = step

    def getStats(self):
        return self.dict[self.stats]



