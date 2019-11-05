import tensorflow as tf
import skimage as skimage
from skimage import color, transform, exposure, io
from pathlib import Path
import time
class StateProcessor:
    def process(self, image):
        """
        :param state: 输入彩色图像
        :return: 返回84*84 灰度图像
        """
        # print(image.shape)
        image = color.rgb2gray(image)
        image = skimage.transform.resize(image, (84, 84))
        image = skimage.exposure.rescale_intensity(image, out_range=(0, 255))  # 调整亮度
        image = image/255.0
        return image

class Estimator:

    """
        Q-Value Estimator neural network.
        This network is used for both the Q-Network and the Target Network.
    """
    def __init__(self, an, lr, filePath):
        """
        :param an: 动作数
        """
        self._an = an
        self.__filePath = filePath
        file = Path(filePath)
        if file.is_file():
            self.model = tf.keras.models.load_model(filePath)
        else:
            self.model = self.__build_net(lr)
    def getModel(self):
        return self.model
    def simpleNet(self):
        """
        只是为了验证model.get_getWeights() 有什么
        :return:
        """
        model = tf.keras.models.Sequential()
        model.add(tf.keras.layers.Conv2D(filters=1,
                                         kernel_size=(3, 3),
                                         activation="relu",
                                         input_shape=(5, 5, 3)))
        model.add(tf.keras.layers.Flatten())
        model.add(tf.keras.layers.Dense(2))
        return model

    def __build_net(self, lr):
        model = tf.keras.models.Sequential()
        model.add(tf.keras.layers.Conv2D(filters=32,
                                         kernel_size=(8, 8),
                                         strides=(4, 4),
                                         activation="relu",
                                         input_shape=(84, 84, 4),
                                         ))
        model.add(tf.keras.layers.Conv2D(filters=64,
                                         kernel_size=(4, 4),
                                         strides=(2, 2),
                                         activation="relu"))
        model.add(tf.keras.layers.Conv2D(filters=64,
                                         kernel_size=(3, 3),
                                         activation="relu"))
        model.add(tf.keras.layers.Flatten())
        model.add(tf.keras.layers.Dense(512, activation="relu"))
        model.add(tf.keras.layers.Dense(self._an))
        rmsprop = tf.keras.optimizers.RMSprop(lr)
        model.compile(loss='mse', optimizer=rmsprop, metrics=['accuracy'])
        return model

    def copyParams(self, estimator):
        """
        :param model: 通过另外一个estimator初始化它
        :return:
        """
        self.model.set_weights(estimator.model.get_weights())

    def predict(self, inputData):
        """
        根据输入多批次数据 返回网络输出多批次数据
        :param inputData: 批次的数据
        :return:  批次的返回
        """
        return self.model.predict(inputData)

    def update(self, states, targets):
        """
        :param states: State input of shape 有batch_size样本进入
        :param targets: 响应的动作与得分
        :return:
        """
        return self.model.train_on_batch(states, targets)
    def save(self):
        self.model.save(self.__filePath)
