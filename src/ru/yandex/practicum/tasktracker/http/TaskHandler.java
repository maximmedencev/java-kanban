package ru.yandex.practicum.tasktracker.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.tasktracker.IntersectionException;
import ru.yandex.practicum.tasktracker.NotFoundException;
import ru.yandex.practicum.tasktracker.Task;
import ru.yandex.practicum.tasktracker.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;
import java.util.regex.Pattern;

public class TaskHandler extends BaseHttpHandler implements HttpHandler {

    protected Gson gson;
    protected TaskManager taskManager;

    public TaskHandler(Gson gson, TaskManager taskManager) {
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

        switch (requestMethod) {
            case "GET": {
                if (Pattern.matches("^/tasks$", path)) {
                    super.sendText(exchange, gson.toJson(taskManager.getTasksList()));
                } else if (Pattern.matches("^/tasks/\\d+$", path)) {
                    String strTaskId = path.replaceFirst("/tasks/", "");
                    Task task = null;
                    try {
                        task = taskManager.getTask(Integer.parseInt(strTaskId));
                    } catch (NotFoundException e) {
                        super.sendNotFound(exchange, "Задача не найдена");
                    }
                    super.sendText(exchange, gson.toJson(task));
                } else {
                    super.sendNotFound(exchange, "Страница не найдена");
                }
                break;
            }
            case "POST": {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Task task = gson.fromJson(body, Task.class);
                if (task.getId() == 0) {
                    try {
                        taskManager.addTask(task);
                    } catch (IntersectionException e) {
                        super.sendHasInteractions(exchange, "Задача пересекается с существующими");
                    }
                    super.sendText(exchange, "Задача успешно добавлена");
                } else {

                    try {
                        taskManager.updateTask(task);
                    } catch (IntersectionException e) {
                        super.sendHasInteractions(exchange, "Задача пересекается с существующими");
                    }
                    super.sendText(exchange, "Задача успешно обновлена");
                }
                break;
            }
            case "DELETE": {
                List<String> strTaskIdList;
                if (exchange.getRequestHeaders().containsKey("X-Task-Id")) {
                    strTaskIdList = exchange.getRequestHeaders().get("X-Task-Id");
                    String strTaskId = strTaskIdList.get(0);
                    taskManager.removeTask(Integer.parseInt(strTaskId));
                    super.sendText(exchange, "Удалена задача id = " + strTaskId);
                }
                break;
            }
            default: {
                super.sendNotFound(exchange, "Метод " + "\"" + requestMethod + "\" не поддерживается");
            }
        }
    }
}
