package ru.yandex.practicum.tasktracker.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import ru.yandex.practicum.tasktracker.Epic;
import ru.yandex.practicum.tasktracker.NotFoundException;
import ru.yandex.practicum.tasktracker.TaskManager;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;

public class EpicHandler extends BaseHttpHandler implements HttpHandler {

    protected Gson gson;
    protected TaskManager taskManager;

    public EpicHandler(Gson gson, TaskManager taskManager) {
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
                if (Pattern.matches("^/epics$", path)) {
                    super.sendText(exchange, gson.toJson(taskManager.getEpicsList()));
                } else if (Pattern.matches("^/epics/\\d+$", path)) {
                    String strEpicId = path.replaceFirst("/epics/", "");
                    Epic epic = null;
                    try {
                        epic = taskManager.getEpic(Integer.parseInt(strEpicId));
                    } catch (NotFoundException e) {
                        super.sendNotFound(exchange, "Эпик не найден");
                    }
                    super.sendText(exchange, gson.toJson(epic));
                } else if (Pattern.matches("^/epics/\\d+/subtasks$", path)) {
                    String strEpicId = path
                            .replaceFirst("/epics/", "")
                            .replaceFirst("/subtasks", "");
                    Epic epic = null;
                    try {
                        epic = taskManager.getEpic(Integer.parseInt(strEpicId));
                        super.sendText(exchange, gson.toJson(taskManager.getEpicSubtaskList(epic.getId())));
                    } catch (NotFoundException e) {
                        super.sendNotFound(exchange, "Эпик не найден");
                    }
                } else {
                    super.sendNotFound(exchange, "Страница не найдена");
                }
                break;
            }
            case "POST": {
                InputStream inputStream = exchange.getRequestBody();
                String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
                Epic epic = gson.fromJson(body, Epic.class);
                taskManager.addEpic(epic);
                super.sendText(exchange, "Эпик успешно добавлен");
                break;
            }
            case "DELETE": {
                List<String> strEpicIdList;
                if (exchange.getRequestHeaders().containsKey("X-Epic-Id")) {
                    strEpicIdList = exchange.getRequestHeaders().get("X-Epic-Id");
                    String strEpicId = strEpicIdList.get(0);
                    taskManager.removeEpic(Integer.parseInt(strEpicId));
                    super.sendText(exchange, "Удален эпик id = " + strEpicId);
                }
                break;
            }
            default: {
                super.sendNotFound(exchange, "Метод " + "\"" + requestMethod + "\" не поддерживается");
            }
        }
    }
}
