from zrl.core import LearningListerner
from zrl.core.rflImpl import QLearning
import gym
import numpy as np
from zrl.lib.envs.cliff_walking import CliffWalkingEnv
from zrl.util.Display import Display
from zrl.core.rflImpl.NET import Estimator,StateProcessor
import os
from zrl.core.rflImpl.DQN import DQN

def qlearningTest():
    listern = LearningListerner.LearningListerner()
    mdp = QLearning.QLearning()
    env = CliffWalkingEnv()
    mdp.learn(env, 500, listern, gamma=1.0, alpha=0.5, epsilon=0.1)
    display = Display()
    display.printListernStatus(listern)
def dqnTest():
    sp = StateProcessor()
    env = gym.make("Breakout-v0")
    estimator = Estimator(env.action_space.n)
    estimator.getModel()
    observation = env.reset()
    observation_p = sp.process(observation)
    observation = np.stack([observation_p] * 4, axis=2)
    observations = np.array([observation] * 3)
    print(observations.shape)
    print(estimator.predict(observations))
    y = np.array([[10, 8, 6, 2], [10, 8, 6, 2], [10, 8, 6, 2]])
    print(estimator.update(observations, y))
    display = Display()
    display.displayModel(estimator.getModel())
def breakoutTrain():
    sp = StateProcessor()
    env = gym.make("Breakout-v0")
    experiment_dir = os.path.abspath("F:/experiments/{}".format(env.spec.id))
    sp = StateProcessor()
    estimator = Estimator(env.action_space.n, 0.00025, os.path.join(experiment_dir, "q_estimator.h5"))
    target_estimator = Estimator(env.action_space.n, 0.00025, os.path.join(experiment_dir, "q_estimator.h5"))

    if not os.path.exists(experiment_dir):
        os.makedirs(experiment_dir)
    mdp = DQN(q_estimator=estimator,
              target_estimator=target_estimator,
              state_processor=sp,
              replay_memory_size=30000,
              replay_memory_init_size=10000,
              update_target_estimator_every=10000,
              save_estimator_every=10,
              epsilon_start=0.15,
              epsilon_end=0.1,
              epsilon_decay_steps=1000)

    mdp.learn(env=env,
              num_episodes=10000,
              listerner=LearningListerner.LearningListerner())
    gym.spaces
def carnival_ram():
    sp = StateProcessor()
    env = gym.make("UpNDown-v0")
    experiment_dir = os.path.abspath("F:/experiments/{}".format(env.spec.id))
    sp = StateProcessor()
    estimator = Estimator(env.action_space.n, 0.00025, os.path.join(experiment_dir, "q_estimator.h5"))
    target_estimator = Estimator(env.action_space.n, 0.00025, os.path.join(experiment_dir, "q_estimator.h5"))

    if not os.path.exists(experiment_dir):
        os.makedirs(experiment_dir)
    mdp = DQN(q_estimator=estimator,
              target_estimator=target_estimator,
              state_processor=sp,
              replay_memory_size=40000,
              replay_memory_init_size=5000,
              update_target_estimator_every=5000,
              save_estimator_every=10,
              epsilon_start=1,
              epsilon_end=0.05,
              epsilon_decay_steps=500000)

    mdp.learn(env=env,
              num_episodes=3000,
              listerner=LearningListerner.LearningListerner())
def SpaceInvaders():
    sp = StateProcessor()
    env = gym.make("SpaceInvaders-v0")
    experiment_dir = os.path.abspath("F:/experiments/{}".format(env.spec.id))
    sp = StateProcessor()
    estimator = Estimator(env.action_space.n, 0.00025, os.path.join(experiment_dir, "q_estimator.h5"))
    target_estimator = Estimator(env.action_space.n, 0.00025, os.path.join(experiment_dir, "q_estimator.h5"))
    if not os.path.exists(experiment_dir):
        os.makedirs(experiment_dir)
    mdp = DQN(q_estimator=estimator,
              target_estimator=target_estimator,
              state_processor=sp,
              replay_memory_size=40000,
              replay_memory_init_size=40000,
              update_target_estimator_every=10000,
              save_estimator_every=10,
              epsilon_start=0.6,
              epsilon_end=0.2,
              epsilon_decay_steps=500000)

    mdp.learn(env=env,
              num_episodes=1000,
              listerner=LearningListerner.LearningListerner())


if __name__ == "__main__":
    # print(gym.envs.registry.all())
    breakoutTrain()

    """
            for i_episode in range(20):
            observation = env.reset()
            for t in range(100):
                env.render()
                print(observation)
                action = random.randint(0, 3)
                observation, reward, done, info = env.step(action)
                if done:
                    print("Episode finished after {} timesteps".format(t + 1))
                    break
    """


