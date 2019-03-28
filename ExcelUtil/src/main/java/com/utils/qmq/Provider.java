package com.utils.qmq;

import qunar.tc.qmq.Message;
import qunar.tc.qmq.producer.MessageProducerProvider;

public class Provider {
    public static void main(String[] args) {
        MessageProducerProvider producer = new MessageProducerProvider();
        producer.init();

        Message message = producer.generateMessage("test");
        message.setProperty("shibowen", "1");
//发送延迟消息
//message.setDelayTime(15, TimeUnit.MINUTES);
        producer.sendMessage(message);
    }
}
