package com.zhengbing.base.aio;

/**
 * AIO 服务端
 * @author zhengbing_vendor
 * @date 2020/1/9
 **/
public class AioServer {

    private static final int DEFAULT_SERVER_PORT = 5555;
    private static AioServerHandler aioServerHandler;
    public volatile static  long clientCount=0;
    
    public static void start(){
        start(DEFAULT_SERVER_PORT);
    }

    private static void start(int port) {
        if (null!=aioServerHandler){
            return;
        }
        aioServerHandler = new AioServerHandler(port);
        new Thread(aioServerHandler,"Server").start();
    }

    public static void main(String[] args) {
        start();
    }
}
