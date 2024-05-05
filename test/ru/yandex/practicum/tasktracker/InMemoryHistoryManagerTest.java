package ru.yandex.practicum.tasktracker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryHistoryManagerTest {
    public static TaskManager inMemoryTaskManager;

    public static boolean taskFieldsEquals(Task task1, Task task2) {
        return (task1.getId() == task2.getId())
                && (task1.getName().equals(task2.getName()))
                && (task1.getDescription().equals(task2.getDescription()))
                && (task1.getStatus().equals(task2.getStatus()));
    }

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = Managers.getDefault();
    }

    //задачи, добавляемые в HistoryManager, сохраняют значения полей
    //HistoryManager сохраняет новый объект задачи на место старого с тем же Id

    @Test
    public void taskFieldsAreTheSameAfterSavingToHistory() {

        Task task1 = new Task(1, "Name1", "Description1", TaskStatus.NEW);
        inMemoryTaskManager.addTask(1, task1);

        inMemoryTaskManager.getTask(1);
        Assertions.assertEquals(task1.getId(), inMemoryTaskManager.getHistory().get(0).getId(),
                "значение поля Id объекта сохраненного в HistoryManager не совпадает с изначально заданными полем");
        Assertions.assertEquals(task1.getDescription(), inMemoryTaskManager.getHistory().get(0).getDescription(),
                "значение поля description объекта сохраненного в HistoryManager не совпадает с изначально заданными полем");
        Assertions.assertEquals(task1.getName(), inMemoryTaskManager.getHistory().get(0).getName(),
                "значение поля name объекта сохраненного в HistoryManager не совпадает с изначально заданными полем");
        Assertions.assertEquals(task1.getStatus(), inMemoryTaskManager.getHistory().get(0).getStatus(),
                "значение поля status объекта сохраненного в HistoryManager не совпадает с изначально заданными полем");
    }

    @Test
    public void taskFieldsAreNewAfterTaskUpdate() {
        Task task1 = new Task(1, "Name1", "Description1", TaskStatus.NEW);
        inMemoryTaskManager.addTask(1, task1);
        inMemoryTaskManager.getTask(1);

        Task task2 = new Task(1, "Name2", "Description2", TaskStatus.NEW);
        inMemoryTaskManager.updateTask(task2);
        inMemoryTaskManager.getTask(1);

        Assertions.assertEquals(task2.getId(), inMemoryTaskManager.getHistory().get(0).getId(),
                "значение поля Id объекта сохраненного в HistoryManager не равно полю обновленного объекта");
        Assertions.assertEquals(task2.getDescription(), inMemoryTaskManager.getHistory().get(0).getDescription(),
                "значение поля description объекта сохраненного в HistoryManager не равно полю обновленного объекта");
        Assertions.assertEquals(task2.getName(), inMemoryTaskManager.getHistory().get(0).getName(),
                "значение поля name объекта сохраненного в HistoryManager не равно полю обновленного объекта");
        Assertions.assertEquals(task2.getStatus(), inMemoryTaskManager.getHistory().get(0).getStatus(),
                "значение поля status объекта сохраненного в HistoryManager не равно полю обновленного объекта");
    }

    @Test
    public void historyListShouldHave0SizeAfterRemoveLastTaskFromIt() {
        Task task1 = new Task(1, "Name1", "Description1", TaskStatus.NEW);
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.removeTask(task1.getId());
        Assertions.assertEquals(0, inMemoryTaskManager.getHistory().size(), "Задача не удалена!");
    }

    @Test
    public void tasksShouldHaveSameFieldsAfterRemovingFromMiddleOfHistoryList() {
        Task task1 = new Task(1, "Name1", "Description1", TaskStatus.NEW);
        Task task2 = new Task(2, "Name2", "Description2", TaskStatus.NEW);
        Task task3 = new Task(3, "Name3", "Description3", TaskStatus.NEW);
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addTask(task3);

        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task2.getId());
        inMemoryTaskManager.getTask(task3.getId());

        inMemoryTaskManager.removeTask(task2.getId());

        Assertions.assertTrue(taskFieldsEquals(task1, inMemoryTaskManager.getHistory().get(0)), "Оставшиеся объекты не совпадают после удаления из середины списка");
        Assertions.assertTrue(taskFieldsEquals(task3, inMemoryTaskManager.getHistory().get(1)), "Оставшиеся Объекты не совпадают после удаления из середины списка");
    }

    @Test
    public void tasksShouldHaveSameFieldsAfterRemovingFromHistoryListTail() {
        Task task1 = new Task(1, "Name1", "Description1", TaskStatus.NEW);
        Task task2 = new Task(2, "Name2", "Description2", TaskStatus.NEW);
        Task task3 = new Task(3, "Name3", "Description3", TaskStatus.NEW);
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addTask(task3);

        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task2.getId());
        inMemoryTaskManager.getTask(task3.getId());

        inMemoryTaskManager.removeTask(task3.getId());

        Assertions.assertTrue(taskFieldsEquals(task1, inMemoryTaskManager.getHistory().get(0)), "Оставшиеся Объекты не совпадают после удаления головы списка");
        Assertions.assertTrue(taskFieldsEquals(task2, inMemoryTaskManager.getHistory().get(1)), "ОСтавшиеся Объекты не совпадают после удаления головы списка");
        Assertions.assertEquals(2, inMemoryTaskManager.getHistory().size(), "В списке должно быть две задачи");
    }

    @Test
    public void tasksShouldHaveSameFieldsAfterRemovingFromHistoryListHead() {
        Task task1 = new Task(1, "Name1", "Description1", TaskStatus.NEW);
        Task task2 = new Task(2, "Name2", "Description2", TaskStatus.NEW);
        Task task3 = new Task(3, "Name3", "Description3", TaskStatus.NEW);
        inMemoryTaskManager.addTask(task1);
        inMemoryTaskManager.addTask(task2);
        inMemoryTaskManager.addTask(task3);

        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getTask(task2.getId());
        inMemoryTaskManager.getTask(task3.getId());

        inMemoryTaskManager.removeTask(task1.getId());

        Assertions.assertTrue(taskFieldsEquals(task2, inMemoryTaskManager.getHistory().get(0)), "Оставшиеся Объекты не совпадают после удаления головы списка");
        Assertions.assertTrue(taskFieldsEquals(task3, inMemoryTaskManager.getHistory().get(1)), "ОСтавшиеся Объекты не совпадают после удаления головы списка");
        Assertions.assertEquals(2, inMemoryTaskManager.getHistory().size(), "В списке должно быть две задачи");
    }

    @Test
    public void taskShouldBeInHistoryListAfterAdd() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        Task task1 = new Task(1, "Name1", "Description1", TaskStatus.NEW);
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