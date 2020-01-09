package com.zhengbing.aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhengbing_vendor
 * @date 2020/1/9
 **/
public class ClientReadHandler implements CompletionHandler<Integer, ByteBuffer> {

    private AsynchronousSocketChannel channel;
    private CountDownLatch latch;

    public ClientReadHandler(AsynchronousSocketChannel channel, CountDownLatch latch) {
        this.channel = channel;
        this.latch = latch;
    }

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        buffer.flip();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);
        String body = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("客户端收到服务端响应结果：" + body);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        System.out.println("数据读取失败...");
        exc.printStackTrace();
        try {
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            latch.countDown();
        }

    }
}
