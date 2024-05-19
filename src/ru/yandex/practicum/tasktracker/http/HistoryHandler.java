package ru.yandex.practicum.tasktracker.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.tasktracker.TaskManager;

import java.io.IOException;
import java.util.regex.Pattern;

public class HistoryHandler extends BaseHttpHandler implements HttpHandler {

    protected Gson gson;
    protected TaskManager taskManager;

    public HistoryHandler(Gson gson, TaskManager taskManager) {
        this.gson = gson;
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String requestMethod = exchange.getRequestMethod();

        if (path.charAt(path.length() - 1) == '/')
            path = path.substring(0, path.length() - 1);

        if (requestMethod.equals("GET")) {
            if (Pattern.matches("^/history$", path)) {
                super.sendText(exchange, gson.toJson(taskManager.getHistory()));
            } else {
                super.sendNotFound(exchange, "Страница не найдена");
            }
        } else {
            super.sendNotFound(exchange, "Метод " + "\"" + requestMethod + "\" не поддерживается");
        }
    }
}
