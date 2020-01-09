package com.zhengbing.example.bio;

import com.sun.org.apache.xpath.internal.operations.String;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/**
 * BIO 聊天程序服务端
 * @author zhengbing_vendor
 * @date 2020/1/9
 **/
public class BioChatServer {

    public static final int DEFAULT_SERVER_PORT = 8999;

    private static ServerSocket server;
    public static void start(){
        start(DEFAULT_SERVER_PORT);
    }

    public static void start(int port) {

        try {
            server = new ServerSocket(port);
            Socket socket = server.accept();
            readContent(socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void readContent(Socket socket) {
    }

    public static void stop() {
        if (null!=server){
            try {
                server.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
