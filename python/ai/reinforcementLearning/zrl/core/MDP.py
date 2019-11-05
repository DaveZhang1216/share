from abc import ABCMeta,abstractmethod


class MDP(metaclass=ABCMeta):
    """
     模型基础：
        P[St+1|St] = P[St+1|S1,....,St]  不关心历史状态
    强化学习的接口
    值函数： 给出状态获得对应的最终奖励
    策略函数：给出状态获得响应动作
    learn 返回有用的数组
    """
    # 通过状态 返回状态值
    @abstractmethod
    def valFuntion(self, state):
        pass

    # 通过状态给出最佳动作
    @abstractmethod
    def policyFunction(self, state):
        pass

    @abstractmethod
    def learn(self, env, num_episodes, listerner, gamma=1.0, alpha=0.5, epsilon=0.1):
        """
        :param env: 环境
        :param num_episodes:  训练多少个流程
        :param gamma:  得分的衰减率
        :param alpha:  学习率
        :param epsilon:  探索率
        :return:
        """
        pass
