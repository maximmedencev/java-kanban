package ru.yandex.practicum.tasktracker.http;

import com.google.gson.Gson;
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

public class HttpTaskManagerEpicsTest {
    // создаём экземпляр InMemoryTaskManager
    TaskManager manager;
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer;
    Gson gson;

    public HttpTaskManagerEpicsTest() throws IOException {
        manager = new InMemoryTaskManager();
        taskServer = new HttpTaskServer(manager);
        gson = HttpTaskServer.getGson();
    }

    class EpicListTypeToken extends TypeToken<List<Epic>> {
    }

    @BeforeEach
    public void setUp() {
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
    public void testGetEpics() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic1 = new Epic(5, "Epic1 name",
                "Epic1 description");

        Epic epic2 = new Epic(6, "Epic2 name",
                "Epic2 description");

        Epic epic3 = new Epic(7, "Epic3 name",
                "Epic3 description");

        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addEpic(epic3);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        List<Epic> epicsFromManager = manager.getEpicsList();
        List<Epic> epicsFromResponse = gson.fromJson(response.body(), new EpicListTypeToken().getType());

        assertTrue(EpicTest.epicsFieldsEquals(epicsFromManager.get(0), epicsFromResponse.get(0)),
                "Поля эпиков не совпадают");
        assertTrue(EpicTest.epicsFieldsEquals(epicsFromManager.get(1), epicsFromResponse.get(1)),
                "Поля эпиков не совпадают");
        assertTrue(EpicTest.epicsFieldsEquals(epicsFromManager.get(2), epicsFromResponse.get(2)),
                "Поля эпиков не совпадают");
    }

    @Test
    public void testGetEpic() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic1 = new Epic(1, "Epic1 name",
                "Epics1 description");

        Epic epic2 = new Epic(2, "Epic2 name",
                "Epics2 description");

        Epic epic3 = new Epic(3, "Task3 name",
                "Task3 description");

        manager.addEpic(epic1);
        manager.addEpic(epic2);
        manager.addEpic(epic3);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        Epic epicFromResponse = gson.fromJson(response.body(), Epic.class);

        assertTrue(EpicTest.epicsFieldsEquals(manager.getEpic(2), epicFromResponse),
                "Поля эпиков не совпадают");
    }

    @Test
    public void shouldGet404WhenEpicDoesNotExist() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(404, response.statusCode());
    }

    @Test
    public void testGetEpicSubtasks() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic1 = new Epic(1, "Epic1 name",
                "Epics1 description");

        Subtask subtask1 = new Subtask(1, epic1.getId(), "Subtask1 name",
                "Subtask1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 15, 0),
                Duration.ofMinutes(30));

        Subtask subtask2 = new Subtask(2, epic1.getId(), "Subtask2 name",
                "Subtask2 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 16, 0),
                Duration.ofMinutes(30));

        Subtask subtask3 = new Subtask(3, epic1.getId(), "Subtask3 name",
                "Subtask3 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 17, 0),
                Duration.ofMinutes(30));

        manager.addEpic(epic1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/" + epic1.getId() + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        List<Subtask> epicSubtasksFromResponse = gson.fromJson(response.body(), new HttpTaskManagerSubtasksTest.SubtaskListTypeToken().getType());
        List<Subtask> epicSubtasksFromManager = manager.getEpicSubtaskList(epic1.getId());
        assertEquals(epicSubtasksFromManager, epicSubtasksFromResponse);
    }

    @Test
    public void shouldGet404WhenEpicDoesNotExistForEpicSubtasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/2/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(404, response.statusCode());
    }

    @Test
    public void testAddEpic() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic = new Epic("Epic 2", "Desc epic 2");
        // конвертируем её в JSON
        String epicJson = gson.toJson(epic);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Epic> epicsFromManager = manager.getEpicsList();

        assertNotNull(epicsFromManager, "Эпики не возвращаются");
        assertEquals(1, epicsFromManager.size(), "Некорректное количество эпиков");
        assertEquals("Epic 2", epicsFromManager.get(0).getName(), "Некорректное имя эпик");
    }

    @Test
    public void testUpdateEpic() throws IOException, InterruptedException {
        // создаём задачу
        Epic epic1 = new Epic(1, "Epic1 name",
                "Epic1 description");

        Epic epic2 = new Epic(1, "Epic1 name updated",
                "Epic1 description updated");

        // конвертируем её в JSON
        manager.addEpic(epic1);

        String epicJson = gson.toJson(epic2);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(epicJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        assertEquals("Epic1 name updated",
                manager.getEpic(1).getName(),
                "Имя эпика не равно ожидаемому после обновления");
    }


    @Test
    public void testDeleteEpic() throws IOException, InterruptedException {
        Epic epic1 = new Epic(1, "Epic1 name",
                "Epics1 description");

        Subtask subtask1 = new Subtask(1, epic1.getId(), "Subtask1 name",
                "Subtask1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 15, 0),
                Duration.ofMinutes(30));

        Subtask subtask2 = new Subtask(2, epic1.getId(), "Subtask2 name",
                "Subtask2 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 16, 0),
                Duration.ofMinutes(30));

        Subtask subtask3 = new Subtask(3, epic1.getId(), "Subtask3 name",
                "Subtask3 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 17, 0),
                Duration.ofMinutes(30));

        manager.addEpic(epic1);
        manager.addSubtask(subtask1);
        manager.addSubtask(subtask2);
        manager.addSubtask(subtask3);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("X-Epic-Id", "1")
                .DELETE()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        assertFalse(manager.getEpicsList().contains(epic1), "Эпик не удален");
        assertTrue(manager.getSubtasksList().isEmpty(), "Подзадачи не удалены");
    }
}