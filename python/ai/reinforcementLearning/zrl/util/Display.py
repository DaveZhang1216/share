import sys
from matplotlib import pyplot as plt
import matplotlib
import numpy as np
import tensorflow as tf
from PIL import Image
class Display:
    def __init__(self):
        matplotlib.style.use('ggplot')
    def printProcess(self, i_episode, num_episodes):
        print("\rEpisode {}/{}.".format(i_episode + 1, num_episodes), end="")
        sys.stdout.flush()

    def printListernStatus(self, listern):
        data = listern.getStats()
        index = [n for n in range(0, (len(data[0])))]
        npIndex = np.array(index)
        npRewords = np.array(data[0])
        npSteps = np.array(data[1])
        plt.subplot(2, 1, 1)
        plt.plot(npIndex, npRewords)
        plt.title("Episode Reword")
        plt.subplot(2, 1, 2)
        plt.plot(npIndex, npSteps)
        plt.title("Episode Per Time Steps")
        plt.show()

    def displayModel(self, model):
        tf.keras.utils.plot_model(model, to_file="model.png", show_shapes=True)
        # print(model.get_weights()) 权重和偏置
        im = Image.open("model.png")
        im.show()

