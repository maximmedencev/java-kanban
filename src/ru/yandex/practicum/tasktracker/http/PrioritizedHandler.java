package ru.yandex.practicum.tasktracker.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.tasktracker.TaskManager;

import java.io.IOException;
import java.time.Duration;
import java.util.regex.Pattern;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    protected Gson gson;
    protected TaskManager taskManager;

    public PrioritizedHandler(Gson gson, TaskManager taskManager) {
        this.gson = gson;
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String requestMethod = exchange.getRequestMethod();

        gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();

        if (path.charAt(path.length() - 1) == '/')
            path = path.substring(0, path.length() - 1);

        if (requestMethod.equals("GET")) {
            if (Pattern.matches("^/prioritized$", path)) {
                super.sendText(exchange, gson.toJson(taskManager.getPrioritizedTasks()));
            } else {
                super.sendNotFound(exchange, "Страница не найдена");
            }
        } else {
            super.sendNotFound(exchange, "Метод " + "\"" + requestMethod + "\" не поддерживается");
        }
    }
}
