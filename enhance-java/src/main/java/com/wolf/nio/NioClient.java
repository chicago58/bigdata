package com.wolf.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class NioClient implements Runnable {
    private BlockingQueue<String> queue;
    private Random random;

    public static void main(String[] args) {
        //多个线程发起Socket客户端连接请求
        for (int i = 0; i < 10; i++) {
            NioClient client = new NioClient();
            client.init();
            new Thread(client).start();
        }

    }

    private void init() {
        queue = new ArrayBlockingQueue<String>(5);
        try {
            queue.put("hi");
            queue.put("who");
            queue.put("what");
            queue.put("where");
            queue.put("bye");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String getWord() {

        return null;
    }

    public void run() {
        SocketChannel channel = null;
        Selector selector = null;

        try {
            channel = SocketChannel.open();
            channel.configureBlocking(false);

            //客户端主动请求连接
            channel.connect(new InetSocketAddress("127.0.0.1", 8383));
            selector = Selector.open();
            //在Channel上注册Selector，并绑定连接事件
            channel.register(selector, SelectionKey.OP_CONNECT);

            boolean isOver = false;
            while (!isOver) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isConnectable()) {
                        if (channel.isConnectionPending()) {
                            if (channel.finishConnect()) {
                                //只有连接成功后才能注册OP_READ事件
                                key.interestOps(SelectionKey.OP_READ);

//                                channel.write(CharsetHelper.encode(CharBuffer.wrap(getWord())));
//                                sleep();
                            } else {
                                key.cancel();
                            }
                        }
                    } else if (key.isReadable()) {
                        ByteBuffer byteBuffer = ByteBuffer.allocate(128);
                        channel.read(byteBuffer);
                        byteBuffer.flip();
//                        CharBuffer charBuffer = CharsetHelper.decode(byteBuffer);
//                        String answer = charBuffer.toString();
//                        System.out.println(Thread.currentThread().getId() + " --- " + answer);
//
//                        String word = getWord();
//                        if (word != null) {
//                            channel.write(CharsetHelper.encode(CharBuffer.wrap(word)));
//                        } else {
//                            isOver = true;
//                        }
//                        sleep();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
