package ru.yandex.practicum.tasktracker.http;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HttpTaskManagerHistoryTest {
    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    public static Gson gson = HttpTaskServer.getGson();

    public static Type type = new TypeToken<List<? extends Task>>() {
    }.getType();


    public HttpTaskManagerHistoryTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        gson = new GsonBuilder()
                .registerTypeAdapter(type, new JsonResponseDeserialize())
                .create();
        manager.removeAllTasks();
        manager.removeAllSubtasks();
        manager.removeAllEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    public static class JsonResponseDeserialize implements JsonDeserializer<List<? extends Task>> {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();

        @Override
        public List<? extends Task> deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
            List<Task> listToReturn = new ArrayList<>();
            JsonArray jsonArray = element.getAsJsonArray();
            for (JsonElement jsonElement : jsonArray) {
                Task task;
                if (jsonElement.getAsJsonObject().get("epicId") != null) {
                    task = gson.fromJson(jsonElement, Subtask.class);
                    if (task != null) {
                        listToReturn.add(task);
                        continue;
                    }
                }
                if (jsonElement.getAsJsonObject().get("subtasksIds") != null) {
                    task = gson.fromJson(jsonElement, Epic.class);
                    if (task != null) {
                        listToReturn.add(task);
                        continue;
                    }
                }
                if (jsonElement.getAsJsonObject().get("epicId") == null && jsonElement.getAsJsonObject().get("subtasksIds") == null) {
                    task = gson.fromJson(jsonElement, Task.class);
                    if (task != null) {
                        listToReturn.add(task);
                    }
                }
            }
            return listToReturn;
        }
    }

    @Test
    public void testGetHistory() throws IOException, InterruptedException, IntersectionException, NotFoundException {
        Task task1 = new Task(1, "Task1 name",
                "Task1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 15, 0),
                Duration.ofMinutes(30));

        Task task2 = new Task(2, "Task2 name",
                "Task2 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 16, 0),
                Duration.ofMinutes(30));

        Task task3 = new Task(3, "Task3 name",
                "Task3 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 17, 0),
                Duration.ofMinutes(30));

        Epic epic1 = new Epic(4, "Epic1 name",
                "Epics1 description");

        Epic epic2 = new Epic(5, "Epic2 name",
                "Epics2 description");

        Subtask subtask1 = new Subtask(6, epic1.getId(), "Subtask1 name",
                "Subtask1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 18, 0),
                Duration.ofMinutes(30));

        Subtask subtask2 = new Subtask(7, epic1.getId(), "Subtask2 name",
                "Subtask2 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 19, 0),
                Duration.ofMinutes(30));

        Subtask subtask3 = new Subtask(8, epic1.getId(), "Subtask3 name",
                "Subtask3 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 20, 0),
                Duration.ofMinutes(30));

        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        manager.getTask(task1.getId());
        manager.getSubtask(subtask1.getId());
        manager.getEpic(epic1.getId());
        manager.getEpic(epic2.getId());

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        List<? extends Task> allTasksFromResponse = gson.fromJson(response.body(), type);

        assertTrue((TaskTest.taskFieldsEquals(allTasksFromResponse.get(0), task1)),
                "Задача находится на неверном месте или отсутствует в истории");
        assertTrue((SubtaskTest.subtasksFieldsEquals((Subtask) allTasksFromResponse.get(1), subtask1)),
                "Подадача находится на неверном месте или отсутствует в истории");
        assertTrue(EpicTest.epicsFieldsEquals((Epic) allTasksFromResponse.get(2), epic1),
                "Эпик находится на нверном месте или отсутствует в истории");
        assertTrue(EpicTest.epicsFieldsEquals((Epic) allTasksFromResponse.get(3), epic2),
                "Эпик находится на нверном месте или отсутствует в истории");
    }
}
