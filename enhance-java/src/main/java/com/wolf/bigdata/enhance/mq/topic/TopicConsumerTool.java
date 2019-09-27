package com.wolf.bigdata.enhance.mq.topic;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class TopicConsumerTool implements MessageListener, ExceptionListener {
    private String user = ActiveMQConnection.DEFAULT_USER;
    private String password = ActiveMQConnection.DEFAULT_PASSWORD;
    private String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private String subject = "mytopic";
    private Destination destination = null;
    private Connection connection = null;
    private Session session = null;
    private MessageConsumer consumer = null;
    public static Boolean isConnected = false;

    /**
     * 初始化
     *
     * @throws JMSException
     */
    public void initiate() throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, url);
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        destination = session.createTopic(subject);
        consumer = session.createConsumer(destination);
    }

    /**
     * 消费消息
     *
     * @throws JMSException
     */
    public void consumerMessage() throws JMSException {
        initiate();
        connection.start();
        consumer.setMessageListener(this);
        connection.setExceptionListener(this);
        isConnected = true;
        System.out.println("Consumer: -> Begin listening ... ");
    }

    /**
     * 关闭连接
     *
     * @throws JMSException
     */
    public void close() throws JMSException {
        System.out.println("Consumer: -> Closing connection.");
        if (consumer != null) {
            consumer.close();
        }
        if (session != null) {
            session.close();
        }
        if (connection != null) {
            connection.close();
        }
    }

    /**
     * 消息处理
     *
     * @param message
     */
    public void onMessage(Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage txtMsg = (TextMessage) message;
                String msg = txtMsg.getText();
                System.out.println("Consumer " + Thread.currentThread().getName() + ": -> Received: " + msg);
            } else {
                System.out.println("Consumer " + Thread.currentThread().getName() + ": -> Received: " + message);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public void onException(JMSException exception) {
        isConnected = false;
    }
}
