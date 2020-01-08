package com.zhengbing.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Set;

/**
 * NIO 服务端实现
 * @author zhengbing
 * @date 2020-01-08
 */
public class NioServerHandler implements Runnable {


    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private volatile boolean started;

    public NioServerHandler(int port) {

        try {
            // 创建选择器
            selector = Selector.open();
            // 打开监听通道
            serverSocketChannel = ServerSocketChannel.open();
            // 设置通道阻塞 true/非阻塞模式false
            serverSocketChannel.configureBlocking(false);
            // 绑定端口  1024 ,请求传入链接的最大传入数据长度
            serverSocketChannel.socket().bind(new InetSocketAddress(port),1024);
            // 监听客户端连接请求
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            // 标记服务端已启动
            started = true;
            System.out.println("服务端已启动，端口号："+port);
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }

    }

    public void stop() {
        // 标记服务端关闭
        started = false;
    }

    @Override
    public void run() {

        // 循环遍历selector
        while(started){
            try {
                // 无论是否有读写事件发生，selector 每秒进行一次轮询
                selector.select(1000);
                //阻塞,只有当至少一个注册的事件发生的时候才会继续.
//                selector.select();

                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> it = keys.iterator();
                SelectionKey key = null;
                while(it.hasNext()){
                    key = it.next();
                    it.remove();
                    try{
                        handleInput(key);
                    }catch (Exception e){
                        if (null!=key){
                            key.cancel();
                            if (null!=key.channel()){
                                key.channel().close();
                            }
                        }
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
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

    /**
     * 读取通道中的消息
     *
     * @param key
     * @throws IOException
     */
    private void handleInput(SelectionKey key) throws IOException {
        if (key.isValid()){
            // 处理接入的请求消息
            // 通过SelctionKey 的操作位进行判断即可获知网络事件类型
            if (key.isAcceptable()){
                ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                SocketChannel sc = ssc.accept();
                // 以上操作，相当于完成了TCP/IP 三次握手，TCP 物理链路正式建立

                // 将新创建的SocketChannel设置位异步非阻塞，同时也可以对其TCP参数进行设置，例如，TCP接收和发送的缓冲区的大小
                sc.configureBlocking(false);
                // 注册一个新的连接到Selector
                sc.register(selector,SelectionKey.OP_READ);
            }
            if (key.isReadable()){
                // 读取数据
                SocketChannel sc = (SocketChannel)key.channel();
                // 创建ByteByffer 并开辟一个1K的缓冲区
                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                // 读取请求码流，并返回读取到的字节数
                int readBytes = sc.read(byteBuffer);
                // 读取到字节，对字节进行编解码
                if (readBytes>0){
                    // 将缓冲区当前的limit设置为position=0,用于后续对缓冲区的读取操作
                    byteBuffer.flip();
                    // 根据缓冲区的可读字节数，创建字节数组
                    byte[] bytes = new byte[byteBuffer.remaining()];
                    // 将缓冲区可读字节数组复制到新建的数组中
                    byteBuffer.get(bytes);
                    // 将读取到的数据转换成字符串
                    String expression = new String(bytes, StandardCharsets.UTF_8);
                    String result  = "服务器收到消息：" + expression;
                    System.out.println(result);
                    // 发送应答消息
                    doWrite(sc,result);

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

    /**
     * 异步发送应答消息
     *
     * @param sc SocketChannel
     * @param response String
     */
    private void doWrite(SocketChannel sc, String response) throws IOException {

        // 将消息编码为字节数组
        byte[] bytes = response.getBytes();
        // 根据字节数组容量创建buffer
        ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
        // 将字节数组复制到缓冲区
        writeBuffer.put(bytes);
        // flip
        writeBuffer.flip();
        // 发送缓冲区数据
        sc.write(writeBuffer);
        // 此处不包含写半包的操作？？？
    }
}
