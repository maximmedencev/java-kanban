package ru.yandex.practicum.tasktracker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

abstract class TaskManagerTest<T extends TaskManager> {
    T taskManager;

    //    List<Task> getHistory();
    @Test
    public void shouldReturnHistoryListWithSpecifiedTasks() {
        Task task1 = new Task(1, "Task1 name",
                "Task1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 12, 0, 0),
                Duration.ofMinutes(30));

        Task task2 = new Task(2, "Task2 name",
                "Task2 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 13, 0, 0),
                Duration.ofMinutes(30));

        Task task3 = new Task(3, "Task3 name",
                "Task3 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 14, 0, 0),
                Duration.ofMinutes(30));

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        taskManager.getTask(1);
        taskManager.getTask(2);
        taskManager.getTask(3);

        Assertions.assertEquals(3,
                taskManager.getHistory().size(),
                "Размер истории отличается от ожидаемого");
        Assertions.assertTrue(TaskTest.taskFieldsEquals(task1,
                taskManager.getHistory().get(0)),
                "Первый элемент истории не свопадеает с ожидаемым");
        Assertions.assertTrue(TaskTest.taskFieldsEquals(task2,
                taskManager.getHistory().get(1)),
                "Второй элемент истории не свопадеает с ожидаемым");
        Assertions.assertTrue(TaskTest.taskFieldsEquals(task3,
                taskManager.getHistory().get(2)),
                "Третий элемент истории не свопадеает с ожидаемым");

    }

    //    List<Task> getTasksList();
    @Test
    public void shouldReturnListWithSpecifiedTasks() {
        Task task1 = new Task(1, "Task1 name",
                "Task1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 12, 0, 0),
                Duration.ofMinutes(30));

        Task task2 = new Task(2, "Task2 name",
                "Task2 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 13, 0, 0),
                Duration.ofMinutes(30));

        Task task3 = new Task(3, "Task3 name",
                "Task3 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 14, 0, 0),
                Duration.ofMinutes(30));

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);

        Assertions.assertEquals(3, taskManager.getTasksList().size(),
                "Размер возвращаемого списка задач не совпадает с ожидаемым");
        Assertions.assertTrue(TaskTest.taskFieldsEquals(task1,
                taskManager.getTasksList().get(0)),
                "Первый элемент списка задач не совпадает с ожидаемым");
        Assertions.assertTrue(TaskTest.taskFieldsEquals(task2,
                taskManager.getTasksList().get(1)),
                "Второй элемент списка задач не совпадает с ожидаемым");
        Assertions.assertTrue(TaskTest.taskFieldsEquals(task3,
                taskManager.getTasksList().get(2)),
                "Третий элемент списка задач не совпадает с ожидаемым");
    }

    //    List<Task> getEpicsList();
    @Test
    public void shouldReturnListWithSpecifiedEpics() {

        Epic epic1 = new Epic("Epic1 name", "Epic1 description");
        Epic epic2 = new Epic("Epic2 name", "Epic2 description");
        Epic epic3 = new Epic("Epic3 name", "Epic3 description");

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);

        Assertions.assertEquals(3, taskManager.getEpicsList().size(),
                "Размер возвращаемого списка эпиков не совпадает с ожидаемым");
        Assertions.assertTrue(EpicTest.epicsFieldsEquals(epic1,
                taskManager.getEpicsList().get(0)),
                "Первый элемент списка эпиков не совпадает с ожидаемым");
        Assertions.assertTrue(EpicTest.epicsFieldsEquals(epic2,
                taskManager.getEpicsList().get(1)),
                "Второй элемент списка эпиков не совпадает с ожидаемым");
        Assertions.assertTrue(EpicTest.epicsFieldsEquals(epic3,
                taskManager.getEpicsList().get(2)),
                "Третий элемент списка эпиков не совпадает с ожидаемым");
    }

    //    List<Task> getSubtasksList();
    @Test
    public void shouldReturnListWithSpecifiedSubtasks() {

        Epic epic1 = new Epic("Epic1 name", "Epic1 description");

        Subtask subtask1 = new Subtask("Subtask1 name",
                "Subtask1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 13, 0, 0),
                Duration.ofMinutes(30));


        Subtask subtask2 = new Subtask("Subtask2 name",
                "Subtask2 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 14, 0, 0),
                Duration.ofMinutes(30));

        taskManager.addEpic(epic1);
        taskManager.addSubtask(1, subtask1);
        taskManager.addSubtask(1, subtask2);

        Assertions.assertEquals(
                2,
                taskManager.getSubtasksList().size(),
                "Размер возвращаемого списка подзадач не совпадает с ожидаемым");
        Assertions.assertTrue(
                SubtaskTest.subtasksFieldsEquals(subtask1,
                taskManager.getSubtasksList().get(0)),
                "Первый элемент списка подзадач не совпадает с ожидаемым");
        Assertions.assertTrue(
                SubtaskTest.subtasksFieldsEquals(subtask2,
                taskManager.getSubtasksList().get(1)),
                "Второй элемент списка подзадач не совпадает с ожидаемым");
    }

    //    void removeAllTasks();
    @Test
    public void shouldHaveZeroSizeIfAllTasksRemoved() {
        Task task1 = new Task(1, "Task1 name",
                "Task1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 12, 0, 0),
                Duration.ofMinutes(30));

        Task task2 = new Task(2, "Task2 name",
                "Task2 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 13, 0, 0),
                Duration.ofMinutes(30));

        Task task3 = new Task(3, "Task3 name",
                "Task3 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 14, 0, 0),
                Duration.ofMinutes(30));

        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        taskManager.removeAllTasks();

        Assertions.assertEquals(
                0,
                taskManager.getTasksList().size(),
                "Список задач не пуст после очистки");

    }

    //    void removeAllEpics();
    @Test
    public void shouldHaveZeroSizeIfAllEpicsRemoved() {
        Epic epic1 = new Epic("Epic1 name", "Epic1 description");
        Epic epic2 = new Epic("Epic2 name", "Epic2 description");
        Epic epic3 = new Epic("Epic3 name", "Epic3 description");

        taskManager.addEpic(epic1);
        taskManager.addEpic(epic2);
        taskManager.addEpic(epic3);

        taskManager.removeAllEpics();

        Assertions.assertEquals(
                0,
                taskManager.getEpicsList().size(),
                "Список эпиков не пуст после очистки");
    }

    //    void removeAllSubtasks();
    @Test
    public void shouldHaveZeroSizeIfAllSubtasksRemoved() {
        Epic epic1 = new Epic("Epic1 name", "Epic1 description");
        Subtask subtask1 = new Subtask("Subtask1 name",
                "Subtask1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 13, 0, 0),
                Duration.ofMinutes(30));


        Subtask subtask2 = new Subtask("Subtask2 name",
                "Subtask2 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 14, 0, 0),
                Duration.ofMinutes(30));

        taskManager.addEpic(epic1);
        taskManager.addSubtask(1, subtask1);
        taskManager.addSubtask(1, subtask2);

        taskManager.removeAllSubtasks();
        Assertions.assertEquals(
                0,
                taskManager.getSubtasksList().size(),
                "Список подзадач не пуст после очистки");
    }

    //    void removeTask(int taskId);
    @Test
    public void shouldRemoveTaskWithSpecifiedId() {
        Task task1 = new Task(1, "Task1 name",
                "Task1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 12, 0, 0),
                Duration.ofMinutes(30));

        Task task2 = new Task(2, "Task2 name",
                "Task2 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 13, 0, 0),
                Duration.ofMinutes(30));

        Task task3 = new Task(3, "Task3 name",
                "Task3 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 14, 0, 0),
                Duration.ofMinutes(30));

        taskManager.addTask(1, task1);
        taskManager.addTask(2, task2);
        taskManager.addTask(3, task3);

        taskManager.removeTask(1);

        Assertions.assertFalse(
                taskManager.getTasksList().contains(task1),
                "Задача не удалена");
    }

    //    void removeEpic(int epicId);
    @Test
    public void shouldRemoveEpicWithSpecifiedId() {
        Epic epic1 = new Epic("Epic1 name", "Epic1 description");
        Epic epic2 = new Epic("Epic2 name", "Epic2 description");
        Epic epic3 = new Epic("Epic3 name", "Epic3 description");

        taskManager.addEpic(1, epic1);
        taskManager.addEpic(2, epic2);
        taskManager.addEpic(3, epic3);

        taskManager.removeEpic(1);

        Assertions.assertFalse(
                taskManager.getTasksList().contains(epic1),
                "Эпик не был удален");
    }

    //    void removeSubtask(int subtaskId);
    @Test
    public void shouldRemoveSubtaskWithSpecifiedId() {
        Epic epic1 = new Epic("Epic1 name", "Epic1 description");
        Subtask subtask1 = new Subtask("Subtask1 name",
                "Subtask1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 13, 0, 0),
                Duration.ofMinutes(30));


        Subtask subtask2 = new Subtask("Subtask2 name",
                "Subtask2 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 14, 0, 0),
                Duration.ofMinutes(30));

        taskManager.addEpic(epic1);
        taskManager.addSubtask(2, 1, subtask1);
        taskManager.addSubtask(3, 1, subtask2);
        taskManager.removeSubtask(2);

        Assertions.assertFalse(
                taskManager.getSubtasksList().contains(subtask1),
                "Подзадача не была удалена");
    }

    //    void addTask(Task task);
    @Test
    public void shouldHaveSpecifiedTaskAfterAdd() {
        Task task1 = new Task(1, "Task1 name",
                "Task1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 12, 0, 0),
                Duration.ofMinutes(30));
        taskManager.addTask(task1);
        Assertions.assertTrue(
                taskManager.getTasksList().contains(task1),
                "Добавленная задача не найдена");
    }

    //    void addEpic(Epic epic);
    @Test
    public void shouldHaveSpecifiedEpicAfterAdd() {
        Epic epic1 = new Epic("Epic1 name", "Epic1 description");
        taskManager.addEpic(1, epic1);
        Assertions.assertTrue(taskManager.getEpicsList().contains(epic1),
                "Добавленный эпик не найден");
    }

    //    void addSubtask(int epicId, Subtask subtask);
    @Test
    public void shouldHaveSpecifiedSubtaskAfterAdd() {
        Epic epic1 = new Epic("Epic1 name", "Epic1 description");
        Subtask subtask1 = new Subtask("Subtask1 name",
                "Subtask1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 13, 0, 0),
                Duration.ofMinutes(30));
        taskManager.addEpic(epic1);
        taskManager.addSubtask(1, subtask1);

        Assertions.assertTrue(taskManager.getSubtasksList().contains(subtask1),
                "Добавленная подзадача не найдена");
    }

    //    int addTask(int id, Task task);
    @Test
    public void shouldHaveTaskWithSpecifiedIdAfterAdd() {
        Task task1 = new Task(1, "Task1 name",
                "Task1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 12, 0, 0),
                Duration.ofMinutes(30));
        taskManager.addTask(1, task1);
        Assertions.assertEquals(task1, taskManager.getTask(1).get(),"Добавленная подзадача не найдена");
        Assertions.assertTrue(TaskTest.taskFieldsEquals(task1,
                taskManager.getTask(1).get()),
                "Поля добавленной и запрошенной задачи не совпадают");
    }

    //    int addEpic(int id, Epic epic);
    @Test
    public void shouldHaveEpicWithSpecifiedIdAfterAdd() {
        Epic epic1 = new Epic("Epic1 name", "Epic1 description");
        taskManager.addEpic(1, epic1);
        Assertions.assertEquals(epic1, taskManager.getEpic(1).get(),"Добавленный эпик не найден");
        Assertions.assertTrue(EpicTest.epicsFieldsEquals(epic1, taskManager.getEpic(1).get()),
                "Поля добавленного и запрошенного эпика не совпадают");
    }

    //    int addSubtask(int id, int epicId, Subtask subtask);
    //задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера
    @Test
    public void shouldReturnMinus1IfSubtasksIdsConflict() {
        Subtask subtask1 = new Subtask("Name1",
                "Description1",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 12, 0, 0),
                Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask("Name2",
                "Description2",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 13, 0, 0),
                Duration.ofMinutes(30));
        Epic epic1 = new Epic("Name1", "Description1");
        taskManager.addEpic(epic1);
        taskManager.addSubtask(epic1.getId(), subtask1);
        Assertions.assertEquals(-1, taskManager.addSubtask(subtask1.getId(), epic1.getId(), subtask2),
                "конфликт id не обнаружен");
    }

    @Test
    public void shouldReturnMinus2WhenNull() {
        Subtask subtask1 = new Subtask("Name1",
                "Description1",
                TaskStatus.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(30));
        Epic epic1 = new Epic("Name1", "Description1");
        taskManager.addEpic(epic1);
        taskManager.addSubtask(epic1.getId(), subtask1);

        Assertions.assertEquals(-2, taskManager.addSubtask(subtask1.getId(), epic1.getId(), null),
                "метод возвращает не -2, но передан null");

    }

    @Test
    public void shouldReturnMinus3WhenSubtasksIntersects() {
        Subtask subtask1 = new Subtask(1,
                "Name1",
                "Description1",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 12, 0, 0),
                Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(2,
                "Name2",
                "Description2",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 12, 10, 0),
                Duration.ofMinutes(30));
        Epic epic1 = new Epic("Name1", "Description1");
        taskManager.addEpic(3, epic1);
        taskManager.addSubtask(epic1.getId(), subtask1);

        Assertions.assertEquals(-3, taskManager.addSubtask(subtask2.getId(), 3, subtask2),
                "подзадачи пересекаются, но метод не возвращает -3");
    }

    //    Task getTask(int id);
    @Test
    public void shouldGetTaskWithSpecifiedId() {
        Task task1 = new Task(1, "Task1 name",
                "Task1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 12, 0, 0),
                Duration.ofMinutes(30));

        taskManager.addTask(1, task1);
        Assertions.assertEquals(task1, taskManager.getTask(1).get(),
                "У запрошенной и добавленной задачи не совпадают id");
        Assertions.assertTrue(TaskTest.taskFieldsEquals(
                task1,
                taskManager.getTask(1).get()),
                "Поля запрошенной и ожидаемой задачи не совпадапют");
    }

    //    Epic getEpic(int id);
    @Test
    public void shouldGetEpicWithSpecifiedId() {
        Epic epic1 = new Epic("Epic1 name", "Epic1 description");
        taskManager.addEpic(1, epic1);
        Assertions.assertEquals(epic1,
                taskManager.getEpic(1).get(),
                "У запрошенного и добавленного эпика не совпадают id");
        Assertions.assertTrue(EpicTest.epicsFieldsEquals(epic1,taskManager.getEpic(1).get()),
                "Поля запрошенного и ожидаемого эпиков не совпадапют");
    }

    //    Subtask getSubtask(int id);
    @Test
    public void shouldGetSubtaskWithSpecifiedId() {
        Epic epic1 = new Epic("Epic1 name", "Epic1 description");
        Subtask subtask1 = new Subtask(1,
                "Name1",
                "Description1",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 12, 0, 0),
                Duration.ofMinutes(30));
        taskManager.addEpic(1, epic1);
        taskManager.addSubtask(1, subtask1);
        Assertions.assertEquals(subtask1, taskManager.getSubtask(1).get());
        Assertions.assertTrue(SubtaskTest.subtasksFieldsEquals(subtask1,taskManager.getSubtask(1).get()),
                "Поля запрошенной и ожидаемой подзадачи не совпадапют");
    }

    //    List<Task> getEpicSubtaskList(int id);
    @Test
    public void shouldReturnArrayListWithSpecifiedIds() {
        Subtask subtask1 = new Subtask(1,
                "Name1",
                "Description1",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 12, 0, 0),
                Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(2,
                "Name2",
                "Description2",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 12, 10, 0),
                Duration.ofMinutes(30));
        Epic epic1 = new Epic("Name1", "Description1");
        taskManager.addEpic(3, epic1);
        taskManager.addSubtask(1, epic1.getId(), subtask1);
        taskManager.addSubtask(2, epic1.getId(), subtask1);
        Assertions.assertEquals(2,
                taskManager.getEpicSubtaskList(3).size(),
                "Ожидаемый и полученный размер списка эпиков не совпадают");
        Assertions.assertTrue(taskManager.getEpicSubtaskList(3).contains(subtask1),
                "Подзадача №1 не найдена списке подзадач");
        Assertions.assertTrue(taskManager.getEpicSubtaskList(3).
                contains(subtask2),
                "Подзадача №2 не найдена списке подзадач");
    }

    //    void updateTask(Task task);
    @Test
    public void shouldReturnNewTaskAfterUpdate() {
        Task task1 = new Task(1, "Task1 name",
                "Task1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 12, 0, 0),
                Duration.ofMinutes(30));

        Task task2 = new Task(1, "Task2 name",
                "Task2 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 13, 0, 0),
                Duration.ofMinutes(30));

        taskManager.addTask(task1);
        taskManager.updateTask(task2);

        Assertions.assertFalse(TaskTest.taskFieldsEquals(task1,
                taskManager.getTask(1).get()),
                "После обновления задача осталась прежней");
        Assertions.assertTrue(TaskTest.taskFieldsEquals(task2,
                taskManager.getTask(1).get()),
                "Задача не равена обновленной");
    }

    //    void updateEpic(Epic epic);
    @Test
    public void shouldReturnNewEpicAfterUpdate() {
        Epic epic1 = new Epic(1, "Epic1 name", "Epic1 description");
        Epic epic2 = new Epic(1, "Epic2 name", "Epic2 description");

        taskManager.addEpic(epic1);
        taskManager.updateEpic(epic2);

        Assertions.assertFalse(EpicTest.epicsFieldsEquals(
                epic1,
                taskManager.getEpic(1).get()),
                "После обновления эпик остался прежним");
        Assertions.assertTrue(EpicTest.epicsFieldsEquals(
                epic2,
                taskManager.getEpic(1).get()),
                "Поля запрошенногои нового эпика не свопадают");

    }

    //    void updateSubtask(Subtask subtask);
    @Test
    public void shouldReturnNewSubtaskAfterUpdate() {
        Subtask subtask1 = new Subtask(2,
                "Name1",
                "Description1",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 12, 0, 0),
                Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(2,
                "Name2",
                "Description2",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 13, 0, 0),
                Duration.ofMinutes(30));
        Epic epic1 = new Epic("Name1", "Description1");

        taskManager.addEpic(1, epic1);
        taskManager.addSubtask(2, 1, subtask1);

        taskManager.updateSubtask(subtask2);

        Assertions.assertFalse(SubtaskTest.subtasksFieldsEquals(
                subtask1, taskManager.getSubtask(2).get()),
                "После обновления подзадача не изменилась");
        Assertions.assertTrue(SubtaskTest.subtasksFieldsEquals(subtask2,
                taskManager.getSubtask(2).get()),
                "Поля запрошенной и новой подзадачи не свопадают");
    }

    //    void updateEpicStatus(Epic epic);
    @Test
    public void epicStatusShouldBeNewIfAllSubtaskStatusesAreNew() {
        Epic epic1 = new Epic("Name1", "Description1");
        taskManager.addEpic(1, epic1);
        Subtask subtask1 = new Subtask(2, 1, "Name2", "Description2", TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 12, 0, 0), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(3, 1, "Name3", "Description3", TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 13, 0, 0), Duration.ofMinutes(30));
        Subtask subtask3 = new Subtask(4, 1, "Name4", "Description4", TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 14, 0, 0), Duration.ofMinutes(30));

        taskManager.addSubtask(subtask1.getId(), 1, subtask1);
        taskManager.addSubtask(subtask2.getId(), 1, subtask2);
        taskManager.addSubtask(subtask3.getId(), 1, subtask3);

        Assertions.assertEquals(TaskStatus.NEW, epic1.getStatus(), "Статус эпика не равен NEW");
    }

    @Test
    public void epicStatusShouldBeDoneIfAllSubtaskStatusesAreDone() {
        Epic epic1 = new Epic("Name1", "Description1");
        taskManager.addEpic(1, epic1);
        Subtask subtask1 = new Subtask(2, 1, "Name2", "Description2", TaskStatus.DONE,
                LocalDateTime.of(2024, 5, 2, 12, 0, 0), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(3, 1, "Name3", "Description3", TaskStatus.DONE,
                LocalDateTime.of(2024, 5, 2, 13, 0, 0), Duration.ofMinutes(30));
        Subtask subtask3 = new Subtask(4, 1, "Name4", "Description4", TaskStatus.DONE,
                LocalDateTime.of(2024, 5, 2, 14, 0, 0), Duration.ofMinutes(30));

        taskManager.addSubtask(subtask1.getId(), 1, subtask1);
        taskManager.addSubtask(subtask2.getId(), 1, subtask2);
        taskManager.addSubtask(subtask3.getId(), 1, subtask3);

        Assertions.assertEquals(TaskStatus.DONE, epic1.getStatus(), "Статус эпика не равен DONE");
    }

    @Test
    public void epicStatusShouldBeInProgressIfAllSubtaskStatusesAreNewAndDone() {
        Epic epic1 = new Epic("Name1", "Description1");
        taskManager.addEpic(1, epic1);
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
                TaskStatus.DONE,
                LocalDateTime.of(2024, 5, 2, 14, 0, 0),
                Duration.ofMinutes(30));

        taskManager.addSubtask(subtask1.getId(), 1, subtask1);
        taskManager.addSubtask(subtask2.getId(), 1, subtask2);
        taskManager.addSubtask(subtask3.getId(), 1, subtask3);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus(), "Статус эпика не равен DONE");
    }

    @Test
    public void epicStatusShouldBeInProgressIfAllSubtaskStatusesAreInProgress() {
        Epic epic1 = new Epic("Name1", "Description1");
        taskManager.addEpic(1, epic1);
        Subtask subtask1 = new Subtask(2,
                1,
                "Name2",
                "Description2",
                TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2024, 5, 2, 12, 0, 0),
                Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(3,
                1,
                "Name3",
                "Description3",
                TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2024, 5, 2, 13, 0, 0),
                Duration.ofMinutes(30));
        Subtask subtask3 = new Subtask(4,
                1,
                "Name4",
                "Description4",
                TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2024, 5, 2, 14, 0, 0),
                Duration.ofMinutes(30));

        taskManager.addSubtask(subtask1.getId(), 1, subtask1);
        taskManager.addSubtask(subtask2.getId(), 1, subtask2);
        taskManager.addSubtask(subtask3.getId(), 1, subtask3);

        Assertions.assertEquals(TaskStatus.IN_PROGRESS, epic1.getStatus(), "Статус эпика не равен DONE");
    }

    //
//    List<Task> getPrioritizedTasks();
    @Test
    public void shouldReturnPrioritizedTasks() {
        Epic epic1 = new Epic("Name1", "Description1");
        Subtask subtask1 = new Subtask(2,
                1,
                "Name2",
                "Description2",
                TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2024, 5, 2, 12, 0, 0),
                Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(3,
                1,
                "Name3",
                "Description3",
                TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2024, 5, 2, 13, 0, 0),
                Duration.ofMinutes(30));
        Subtask subtask3 = new Subtask(4,
                1,
                "Name4",
                "Description4",
                TaskStatus.IN_PROGRESS,
                LocalDateTime.of(2024, 5, 2, 14, 0, 0),
                Duration.ofMinutes(30));

        Task task1 = new Task(5, "Task1 name",
                "Task1 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 15, 0, 0),
                Duration.ofMinutes(30));

        Task task2 = new Task(6, "Task2 name",
                "Task2 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 16, 0, 0),
                Duration.ofMinutes(30));

        Task task3 = new Task(7, "Task3 name",
                "Task3 description",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 17, 0, 0),
                Duration.ofMinutes(30));

        taskManager.addEpic(1, epic1);

        taskManager.addSubtask(2, 1, subtask1);
        taskManager.addSubtask(3, 1, subtask2);
        taskManager.addSubtask(4, 1, subtask3);

        taskManager.addTask(5, task1);
        taskManager.addTask(6, task2);
        taskManager.addTask(7, task3);

        Assertions.assertEquals(subtask1,
                taskManager.getPrioritizedTasks().get(0),
                "Порядок задач в приоретезированном списке не соответствует времени старта задач");
        Assertions.assertEquals(subtask2,
                taskManager.getPrioritizedTasks().get(1),
                "Порядок задач в приоретезированном списке не соответствует времени старта задач");
        Assertions.assertEquals(subtask3,
                taskManager.getPrioritizedTasks().get(2),
                "Порядок задач в приоретезированном списке не соответствует времени старта задач");
        Assertions.assertEquals(task1,
                taskManager.getPrioritizedTasks().get(3),
                "Порядок задач в приоретезированном списке не соответствует времени старта задач");
        Assertions.assertEquals(task2,
                taskManager.getPrioritizedTasks().get(4),
                "Порядок задач в приоретезированном списке не соответствует времени старта задач");
        Assertions.assertEquals(task3,
                taskManager.getPrioritizedTasks().get(5),
                "Порядок задач в приоретезированном списке не соответствует времени старта задач");
    }
}
