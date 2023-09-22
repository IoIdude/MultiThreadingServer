package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.lang.System.out;

public class Main {
    public static void main(String[] args) throws Exception {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(
                0,
                4,
                0L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>()
        );

        new Thread(() -> {
            for (;;) {
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                out.println("Threads active: " + pool.getActiveCount());
                out.println("Pool size: " + pool.getPoolSize());
                out.println("Task count: " + pool.getTaskCount());
            }
        }).start();

        //pool.prestartAllCoreThreads();

        ServerSocket server = new ServerSocket(1488);
        for (;;) {
            Socket client = server.accept();
            pool.execute(new SocketListener(client));
        }
    }
}