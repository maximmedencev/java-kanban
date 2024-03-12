package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.*;

class InMemoryTaskManagerTest {

    public static TaskManager inMemoryTaskManager;

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = Managers.getDefault();
    }

    //InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
    @Test
    public void inMemoryTaskManagerShouldAddAndSeekTasks() {
        Task task1 = new Task("Name", "Description");
        inMemoryTaskManager.addTask(task1);
        Assertions.assertNotNull(inMemoryTaskManager.getTask(task1.getId()), "task равен null");
        Assertions.assertEquals(task1.getId(), inMemoryTaskManager.getTask(task1.getId()).getId(),
                "по заданному id не содержится нужного объекта");

    }

    //InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
    @Test
    public void inMemoryTaskManagerShouldAddAndSeekSubtasks() {
        Epic epic1 = new Epic("Name", "Description");
        Subtask subtask1 = new Subtask("Name", "Description", TaskStatus.NEW);
        inMemoryTaskManager.addEpic(epic1);
        inMemoryTaskManager.addSubtask(epic1.getId(), subtask1);
        int subtask1Id = subtask1.getId();
        Assertions.assertNotNull(inMemoryTaskManager.getSubtask(subtask1.getId()),
                "subtask равен null");
        Assertions.assertEquals(subtask1Id, inMemoryTaskManager.getSubtask(subtask1.getId()).getId(),
                "по заданному id не содержится нужного объекта");
    }

    //InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id;
    @Test
    public void inMemoryTaskManagerShouldAddAndSeekEpics() {
        Epic epic1 = new Epic("Name", "Description");
        inMemoryTaskManager.addEpic(epic1);
        int epic1Id = epic1.getId();
        Assertions.assertNotNull(inMemoryTaskManager.getEpic(epic1Id),
                "epic равен null");
        Assertions.assertEquals(epic1Id, inMemoryTaskManager.getEpic(epic1.getId()).getId(),
                "по заданному id не содержится нужного объекта");
    }

    //задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера
    @Test
    public void shouldReturnMinus1IfTasksIdsConflict() {
        Task task1 = new Task("Name1", "Description1");
        Task task2 = new Task("Name2", "Description2");
        inMemoryTaskManager.addTask(task1);
        Assertions.assertEquals(-1, inMemoryTaskManager.addTask(task1.getId(), task2),
                "конфликт id не обнаружен");
    }

    //задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера
    @Test
    public void shouldReturnMinus1IfEpicsIdsConflict() {
        Epic epic1 = new Epic("Name1", "Description1");
        Epic epic2 = new Epic("Name2", "Description2");
        inMemoryTaskManager.addEpic(epic1);
        Assertions.assertEquals(-1, inMemoryTaskManager.addEpic(epic1.getId(), epic2),
                "конфликт id не обнаружен");
    }

    //задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера
    @Test
    public void shouldReturnMinus1IfSubtasksIdsConflict() {
        Subtask subtask1 = new Subtask("Name1", "Description1", TaskStatus.NEW);
        Subtask subtask2 = new Subtask("Name2", "Description2", TaskStatus.NEW);
        Epic epic1 = new Epic("Name1", "Description1");
        inMemoryTaskManager.addEpic(epic1);
        inMemoryTaskManager.addSubtask(epic1.getId(), subtask1);
        Assertions.assertEquals(-1, inMemoryTaskManager.addSubtask(subtask1.getId(), epic1.getId(), subtask2),
                "конфликт id не обнаружен");
    }

    //тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    public void taskFieldsShouldBeSameAfterAddingToTaskManager() {
        Task task1 = new Task(1, "Name1", "Description1", TaskStatus.NEW);
        int id = task1.getId();
        String name = task1.getName();
        String description = task1.getDescription();
        TaskStatus taskStatus = task1.getStatus();
        inMemoryTaskManager.addTask(1, task1);
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
        inMemoryTaskManager.addEpic(1, epic1);
        Assertions.assertEquals(epic1.getSubtasksIds(), epic2.getSubtasksIds(), "Значения полей различаются");
        Assertions.assertEquals(epic2, epic1, "Значения полей различаются");
    }

    //тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    public void subtaskFieldsShouldBeSameAfterAddingToTaskManager() {
        Epic epic1 = new Epic("Name1", "Description1");
        inMemoryTaskManager.addEpic(1, epic1);
        Subtask subtask1 = new Subtask(2, 1, "Name2", "Description2", TaskStatus.NEW);
        Subtask subtask2 = new Subtask(subtask1);
        inMemoryTaskManager.addSubtask(2, 1, subtask1);
        Assertions.assertEquals(subtask2, subtask1, "Значения полей различаются");
        Assertions.assertEquals(subtask2.getEpicId(), subtask1.getEpicId(), "Значения полей различаются");

    }

}