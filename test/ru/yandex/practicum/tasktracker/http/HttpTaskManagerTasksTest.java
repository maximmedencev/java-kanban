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

public class HttpTaskManagerTasksTest {
    // создаём экземпляр InMemoryTaskManager
    TaskManager manager = new InMemoryTaskManager();
    // передаём его в качестве аргумента в конструктор HttpTaskServer
    HttpTaskServer taskServer = new HttpTaskServer(manager);
    Gson gson = HttpTaskServer.getGson();

    public HttpTaskManagerTasksTest() throws IOException {
    }

    static class TaskListTypeToken extends TypeToken<List<Task>> {
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
    public void testGetTasks() throws IOException, InterruptedException, IntersectionException {
        // создаём задачу
        Task task1 = new Task(5, "Task1 name",
                "Task1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 15, 0),
                Duration.ofMinutes(30));

        Task task2 = new Task(6, "Task2 name",
                "Task2 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 16, 0),
                Duration.ofMinutes(30));

        Task task3 = new Task(7, "Task3 name",
                "Task3 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 17, 0),
                Duration.ofMinutes(30));

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = manager.getTasksList();
        List<Task> tasksFromResponse = gson.fromJson(response.body(), new TaskListTypeToken().getType());

        assertTrue(TaskTest.taskFieldsEquals(tasksFromManager.get(0), tasksFromResponse.get(0)), "Поля задач не совпадают");
        assertTrue(TaskTest.taskFieldsEquals(tasksFromManager.get(1), tasksFromResponse.get(1)), "Поля задач не совпадают");
        assertTrue(TaskTest.taskFieldsEquals(tasksFromManager.get(2), tasksFromResponse.get(2)), "Поля задач не совпадают");
    }

    @Test
    public void testGetTask() throws IOException, InterruptedException, IntersectionException, NotFoundException {
        // создаём задачу
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

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        Task taskFromResponse = gson.fromJson(response.body(), Task.class);

        assertTrue(TaskTest.taskFieldsEquals(manager.getTask(2), taskFromResponse),
                "Поля задач не совпадают");
    }

    @Test
    public void shouldGet404WhenTaskDoesNotExist() throws IOException, InterruptedException {

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/2");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(404, response.statusCode());
    }

    @Test
    public void testAddTask() throws IOException, InterruptedException {
        // создаём задачу
        Task task = new Task("Task 2", "Desc task 2",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));
        // конвертируем её в JSON
        String taskJson = gson.toJson(task);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        // проверяем, что создалась одна задача с корректным именем
        List<Task> tasksFromManager = manager.getTasksList();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Task 2", tasksFromManager.get(0).getName(), "Некорректное имя задачи");
    }

    @Test
    public void shouldGet406WhenIntersects() throws IOException, InterruptedException, IntersectionException {

        // создаём задачу
        Task task1 = new Task("Task 1", "Desc task 1",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));

        manager.addTask(task1);

        Task task2 = new Task("Task 2", "Desc task 2",
                TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(5));

        // конвертируем её в JSON
        String taskJson = gson.toJson(task2);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(406, response.statusCode());
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException, IntersectionException, NotFoundException {
        // создаём задачу
        Task task1 = new Task(1, "Task1 name",
                "Task1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 15, 0),
                Duration.ofMinutes(30));

        Task task2 = new Task(1, "Task1 name updated",
                "Task1 description updated",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 15, 0),
                Duration.ofMinutes(30));

        // конвертируем её в JSON
        manager.addTask(task1);

        String taskJson = gson.toJson(task2);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());

        assertEquals("Task1 name updated",
                manager.getTask(1).getName(),
                "Имя задачи не равно ожидаемому после обновления");
    }

    @Test
    public void testDeleteTask() throws IOException, InterruptedException, IntersectionException {
        // создаём задачу
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

        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);

        // создаём HTTP-клиент и запрос
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .header("X-Task-Id", "2")
                .DELETE()
                .build();

        // вызываем рест, отвечающий за создание задач
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        // проверяем код ответа
        assertEquals(200, response.statusCode());
        assertFalse(manager.getTasksList().contains(task2), "Задача не удалена");
    }
}