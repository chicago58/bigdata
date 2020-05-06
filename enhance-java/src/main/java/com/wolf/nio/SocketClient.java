package com.wolf.nio;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketClient implements Runnable {

    public void run() {
        try {
            Socket client = new Socket("localhost", 8383);
            OutputStream outputStream = client.getOutputStream();
            InputStream inputStream = client.getInputStream();
//            outputStream.write((clientNum + "").getBytes());
//            outputStream.flush();
            byte[] bytes = new byte[1024];
            int read = inputStream.read(bytes, 0, 1024);
            System.out.println("From Server: " + read + " bytes -- ");

            client.shutdownOutput();
            client.shutdownInput();
            client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(new SocketClient()).start();
    }
}
