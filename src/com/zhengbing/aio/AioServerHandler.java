package com.zhengbing.aio;

import jdk.management.resource.internal.inst.AsynchronousServerSocketChannelImplRMHooks;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhengbing_vendor
 * @date 2020/1/9
 **/
public class AioServerHandler implements Runnable {

    public CountDownLatch latch;
    public AsynchronousServerSocketChannel channel;
    public AioServerHandler(int port) {

        try {
            // 创建服务端通道
            channel = AsynchronousServerSocketChannel.open();
            // 绑定端口
            channel.bind(new InetSocketAddress(port));
            System.out.println("服务端已启动，端口号："+port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        // CountDownLatch 初始化
        // 它的作用：在完成一组正在执行的操作之前，允许当前的线程一直阻塞
        //此处，让线程在此阻塞，防止服务端执行完成后退出
        //也可以使用while(true)+sleep
        //生产环境就不需要担心这个问题，因为服务端是不会退出的
        latch = new CountDownLatch(1);
        // 接收客户端的连接
        channel.accept(this,new AcceptHandler());

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
