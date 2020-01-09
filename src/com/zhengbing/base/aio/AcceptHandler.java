package com.zhengbing.base.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

/**
 * 接收客户端连接
 * @author zhengbing_vendor
 * @date 2020/1/9
 **/
public class AcceptHandler implements CompletionHandler<AsynchronousSocketChannel, AioServerHandler> {


    @Override
    public void completed(AsynchronousSocketChannel channel, AioServerHandler serverHandler) {
        // 继续接收其他客户端请求
        AioServer.clientCount++;
        System.out.println("连接客户端的连接数："+ AioServer.clientCount);
        serverHandler.channel.accept(serverHandler,this);
        // 创建新的buffer
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        // 异步读,第三个参数为接收消息回调的业务Handler
        channel.read(buffer,buffer,new ServerReadHandler(channel));
    }

    @Override
    public void failed(Throwable exc, AioServerHandler serverHandler) {
        exc.printStackTrace();
        serverHandler.latch.countDown();
    }
}
