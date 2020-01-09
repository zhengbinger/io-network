package com.zhengbing.base.bio;

import com.zhengbing.base.utils.SocketUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.stream.Stream;

/**
 * 服务端处理客户端线程处理链路
 * @author zhengbing
 */
public class ServerHandler implements Runnable {

    private Socket socket;
    String[] quitCommand = {"关闭","shutdown","stop"};

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
                boolean isQuit=Stream.of(quitCommand).anyMatch(expression::equalsIgnoreCase);

                if (isQuit){
                    SocketUtils.closed(in,out,socket);
                }else{
                    result = "服务端得到："+expression;
                    out.println(result);
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            SocketUtils.closed(in,out,socket);
        }

    }
}
