package com.zhengbing.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 客户端线程处理链路
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

        try{
            in  = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out  = new PrintWriter(socket.getOutputStream(),true);
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
