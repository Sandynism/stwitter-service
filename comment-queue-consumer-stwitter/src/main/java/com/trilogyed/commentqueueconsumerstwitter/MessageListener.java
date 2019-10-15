package com.trilogyed.commentqueueconsumerstwitter;

import com.trilogyed.commentqueueconsumerstwitter.util.messages.Comment;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class MessageListener {

    @RabbitListener(queues = CommentQueueConsumerStwitterApplication.QUEUE_NAME)
    public void receiveMessage(Comment msg) {

        System.out.println(msg.toString());
    }
}