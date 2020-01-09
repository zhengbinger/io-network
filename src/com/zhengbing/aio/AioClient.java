package com.zhengbing.aio;

import java.util.Scanner;

/**
 * AIO 客户端
 * @author zhengbing_vendor
 * @date 2020/1/9
 **/
public class AioClient {

    private static final String DEFAULT_SERVER_HOST = "127.0.0.1";
    private static final int DEFAULT_PORT = 5555;
    private static AioClientHandler clientHandler;

    public static void start() {
        start(DEFAULT_SERVER_HOST,DEFAULT_PORT);
    }

    public static synchronized void start(String host, int port) {
        if (null!=clientHandler){
            return;
        }
        clientHandler = new AioClientHandler(host,port);
        new Thread(clientHandler,"Client").start();
    }

    public static boolean sendMessage(String message){
        if ("q".equals(message)){
            return false;
        }
        clientHandler.sendMessage(message);
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        start();
        while (true){
            Thread.sleep(100);
            System.out.println("请输入需要计算的表达式：");
            Scanner scanner = new Scanner(System.in);
            String message = scanner.nextLine();
            if (!"quit".equals(message)) {
                sendMessage(message);
            }else{
                break;
            }
        }
    }

}
