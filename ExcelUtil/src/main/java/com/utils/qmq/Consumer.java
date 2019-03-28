package com.utils.qmq;

import qunar.tc.qmq.Message;
import qunar.tc.qmq.consumer.annotation.QmqConsumer;

public class Consumer {
    @QmqConsumer(subject = "your subject", consumerGroup = "group", executor = "your executor")
    public void onMessage(Message message){
        //process your message
        String value = message.getStringProperty("key");
    }
}
