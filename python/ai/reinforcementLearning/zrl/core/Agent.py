# -*- coding: UTF-8 -*-

"""
何为智能体：
    决策：能够观察环境，然后做出最佳动作
    学习： 给出 State,R 可自动改变策略
    理想状态应当是：给出观察信息和得分  给出当前最佳策略并学习
"""
class Agent:
    # 对于只能体需要一个马尔科夫决策过程的模型 去给策略和学习
    def __init__(self, mdp):
        self.mdp = mdp
    # 给出observation 和 reward  返回相应的动作
    def excuteAction(self,observation, reward):
        # 先给出动作
        # 开线程学习
        # 返回动作
        pass