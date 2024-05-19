package ru.yandex.practicum.tasktracker.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HttpTaskManagerSubtasksTest {
    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public HttpTaskManagerSubtasksTest() throws IOException {
    }

    static class SubtaskListTypeToken extends TypeToken<List<Subtask>> {
    }

    @BeforeEach
    public void setUp() {
        gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
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

    @Test
    public void testGetSubtasks() throws IOException, InterruptedException, IntersectionException {
        // создаём задачу
        Subtask subtask1 = new Subtask(5, "Subtask1 name",
                "Subtask1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 15, 0),
                Duration.ofMinutes(30));

        Subtask subtask2 = new Subtask(6, "Subtask2 name",
                "Subtask2 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 16, 0),
                Duration.ofMinutes(30));

        Subtask subtask3 = new Subtask(7, "Subtask3 name",
                "Subtask3 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 17, 0),
                Duration.ofMinutes(30));

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        List<Subtask> subtasksFromManager = manager.getSubtasksList();
        List<Subtask> subtasksFromResponse = gson.fromJson(response.body(), new SubtaskListTypeToken().getType());


        assertTrue(SubtaskTest.subtasksFieldsEquals(subtasksFromManager.get(0), subtasksFromResponse.get(0)),
                "Поля задач не совпадают");
        assertTrue(SubtaskTest.subtasksFieldsEquals(subtasksFromManager.get(1), subtasksFromResponse.get(1)),
                "Поля задач не совпадают");
        assertTrue(SubtaskTest.subtasksFieldsEquals(subtasksFromManager.get(2), subtasksFromResponse.get(2)),
                "Поля задач не совпадают");
    }

    @Test
    public void testGetSubtask() throws IOException, InterruptedException, IntersectionException, NotFoundException {
        // создаём задачу
        Subtask subtask1 = new Subtask(1, "Subtask1 name",
                "Subtask1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 15, 0),
                Duration.ofMinutes(30));

        Subtask subtask2 = new Subtask(2, "Subtask2 name",
                "Subtask2 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 16, 0),
                Duration.ofMinutes(30));

        Subtask subtask3 = new Subtask(3, "Task3 name",
                "Task3 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 17, 0),
                Duration.ofMinutes(30));

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        Subtask subtaskFromResponse = gson.fromJson(response.body(), Subtask.class);

        assertTrue(SubtaskTest.subtasksFieldsEquals(manager.getSubtask(2), subtaskFromResponse),
                "Поля подзадач не совпадают");
    }

    @Test
    public void shouldGet404WhenSubtaskDoesNotExist() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(404, response.statusCode());
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        // создаём задачу
        Subtask subtask = new Subtask("Subtask 2", "Desc subtask 2",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        // конвертируем её в JSON
        String subtaskJson = gson.toJson(subtask);
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();
        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        // проверяем, что создалась одна задача с корректным именем
        List<Subtask> subtasksFromManager = manager.getSubtasksList();
        assertNotNull(subtasksFromManager, "Подзадачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное количество подзадач");
        assertEquals("Subtask 2", subtasksFromManager.get(0).getName(), "Некорректное имя подзадачи");
    }

    @Test
    public void shouldGet406WhenIntersects() throws IOException, InterruptedException, IntersectionException {
        // создаём задачу
        Subtask subtask1 = new Subtask("Subtask 1", "Desc subtask 1",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        manager.addSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask 2", "Desc subtask 2",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        // конвертируем её в JSON
        String subtaskJson = gson.toJson(subtask2);
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(subtaskJson)).build();
        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(406, response.statusCode());
    }

    @Test
    public void testUpdateSubtask() throws IOException, InterruptedException, IntersectionException, NotFoundException {
        // создаём задачу
        Subtask subtask1 = new Subtask(1, "Subtask1 name",
                "Subtask1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 15, 0),
                Duration.ofMinutes(30));

        Subtask subtask2 = new Subtask(1, "Subtask1 name updated",
                "Subtask1 description updated",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 15, 0),
                Duration.ofMinutes(30));

        // конвертируем её в JSON
        manager.addSubtask(subtask1);

        String subtaskJson = gson.toJson(subtask2);
        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest
                .newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        assertEquals("Subtask1 name updated",
                manager.getSubtask(1).getName(),
                "Имя задачи не равно ожидаемому после обновления");
    }

    @Test
    public void testDeleteSubtask() throws IOException, InterruptedException, IntersectionException {
        // создаём задачу
        Subtask subtask1 = new Subtask(1, "Subtask1 name",
                "Subtask1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 15, 0),
                Duration.ofMinutes(30));

        Subtask subtask2 = new Subtask(2, "Subtask2 name",
                "Subtask2 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 16, 0),
                Duration.ofMinutes(30));

        Subtask subtask3 = new Subtask(3, "Subtask3 name",
                "Subtask3 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 17, 0),
                Duration.ofMinutes(30));

        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("X-Subtask-Id", "2")
                .DELETE()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        assertFalse(manager.getSubtasksList().contains(subtask2), "Подзадача не удалена");
    }
}