package com.zhengbing.base.pio;

import com.zhengbing.base.utils.SocketUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 服务端处理客户端线程处理链路
 * @author zhengbing
 */
public class PioServerHandler implements Runnable {

    private Socket socket;
    private String[] quitCommand = {"关闭","shutdown","stop"};

    public PioServerHandler(Socket socket){
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
                result = "服务端得到："+expression;
                out.println(result);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            SocketUtils.closed(in,out,socket);
        }

    }
}
