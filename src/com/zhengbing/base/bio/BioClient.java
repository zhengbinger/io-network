package com.zhengbing.base.bio;

import com.zhengbing.base.utils.SocketUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

/**
 * 阻塞式I/O 客户端
 * @author  zhengbing
 */
public class BioClient {

    private static final int DEFAULT_SERVER_PORT = 7777;
    private static final String DEFAULT_SERVER_HOST = "localhost";

    public static void send(String expression){
        send(DEFAULT_SERVER_PORT,expression);
    }

    public static void send(int port,String expression){
        System.out.println("表达式为："+expression);
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out  = null;

        try{
            socket = new Socket( DEFAULT_SERVER_HOST,port);
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
            out.println(expression);
            System.out.println("结果为："+in.readLine());
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            SocketUtils.closed(in,out,socket);
        }
    }

    public static void main(String[] args) {
        String[] oprates = {"+","-","*","/"};
        Random random = new Random(System.currentTimeMillis());
        String oprate = oprates[random.nextInt(4)];
        String expression = random.nextInt(10)+oprate+random.nextInt(10);
        BioClient.send(expression);
    }
}
