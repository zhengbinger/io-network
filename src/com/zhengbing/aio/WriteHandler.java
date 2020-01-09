package com.zhengbing.aio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhengbing_vendor
 * @date 2020/1/9
 **/
public class WriteHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel channel;
    private CountDownLatch latch;

    public WriteHandler(AsynchronousSocketChannel channel, CountDownLatch latch) {
        this.channel = channel;
        this.latch = latch;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        if (buffer.hasRemaining()){
            channel.write(buffer,buffer,this);
        }else{
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            channel.read(readBuffer,readBuffer,new ClientReadHandler(channel,latch));
        }

    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        System.out.println("数据发送失败...");
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            latch.countDown();
        }
    }
}
