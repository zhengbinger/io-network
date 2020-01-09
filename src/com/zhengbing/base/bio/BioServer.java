package com.zhengbing.base.bio;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BIO 服务端实现
 * @author zhengbing
 */
public class BioServer {

    /**
     *     服务端端口号
     */
    private static final int SOCKET_SERVER_PORT = 7777;

    private static ServerSocket server;

    /**
     * 根据传入参数设置监听端口，如果没有参数调用以下方法并使用默认值
     */
    public static void  start() throws Exception{
        //使用默认值
        start(SOCKET_SERVER_PORT);
    }

    /**
     * 服务启动的方法，不会经常使用，使用synchronized 声明同步即可
     * @param port
     * @throws IOException
     */
    public synchronized static void start(int port)throws IOException{
        // 如果服务端实例存在，则不继续执行
        if(null!=server) {
            return;
        }
        try {
            //通过构造函数创建ServerSocket  如果端口合法且空闲，服务端就监听成功
            server = new ServerSocket(port);
            System.out.println("服务端已启动，端口号：" + port);

            // 通过无限循环用来接收客户端请求，如果没有客户端连接，服务端将一直阻塞在acceptor
            while (true) {
                Socket socket = server.accept();

                // 当有新客户端连接进来时，新建一个线程来处理该socket的链路
                new Thread(new ServerHandler(socket)).start();
            }

        }finally {
            if(null!= server){
                server.close();
                System.out.println("服务器关闭");
                server =null;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BioServer.start();
    }





}
