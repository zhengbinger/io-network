package com.zhengbing.aio;

import com.zhengbing.utils.Calculator;

import javax.script.ScriptException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.StandardCharsets;

/**
 * @author zhengbing_vendor
 * @date 2020/1/9
 **/
public class ServerReadHandler implements CompletionHandler<Integer, ByteBuffer> {

    /**
     * 用于读取半包消息和发送应答
     */
    private AsynchronousSocketChannel channel;

    public ServerReadHandler(AsynchronousSocketChannel channel) {
        this.channel = channel;
    }

    /**
     * 读取到消息后的处理
     * @param result
     * @param attachment
     */
    @Override
    public void completed(Integer result, ByteBuffer attachment) {
        attachment.flip();
        byte[] message = new byte[attachment.remaining()];
        attachment.get(message);

        String expression = new String(message, StandardCharsets.UTF_8);
        System.out.println("服务器收到的消息："+expression);
        String calcResult = null;
        try {
            calcResult = Calculator.cal(expression).toString();
        } catch (ScriptException e) {
            calcResult = "计算错误"+e.getMessage();
        }
        doWrite(calcResult);
    }

    /**
     * 发送消息
     * @param calcResult
     */
    private void doWrite(String calcResult) {
        byte[] bytes = calcResult.getBytes();
        ByteBuffer writeBuffer= ByteBuffer.allocate(bytes.length);
        writeBuffer.put(bytes);
        writeBuffer.flip();
        channel.write(writeBuffer, writeBuffer, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                // 如果没有发送完，就继续发送直到完成
                if (attachment.hasRemaining()){
                    channel.write(writeBuffer,writeBuffer,this);
                }else {
                    ByteBuffer readBuffer = ByteBuffer.allocate(1024);
                    //异步读  第三个参数为接收消息回调的业务Handler
                    channel.read(readBuffer,readBuffer,new ServerReadHandler(channel));
                }
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        try {
            this.channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
