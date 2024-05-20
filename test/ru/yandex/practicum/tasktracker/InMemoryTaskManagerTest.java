package ru.yandex.practicum.tasktracker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void setUp() {
        super.taskManager = (InMemoryTaskManager) Managers.getDefault();
    }

    //InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
    @Test
    public void inMemoryTaskManagerShouldAddAndSeekTasks() {
        Task task1 = new Task("Name", "Description");
        super.taskManager.addTask(task1);
        Assertions.assertNotNull(super.taskManager.getTask(task1.getId()), "task равен null");
        Assertions.assertEquals(task1.getId(), super.taskManager.getTask(task1.getId()).getId(),
                "по заданному id не содержится нужного объекта");

    }

    //InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
    @Test
    public void inMemoryTaskManagerShouldAddAndSeekSubtasks() {
        Epic epic1 = new Epic("Name", "Description");
        Subtask subtask1 = new Subtask("Name",
                "Description",
                TaskStatus.NEW, LocalDateTime.now(),
                Duration.ofMinutes(30));
        super.taskManager.addEpic(epic1);
        super.taskManager.addSubtask(epic1.getId(), subtask1);
        int subtask1Id = subtask1.getId();
        Assertions.assertNotNull(super.taskManager.getSubtask(subtask1.getId()),
                "subtask равен null");
        Assertions.assertEquals(subtask1Id,
                super.taskManager.getSubtask(subtask1.getId()).getId(),
                "по заданному id не содержится нужного объекта");
    }

    //InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
    @Test
    public void inMemoryTaskManagerShouldAddAndSeekEpics() {
        Epic epic1 = new Epic("Name", "Description");
        super.taskManager.addEpic(epic1);
        int epic1Id = epic1.getId();
        Assertions.assertNotNull(super.taskManager.getEpic(epic1Id),
                "epic равен null");
        Assertions.assertEquals(epic1Id, super.taskManager.getEpic(epic1.getId()).getId(),
                "по заданному id не содержится нужного объекта");
    }

    //задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера
    @Test
    public void shouldReturnMinus1IfTasksIdsConflict() {
        Task task1 = new Task("Name1", "Description1");
        Task task2 = new Task("Name2", "Description2");
        super.taskManager.addTask(task1);
        Assertions.assertEquals(-1, super.taskManager.addTask(task1.getId(), task2),
                "конфликт id не обнаружен");
    }

    //задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера
    @Test
    public void shouldReturnMinus1IfEpicsIdsConflict() {
        Epic epic1 = new Epic("Name1", "Description1");
        Epic epic2 = new Epic("Name2", "Description2");
        super.taskManager.addEpic(epic1);
        Assertions.assertEquals(-1, super.taskManager.addEpic(epic1.getId(), epic2),
                "конфликт id не обнаружен");
    }

    //тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    public void taskFieldsShouldBeSameAfterAddingToTaskManager() {
        Task task1 = new Task(1,
                "Name1",
                "Description1",
                TaskStatus.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(30));
        int id = task1.getId();
        String name = task1.getName();
        String description = task1.getDescription();
        TaskStatus taskStatus = task1.getStatus();
        super.taskManager.addTask(1, task1);
        Assertions.assertEquals(id, task1.getId(), "Значения полей различаются");
        Assertions.assertEquals(name, task1.getName(), "Значения полей различаются");
        Assertions.assertEquals(description, task1.getDescription(), "Значения полей различаются");
        Assertions.assertEquals(taskStatus, task1.getStatus(), "Значения полей различаются");
    }

    //тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    public void epicFieldsShouldBeSameAfterAddingToTaskManager() {
        Epic epic1 = new Epic(1, "Name1", "Description1");
        Epic epic2 = new Epic(epic1);
        super.taskManager.addEpic(1, epic1);
        Assertions.assertEquals(epic1.getSubtasksIds(),
                epic2.getSubtasksIds(),
                "Значения полей различаются");
        Assertions.assertEquals(epic2, epic1, "Значения полей различаются");
    }

    //тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    public void subtaskFieldsShouldBeSameAfterAddingToTaskManager() {
        Epic epic1 = new Epic("Name1", "Description1");
        super.taskManager.addEpic(1, epic1);
        Subtask subtask1 = new Subtask(2,
                1,
                "Name2",
                "Description2",
                TaskStatus.NEW, LocalDateTime.now(),
                Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(subtask1);
        super.taskManager.addSubtask(2, 1, subtask1);
        Assertions.assertEquals(subtask2, subtask1, "Значения полей различаются");
        Assertions.assertEquals(subtask2.getEpicId(),
                subtask1.getEpicId(),
                "Значения полей различаются");
    }

    @Test
    public void shouldNotBeNotActualSubtasksIdsAfterRemoving() {
        Epic epic1 = new Epic("Name1", "Description1");
        super.taskManager.addEpic(1, epic1);
        Subtask subtask1 = new Subtask(2,
                1,
                "Name2",
                "Description2",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 12, 0, 0),
                Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(3,
                1,
                "Name3",
                "Description3",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 13, 0, 0),
                Duration.ofMinutes(30));
        Subtask subtask3 = new Subtask(4,
                1,
                "Name4",
                "Description4",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 14, 0, 0),
                Duration.ofMinutes(30));

        super.taskManager.addSubtask(subtask1.getId(), 1, subtask1);
        super.taskManager.addSubtask(subtask2.getId(), 1, subtask2);
        super.taskManager.addSubtask(subtask3.getId(), 1, subtask3);

        super.taskManager.removeSubtask(3);
        super.taskManager.removeSubtask(4);

        Assertions.assertFalse(super.taskManager.getEpic(1).getSubtasksIds().contains(3));
        Assertions.assertFalse(super.taskManager.getEpic(1).getSubtasksIds().contains(4));
    }

    @Test
    public void taskShouldNotBeAddedWhenIntersects() {
        Task task1 = new Task(1, "Task1 name",
                "Task1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 12, 0, 0),
                Duration.ofMinutes(30));

        Task task2 = new Task(2, "Task2 name",
                "Task2 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 12, 15, 0),
                Duration.ofMinutes(30));

        Task task3 = new Task(3, "Task3 name",
                "Task3 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 14, 0, 0),
                Duration.ofMinutes(30));

        super.taskManager.addTask(task1);
        Assertions.assertThrows(IntersectionException.class, () -> super.taskManager.addTask(task2));
        super.taskManager.addTask(task3);

        Assertions.assertTrue(super.taskManager.getTasksList().contains(task1));
        Assertions.assertFalse(super.taskManager.getTasksList().contains(task2));
        Assertions.assertTrue(super.taskManager.getTasksList().contains(task3));
    }

}