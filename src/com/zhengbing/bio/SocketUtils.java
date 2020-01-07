package com.zhengbing.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketUtils {

    public static void closeed(BufferedReader in, PrintWriter out, Socket socket){
        if (null!=in){
            try{
                in.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        if (null!=out){
            out.close();
        }
        if (null!=socket){
            try{
                socket.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
