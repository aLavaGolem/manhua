package com.manhua.util;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class Mq {
    private static Channel  channel =null;
    static {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setPort(5672);
        Connection connection;
        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setPort(5672);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare("hellword", false, false, false, null);

        String message = "Hello World2!";
        channel.basicPublish("", "hellword", null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");


        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message2 = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" + message2 + "'");
        };
        channel.basicConsume("hellword", true, deliverCallback, consumerTag -> { });
    }
    public static void getMessage(DeliverCallback deliverCallback,String key) throws Exception {
        channel.queueDeclare(key, false, false, false, null);
        channel.basicConsume(key, true, deliverCallback, consumerTag -> { });
    }
    public static void sendMessage(String message,String key) throws Exception {
        channel.queueDeclare(key, false, false, false, null);
        channel.basicPublish("", key, null, message.getBytes());
    }
}