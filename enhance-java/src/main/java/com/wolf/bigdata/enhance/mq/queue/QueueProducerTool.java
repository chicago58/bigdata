package com.wolf.bigdata.enhance.mq.queue;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class QueueProducerTool {
    private String user = ActiveMQConnection.DEFAULT_USER;
    private String password = ActiveMQConnection.DEFAULT_PASSWORD;
    // Java连接ActiveMQ的默认端口为61616
//    private String url = "tcp://localhost:61616";
    private String url = ActiveMQConnection.DEFAULT_BROKER_URL;

    // queue的名称
    private String subject = "myqueue";
    // 消息的接收者
    private Destination destination = null;
    // JMS客户端到JMS Provider的连接
    private Connection connection = null;
    // 发送消息的线程
    private Session session = null;
    // 消息发送者
    private MessageProducer producer = null;

    /**
     * 初始化
     *
     * @throws JMSException
     */
    public void initialize() throws JMSException {
        // 创建连接工厂，JMS用它创建连接
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(user, password, url);
        // 从工厂获取连接对象
        connection = connectionFactory.createConnection();
        // 获取操作连接会话
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        // 通过queue创建生产者的目标
        destination = session.createQueue(subject);
        // 获取消息发送者
        producer = session.createProducer(destination);
        // 设置消息传送模式：不持久化
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
    }

    /**
     * 发送消息
     *
     * @param message
     * @throws JMSException
     */
    public void produceMessage(String message) throws JMSException {
        initialize();
        TextMessage msg = session.createTextMessage(message);
        // 启动
        connection.start();
        System.out.println("Producer: -> Sending message: " + message);
        producer.send(msg);
        System.out.println("Producer: -> Message sent complete!");
    }

    /**
     * 关闭连接
     *
     * @throws JMSException
     */
    public void close() throws JMSException {
        System.out.println("Producer: -> Closing connection!");
        if (producer != null) {
            producer.close();
        }
        if (session != null) {
            session.close();
        }
        if (connection != null) {
            connection.close();
        }
    }
}
