package com.zhengbing.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * @author zhengbing_vendor
 * @date 2020/1/8
 **/
public class NioClientHandler implements Runnable {

    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;

    private volatile  boolean started;

    /**
     * 带参构造器，主要用来参数初始化
     * @param host
     * @param port
     */
    public NioClientHandler(String host, int port) {
        this.host = host;
        this.port = port;
        try{
            // 创建选择器
            selector = Selector.open();
            // 打开监听渠道
            socketChannel = SocketChannel.open();
            // 设置非阻塞模式
            socketChannel.configureBlocking(false);
            started = true;
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void stop() {
        started = false;
    }

    @Override
    public void run() {
        try {
            doConnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        while(started){
            try {
                selector.select(1000);
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                SelectionKey key = null;
                while(it.hasNext()){
                    key = it.next();
                    it.remove();
                    try {
                        handleInput(key);
                    } catch (Exception e) {
                        if (null!=key){
                            key.cancel();
                            if (null!=key.channel()){
                                key.channel().close();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        if (null!=selector){
            try {
                selector.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()){
            SocketChannel sc = (SocketChannel) key.channel();
            if (key.isConnectable()){
                if (!sc.finishConnect()){
                    System.exit(1);
                }
            }
            if (key.isReadable()){
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                int readBytes = sc.read(byteBuffer);
                if (readBytes>0){
                    byteBuffer.flip();
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    byteBuffer.get(bytes);
                    String result = new String(bytes, StandardCharsets.UTF_8);
                    System.out.println("客户端收到消息："+result);
                }
                //没有读取到字节 忽略
//              else if(readBytes==0);
                //链路已经关闭，释放资源
                else if (readBytes<0){
                    key.cancel();
                    sc.close();
                }
            }
        }
    }

    private void doConnect() throws IOException {
        if (!socketChannel.connect(new InetSocketAddress(host,port))){
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
        }
    }

    public void sendMessage(String message) throws IOException {
        socketChannel.register(selector,SelectionKey.OP_READ);
        doWrite(socketChannel,message);
    }

    private void doWrite(SocketChannel socketChannel, String message) throws IOException {

        byte[] bytes = message.getBytes();
        ByteBuffer byteBuffer =  ByteBuffer.allocate(bytes.length);
        byteBuffer.put(bytes);
        byteBuffer.flip();
        socketChannel.write(byteBuffer);

    }
}
