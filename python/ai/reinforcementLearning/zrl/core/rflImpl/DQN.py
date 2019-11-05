from zrl.core.MDP import MDP
from collections import deque
import numpy as np
from zrl.util import Utils
import itertools
import sys
import random


class DQN(MDP):
    def __init__(self, q_estimator, target_estimator,
                 state_processor,
                 replay_memory_size, replay_memory_init_size,
                 update_target_estimator_every, save_estimator_every,
                 epsilon_start, epsilon_end, epsilon_decay_steps,
                 batch_size=32):
        self.__estimator = q_estimator  # (nA, lr, experiment_dir)
        self.__target_estimator = target_estimator  # (nA, lr, experiment_dir)
        self.__state_processor = state_processor
        self.__replay_memory_size = replay_memory_size
        self.__replay_memory_init_size = replay_memory_init_size
        self.__update_target_estimator_every = update_target_estimator_every
        self.__save_estimator_every = save_estimator_every
        self.__epsilon_start = epsilon_start
        self.__epsilon_end = epsilon_end
        self.__epsilon_decay_steps = epsilon_decay_steps
        self.__batch_size = batch_size
        self.epsilons = np.linspace(epsilon_start, epsilon_end, epsilon_decay_steps)
        self.total_t = 0
    # 通过状态 返回状态值
    def valFuntion(self, state):
        pass

    # 通过状态给出最佳动作 id
    def policyFunction(self, state):
        # 将状态变为批量值为1 返回是动作值
        q_values = self.__estimator.predict(np.expand_dims(state, 0))[0]
        best_action = np.argmax(q_values)
        return best_action

    def __make_epsilon_greedy_policy(self, nA):
        """
        :param Q:  state -> action-values
        :param epsilon: 探索因子
        :param nA: 行为数量
        :return:
        """
        def policy_fn(observation, epsilon):
            A = np.ones(nA, dtype=float) * epsilon / nA  #动作表
            best_action = self.policyFunction(observation)  # q表里面找寻最佳动作
            A[best_action] += (1.0 - epsilon)  # 返回执行某个动作的概率
            return A
        return policy_fn
    def learn(self, env, num_episodes, listerner, gamma=0.99, alpha=0.5, epsilon=0.1):
        # The replay memory
        D = deque()
        self.__target_estimator.copyParams(self.__estimator)
        policy = self.__make_epsilon_greedy_policy(env.action_space.n)
        print("Populating replay memory...")
        state = env.reset()
        state = self.__state_processor.process(state)
        state = np.stack([state]*4, axis=2)
        for i in range(self.__replay_memory_init_size):
            env.render()
            action_probs = policy(state, self.epsilons[min(self.total_t, self.__epsilon_decay_steps-1)])
            action = Utils.getActionByActionPro(action_probs)
            next_state, reward, done, _ = env.step(action)
            # print(_ == "{'ale.lives': 5}")
            # 获得了归一化的84*84图像
            next_state = self.__state_processor.process(next_state)
            # next_state.shape = (84,84,4) state[:, :, 1:].shape=(84,84,3) np.expand_dims(next_state, 2).shape(84,84,1)
            next_state = np.append(state[:, :, 1:], np.expand_dims(next_state, 2), axis=2)
            D.append((state, action, reward, next_state, done))
            if done:
                state = env.reset()
                state = self.__state_processor.process(state)
                state = np.stack([state]*4, axis=2)
            else:
                state = next_state
        for i_episode in range(num_episodes):
            if i_episode % self.__save_estimator_every == 0:
                self.__estimator.save()  # 保存模型
            state = env.reset()
            state = self.__state_processor.process(state)
            state = np.stack([state]*4, axis=2)
            loss = None
            print("here")
            for t in itertools.count():
                env.render()
                if self.total_t % self.__update_target_estimator_every == 0:
                    self.__target_estimator.copyParams(self.__estimator)
                    print("\nCopied model params")
                print("\rStep {} ({}) @ Episode {}/{}, loss: {}".format(
                    t, self.total_t, i_episode + 1, num_episodes, loss), end="")
                sys.stdout.flush()
                action_probs = policy(state, self.epsilons[min(self.total_t, self.__epsilon_decay_steps - 1)])
                action = Utils.getActionByActionPro(action_probs)
                next_state, reward, done, _ = env.step(action)
                # 获得了归一化的84*84图像
                next_state = self.__state_processor.process(next_state)
                # next_state.shape = (84,84,4) state[:, :, 1:].shape=(84,84,3) np.expand_dims(next_state, 2).shape(84,84,1)
                next_state = np.append(state[:, :, 1:], np.expand_dims(next_state, 2), axis=2)
                D.append((state, action, reward, next_state, done))
                if len(D) > self.__replay_memory_size:
                    D.popleft()
                samples = random.sample(D, self.__batch_size)
                states_batch, action_batch, reward_batch, next_states_batch, done_batch = map(np.array, zip(*samples))
                # 获取下一个各行为的值targets_batch.shape = (batch_size,actionNum)
                targets_batch = self.__estimator.predict(states_batch)
                Q_sa = self.__target_estimator.predict(next_states_batch)
                for i in range(len(samples)):
                    if done_batch[i]:
                        targets_batch[i][action_batch[i]] = reward_batch[i]
                    else:
                        targets_batch[i][action_batch[i]] = reward_batch[i] + gamma * np.max(Q_sa[i])
                states_batch = np.array(states_batch)
                loss = self.__estimator.update(states_batch, targets_batch)
                if done:
                    break
                state = next_state
                self.total_t += 1

