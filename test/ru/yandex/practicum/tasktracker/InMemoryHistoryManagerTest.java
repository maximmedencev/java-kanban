package ru.yandex.practicum.tasktracker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

class InMemoryHistoryManagerTest {
    public static TaskManager inMemoryTaskManager;


    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = Managers.getDefault();
    }

    @Test
    public void shouldReturnEmptyHistory() {
        Assertions.assertEquals(0, inMemoryTaskManager.getHistory().size());
    }

    @Test
    public void shouldHaveOneTaskAfterDuplicating() {
        Task task1 = new Task(1,
                "Name1",
                "Description1",
                TaskStatus.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(30));

        inMemoryTaskManager.addTask(task1);

        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task1.getId());
        Assertions.assertEquals(1, inMemoryTaskManager.getHistory().size());
        Assertions.assertTrue(TaskTest.taskFieldsEquals(task1, inMemoryTaskManager.getHistory().get(0)));
    }


    //задачи, добавляемые в HistoryManager, сохраняют значения полей
    //HistoryManager сохраняет новый объект задачи на место старого с тем же Id

    @Test
    public void taskFieldsAreTheSameAfterSavingToHistory() {

        Task task1 = new Task(1, "Name1", "Description1", TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(30));
        inMemoryTaskManager.addTask(1, task1);

        inMemoryTaskManager.getTask(1);
        Assertions.assertEquals(task1.getId(),
                inMemoryTaskManager.getHistory().get(0).getId(),
                "значение поля Id объекта сохраненного в HistoryManager не совпадает с изначально заданными полем");
        Assertions.assertEquals(task1.getDescription(),
                inMemoryTaskManager.getHistory().get(0).getDescription(),
                "значение поля description объекта сохраненного в HistoryManager не совпадает с изначально заданными полем");
        Assertions.assertEquals(task1.getName(),
                inMemoryTaskManager.getHistory().get(0).getName(),
                "значение поля name объекта сохраненного в HistoryManager не совпадает с изначально заданными полем");
        Assertions.assertEquals(task1.getStatus(),
                inMemoryTaskManager.getHistory().get(0).getStatus(),
                "значение поля status объекта сохраненного в HistoryManager не совпадает с изначально заданными полем");
    }

    @Test
    public void historyListShouldHave0SizeAfterRemoveLastTaskFromIt() {
        Task task1 = new Task(1,
                "Name1",
                "Description1",
                TaskStatus.NEW, LocalDateTime.now(),
                Duration.ofMinutes(30));
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.removeTask(task1.getId());
        Assertions.assertEquals(0,
                inMemoryTaskManager.getHistory().size(),
                "Задача не удалена!");
    }

    @Test
    public void tasksShouldHaveSameFieldsAfterRemovingFromMiddleOfHistoryList() {
        Task task1 = new Task(1,
                "Name1",
                "Description1",
                TaskStatus.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(30));
        Task task2 = new Task(2,
                "Name2",
                "Description2",
                TaskStatus.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(30));
        Task task3 = new Task(3,
                "Name3",
                "Description3",
                TaskStatus.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(30));
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addTask(task3);

        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task2.getId());
        inMemoryTaskManager.getTask(task3.getId());

        inMemoryTaskManager.removeTask(task2.getId());

        Assertions.assertTrue(TaskTest.taskFieldsEquals(task1, inMemoryTaskManager.getHistory().get(0)), "Оставшиеся объекты не совпадают после удаления из середины списка");
        Assertions.assertTrue(TaskTest.taskFieldsEquals(task3, inMemoryTaskManager.getHistory().get(1)), "Оставшиеся Объекты не совпадают после удаления из середины списка");
    }

    @Test
    public void tasksShouldHaveSameFieldsAfterRemovingFromHistoryListTail() {
        Task task1 = new Task(1,
                "Name1",
                "Description1",
                TaskStatus.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(30));
        Task task2 = new Task(2,
                "Name2",
                "Description2",
                TaskStatus.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(30));
        Task task3 = new Task(3,
                "Name3",
                "Description3",
                TaskStatus.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(30));
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addTask(task3);

        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task2.getId());
        inMemoryTaskManager.getTask(task3.getId());

        inMemoryTaskManager.removeTask(task3.getId());

        Assertions.assertTrue(TaskTest.taskFieldsEquals(task1, inMemoryTaskManager.getHistory().get(0)), "Оставшиеся Объекты не совпадают после удаления головы списка");
        Assertions.assertTrue(TaskTest.taskFieldsEquals(task2, inMemoryTaskManager.getHistory().get(1)), "ОСтавшиеся Объекты не совпадают после удаления головы списка");
        Assertions.assertEquals(2, inMemoryTaskManager.getHistory().size(), "В списке должно быть две задачи");
    }

    @Test
    public void tasksShouldHaveSameFieldsAfterRemovingFromHistoryListHead() {
        Task task1 = new Task(1,
                "Name1",
                "Description1",
                TaskStatus.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(30));
        Task task2 = new Task(2,
                "Name2",
                "Description2",
                TaskStatus.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(30));
        Task task3 = new Task(3,
                "Name3",
                "Description3",
                TaskStatus.NEW,
                LocalDateTime.now(),
                Duration.ofMinutes(30));
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addTask(task3);

        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task2.getId());
        inMemoryTaskManager.getTask(task3.getId());

        inMemoryTaskManager.removeTask(task1.getId());

        Assertions.assertTrue(TaskTest.taskFieldsEquals(task2, inMemoryTaskManager.getHistory().get(0)), "Оставшиеся Объекты не совпадают после удаления головы списка");
        Assertions.assertTrue(TaskTest.taskFieldsEquals(task3, inMemoryTaskManager.getHistory().get(1)), "ОСтавшиеся Объекты не совпадают после удаления головы списка");
        Assertions.assertEquals(2, inMemoryTaskManager.getHistory().size(), "В списке должно быть две задачи");
    }

    @Test
    public void taskShouldBeInHistoryListAfterAdd() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task(1, "Name1", "Description1", TaskStatus.NEW, LocalDateTime.now(), Duration.ofMinutes(30));
        historyManager.add(task1);

        Assertions.assertEquals(task1.getId(), historyManager.getHistory().get(0).getId(),
                "объекты не совпадают");
        Assertions.assertEquals(task1.getDescription(), historyManager.getHistory().get(0).getDescription(),
                "объекты не совпадают");
        Assertions.assertEquals(task1.getName(), historyManager.getHistory().get(0).getName(),
                "объекты не совпадают");
        Assertions.assertEquals(task1.getStatus(), historyManager.getHistory().get(0).getStatus(),
                "объекты не совпадают");
    }
}