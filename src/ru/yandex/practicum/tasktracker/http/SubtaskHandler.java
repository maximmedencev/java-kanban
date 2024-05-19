package ru.yandex.practicum.tasktracker.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.tasktracker.IntersectionException;
import ru.yandex.practicum.tasktracker.NotFoundException;
import ru.yandex.practicum.tasktracker.Subtask;
import ru.yandex.practicum.tasktracker.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {

    protected Gson gson;
    protected TaskManager taskManager;

    public SubtaskHandler(Gson gson, TaskManager taskManager) {
        this.gson = gson;
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String requestMethod = exchange.getRequestMethod();

        if (path.charAt(path.length() - 1) == '/')
            path = path.substring(0, path.length() - 1);

        switch (requestMethod) {
            case "GET": {
                if (Pattern.matches("^/subtasks$", path)) {
                    super.sendText(exchange, gson.toJson(taskManager.getSubtasksList()));
                } else if (Pattern.matches("^/subtasks/\\d+$", path)) {
                    String strSubtaskId = path.replaceFirst("/subtasks/", "");
                    Subtask subtask = null;
                    try {
                        subtask = taskManager.getSubtask(Integer.parseInt(strSubtaskId));
                    } catch (NotFoundException e) {
                        super.sendNotFound(exchange, "Подадача не найдена");
                    }
                    super.sendText(exchange, gson.toJson(subtask));
                } else {
                    super.sendNotFound(exchange, "Страница не найдена");
                }
                break;
            }
            case "POST": {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Subtask subtask = gson.fromJson(body, Subtask.class);
                if (subtask.getId() == 0) {
                    try {
                        taskManager.addSubtask(subtask);
                    } catch (IntersectionException e) {
                        super.sendHasInteractions(exchange, "Подзадача пересекается с существующими");
                    }
                    super.sendText(exchange, "Подзадача успешно добавлена");
                } else {
                    try {
                        taskManager.updateSubtask(subtask);
                    } catch (IntersectionException e) {
                        super.sendHasInteractions(exchange, "Подзадача пересекается с существующими");
                    }
                    super.sendText(exchange, "Подзадача успешно обновлена");
                }
                break;
            }
            case "DELETE": {
                List<String> strSubtaskIdList;
                if (exchange.getRequestHeaders().containsKey("X-Subtask-Id")) {
                    strSubtaskIdList = exchange.getRequestHeaders().get("X-Subtask-Id");
                    String strSubtaskId = strSubtaskIdList.get(0);
                    taskManager.removeSubtask(Integer.parseInt(strSubtaskId));
                    super.sendText(exchange, "Удалена подзадача id = " + strSubtaskId);
                }
                break;
            }
            default: {
                super.sendNotFound(exchange, "Метод " + "\"" + requestMethod + "\" не поддерживается");
            }
        }
    }
}
