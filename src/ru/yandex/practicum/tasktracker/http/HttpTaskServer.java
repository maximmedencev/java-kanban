package ru.yandex.practicum.tasktracker.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import ru.yandex.practicum.tasktracker.FileBackedTaskManager;
import ru.yandex.practicum.tasktracker.IntersectionException;
import ru.yandex.practicum.tasktracker.TaskManager;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private TaskManager taskManager;
    private HttpServer httpServer;
    private Gson gson;

    public HttpTaskServer(TaskManager taskManager) throws IOException {
        this.taskManager = taskManager;
        gson = new Gson();
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
    }

    public static Gson getGson() {
        return new Gson();
    }

    public static void main(String[] args) throws IntersectionException, IOException {

        File file = new File("java-kanban-save.csv");
        TaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        HttpTaskServer httpTaskServer = new HttpTaskServer(fileBackedTaskManager);
        httpTaskServer.start();
        httpTaskServer.stop();
    }

    public void start() {
        httpServer.createContext("/tasks", new TaskHandler(gson, taskManager));
        httpServer.createContext("/subtasks", new SubtaskHandler(gson, taskManager));
        httpServer.createContext("/epics", new EpicHandler(gson, taskManager));
        httpServer.createContext("/history", new HistoryHandler(gson, taskManager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(gson, taskManager));

        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpServer.stop(1);
    }
}