package com.zhengbing.base.nio;

/**
 * NIO 服务端实现
 * @author zhengbing
 * @date 2020-01-08
 */
public class NioServer {

    private static final int  DEFAULT_SERVER_PORT = 6666;

    private static NioServerHandler nioServerHandler;

    public static void start(){
        start(DEFAULT_SERVER_PORT);
    }

    public static synchronized void start(int port) {
        if(null!=nioServerHandler){
            nioServerHandler.stop();
        }
        nioServerHandler = new NioServerHandler(port);

        new Thread(nioServerHandler,"Server").start();
    }

    public static void main(String[] args) {
        start();
    }
}
