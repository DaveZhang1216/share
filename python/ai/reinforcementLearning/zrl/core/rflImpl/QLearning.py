from zrl.core import MDP
import numpy as np
from collections import defaultdict
import itertools
from zrl.util import Utils

class QLearning(MDP.MDP):
    def __init__(self):
        self.dict = {}
        self.qTable = "Q"
    def __make_epsilon_greedy_policy(self, epsilon, nA):
        """
        :param Q:  state -> action-values
        :param epsilon: 探索因子
        :param nA: 行为数量
        :return:
        """
        def policy_fn(observation):
            if(self.qTable not in self.dict):
                self.dict[self.qTable] = defaultdict(lambda: np.zeros(nA))
            A = np.ones(nA, dtype=float) * epsilon / nA  #动作表
            best_action = self.policyFunction(observation)  # q表里面找寻最佳动作
            A[best_action] += (1.0 - epsilon)  # 返回执行某个动作的概率
            return A
        return policy_fn

    # 通过状态 返回状态值
    def valFuntion(self, state):
        return np.max(self.dict[self.qTable][state])  # 表中最大值执行概率为1

    # 通过状态给出最佳动作
    def policyFunction(self, state):
        return np.argmax(self.dict[self.qTable][state])

    def learn(self, env, num_episodes, listerner, gamma=1.0, alpha=0.5, epsilon=0.1):
        policy = self.__make_epsilon_greedy_policy(epsilon, env.action_space.n)
        for i_episode in range(num_episodes):
            # self.display.printProcess(i_episode, num_episodes)
            state = env.reset()
            for t in itertools.count():
                action_probs = policy(state)
                action = Utils.getActionByActionPro(action_probs)
                # 先走一步获取状态
                next_state, reward, done, info = env.step(action)
                # QLearning Update  sarsa在此处是使用on-policy 非查表 此处为off-policy
                td_target = reward + gamma * self.valFuntion(next_state)
                td_delta = td_target - self.dict[self.qTable][state][action]
                self.dict[self.qTable][state][action] += alpha * td_delta
                listerner.listen(num_episodes, i_episode, t, state,
                                 action, next_state, reward, done, info, self.dict[self.qTable])
                if done:
                    break
                state = next_state

