package com.wolf.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class NioServer {
    //操作系统与应用程序交互数据的缓存
    private ByteBuffer readBuffer;
    //轮询器
    private Selector selector;

    public static void main(String[] args) {
        NioServer server = new NioServer();
        //初始化
        server.init();
        System.out.println("Server Started At 8383 Port.");
        //启动服务
        server.listen();
    }

    private void init() {
        //分配缓存
        readBuffer = ByteBuffer.allocate(1024);
        //channel是NIO中的通信通道
        ServerSocketChannel serverSocketChannel;

        try {
            //创建Socket Channel
            serverSocketChannel = ServerSocketChannel.open();
            //设置通道非阻塞
            serverSocketChannel.configureBlocking(false);
            //将通道绑定到服务器的ip地址和端口上
            serverSocketChannel.socket().bind(new InetSocketAddress(8383));

            //打开轮询器
            selector = Selector.open();
            //将Socket Channel注册到Selector轮询选择器，并绑定OP_ACCEPT事件来响应客户端连接请求
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void listen() {
        while (true) {
            try {
                //询问Selector轮询选择器获取所有事件
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    //遍历事件
                    SelectionKey key = iterator.next();
                    //遍历后删除，确保不重复
                    iterator.remove();
                    //处理事件
                    handleKey(key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleKey(SelectionKey key) throws IOException {
        SocketChannel channel = null;
        try {
            if (key.isAcceptable()) { /*可连接事件*/
                ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();
                //接收连接请求
                channel = serverChannel.accept();
                //将Channel注册到Selector轮询选择器，并绑定OP_READ事件来响应客户端读取请求
                channel.register(selector, SelectionKey.OP_READ);
            } else if (key.isReadable()) {
                channel = (SocketChannel) key.channel();
                //清空缓存，clear操作是让指针复位（position指针指向缓冲区起始位，limit指针指向缓冲区结束位）
                readBuffer.clear();

                //获取客户端请求
                int count = channel.read(readBuffer);
                if (count > 0) {
                    //从缓存获取数据前的flip操作（position指针执行本次数据起始位，limit指针指向本次数据结束位）
                    readBuffer.flip();

//                    CharBuffer charBuffer = CharsetHelper.decode(readBuffer);
//                    String question = charBuffer.toString();

                    //根据客户端请求，调用相应的业务方法获取结果
//                    String answer = getAnswer(question);
//                    channel.write(CharsetHelper.encode(CharBuffer.wrap(answer)));
                }
            } else {
                //当客户端关闭Channel或异常时关闭Channel
                channel.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            if (channel != null) {
                channel.close();
            }
        }
    }

    private String getAnswer(String question) {
        String answer = null;
        if ("who".equals(question)) {
            answer = "我是凤姐\n";
        } else if ("what".equals(question)) {
            answer = "我是来帮你解闷的\n";
        } else if ("where".equals(question)) {
            answer = "我来自外太空\n";
        } else if ("hi".equals(question)) {
            answer = "hello\n";
        } else if ("bye".equals(question)) {
            answer = "88\n";
        } else {
            answer = "请输入who，或者what，或者where";
        }
        return answer;
    }

}
