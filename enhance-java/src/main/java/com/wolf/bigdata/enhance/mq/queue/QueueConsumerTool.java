package com.wolf.bigdata.enhance.mq.queue;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class QueueConsumerTool implements MessageListener, ExceptionListener {
    private String user = ActiveMQConnection.DEFAULT_USER;
    private String password = ActiveMQConnection.DEFAULT_PASSWORD;
    private String url = "failover://tcp://localhost:61616";
    // queue的名称
    private String queue = "myqueue";
    // 消息的接收者
    private Destination destination = null;
    // JMS客户端到JMS Provider的连接
    private Connection connection = null;
    // 发送消息的线程
    private Session session = null;
    // 消息消费者
    private MessageConsumer consumer = null;
    // 创建连接工厂，JMS用它创建连接
    private ActiveMQConnectionFactory connectionFactory = null;
    public static Boolean isConnected = false;

    /**
     * 初始化
     *
     * @throws JMSException
     */
    public void initialize() throws JMSException {
        // 获取连接工厂
        connectionFactory = new ActiveMQConnectionFactory(user, password, url);
        // 从工厂获取连接对象
        connection = connectionFactory.createConnection();
        // 获取操作连接会话
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 通过queue创建消费者的目标
        destination = session.createQueue(queue);
        // 通过目标创建消费者
        consumer = session.createConsumer(destination);
    }

    /**
     * 消费消息
     *
     * @throws JMSException
     */
    public void consumeMessage() throws JMSException {
        initialize();
        connection.start();

        // ActiveMQ采用推送的机制消费消息
        consumer.setMessageListener(this);
        connection.setExceptionListener(this);
        System.out.println("Consumer " + Thread.currentThread().getName() + ": -> Begin listening ...");
        isConnected = true;

        // 开始监听，进入阻塞状态
        Message message = consumer.receive();
        System.out.println(message.getJMSMessageID());
    }

    /**
     * 关闭连接
     *
     * @throws JMSException
     */
    public void close() throws JMSException {
        System.out.println("Consumer " + Thread.currentThread().getName() + ": -> Closing connection!");
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

    public void onException(JMSException exception) {
        isConnected = false;
    }

    /**
     * 收到消息时的消息处理
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
}
