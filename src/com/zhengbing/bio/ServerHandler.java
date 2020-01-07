package com.zhengbing.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 服务端处理客户端线程处理链路
 * @author zhengbing
 */
public class ServerHandler implements Runnable {

    private Socket socket;

    public ServerHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {

        BufferedReader in = null;
        PrintWriter out = null;
        String expression;
        String result;

        try{
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out  = new PrintWriter(socket.getOutputStream(),true);
            while(true){
                if ((expression=in.readLine())==null) {
                  break;
                }
                System.out.println("服务端收到消息："+expression);
//               result = Calcuator.cal(expression)
                result = "服务端得到："+expression;
                out.println(result);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            SocketUtils.closeed(in,out,socket);
        }

    }
}
