package com.zhengbing.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * socket 工具类
 */
public class SocketUtils {

    /**
     * socket 关闭相关对象
     * @param in
     * @param out
     * @param socket
     */
    public static void closed(BufferedReader in, PrintWriter out, Socket socket){
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
