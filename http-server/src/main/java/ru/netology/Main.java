package ru.netology;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    static ExecutorService executeIt = Executors.newFixedThreadPool(64);

    public static void main(String[] args) {

        try (final var serverSocket = new ServerSocket(9998)) {
            while (!serverSocket.isClosed()) {
                Socket client = serverSocket.accept();
                executeIt.execute(new Server(client));
                System.out.println("Connection accepted");
            }
            executeIt.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


