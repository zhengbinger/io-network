package com.zhengbing.base.nio;

import java.io.IOException;
import java.util.Scanner;

/**
 * NIO 客户端
 * @author zhengbing_vendor
 * @date 2020/1/8
 **/
public class NioClient {

    private  static final String DEFAULT_SERVER_HOST = "127.0.0.1";

    private static final int DEFAULT_PORT = 6666;

    private static  NioClientHandler nioClientHandler;

    /**
     *  启动客户端，采用默认主机跟端口
     */
    private static void start(){
        start(DEFAULT_SERVER_HOST,DEFAULT_PORT);
    }

    /**
     * 启动客户端，常规方法
     * @param host   String  服务端主机地址
     * @param port   int     服务端监听端口
     */
    private static synchronized void start(String host, int port) {

        if (null!=nioClientHandler){
            nioClientHandler.stop();
        }
        nioClientHandler = new NioClientHandler(host,port);
        new Thread(nioClientHandler,"client").start();
    }

    /**
     * 向服务端发送消息
     * @param message String
     * @return boolean
     */
    public static boolean sendMessage(String message) throws IOException {
        if ("q".equals(message)){
            return false;
        }
        nioClientHandler.sendMessage(message);
        return true;
    }

    public static void main(String[] args) {
        start();
        while(true){
            try {
                sendMessage(new Scanner(System.in).nextLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
