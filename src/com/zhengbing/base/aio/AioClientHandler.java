package com.zhengbing.base.aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhengbing_vendor
 * @date 2020/1/9
 **/
public class AioClientHandler implements Runnable, CompletionHandler<Void, AioClientHandler> {

    private String host;
    private int port;
    private CountDownLatch latch;
    private AsynchronousSocketChannel clientChannel;

    /**
     * 属性初始化
     * @param host  服务端主机
     * @param port  服务端端口
     */
    public AioClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
        try {
            // 创建异步的客户端通道
            clientChannel = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // 创建CountDownLatch等待
        latch = new CountDownLatch(1);
        // 发起异步连接操作，回调参数就是这个类本身，如果连接成功会回调completed方法
        clientChannel.connect(new InetSocketAddress(host,port),this,this);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 向服务器发送消息
     * @param message
     */
    public void sendMessage(String message) {

        byte[] bytes = message.getBytes();
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        clientChannel.write(writeBuffer,writeBuffer,new WriteHandler(clientChannel,latch));
    }

    @Override
    public void completed(Void result, AioClientHandler attachment) {
        System.out.println("客户端成功连接到服务器...");
    }

    @Override
    public void failed(Throwable exc, AioClientHandler attachment) {
        System.out.println("连接到服务器失败...");
        exc.printStackTrace();
        try {
            clientChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            attachment.latch.countDown();
        }
    }
}
