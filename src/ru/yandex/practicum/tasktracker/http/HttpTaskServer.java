package ru.yandex.practicum.tasktracker;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create();

        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks",);
        httpServer.createContext("/subtasks", );
        httpServer.createContext("/epics", );
        httpServer.createContext("/history", );
        httpServer.createContext("/prioritized", );

        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        httpServer.stop(1);
    }
}
