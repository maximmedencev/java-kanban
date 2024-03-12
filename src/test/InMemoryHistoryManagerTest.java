package test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.Managers;
import ru.yandex.practicum.tasktracker.Task;
import ru.yandex.practicum.tasktracker.TaskManager;
import ru.yandex.practicum.tasktracker.TaskStatus;

class InMemoryHistoryManagerTest {
    public static TaskManager inMemoryTaskManager;

    @BeforeEach
    public void beforeEach() {
        inMemoryTaskManager = Managers.getDefault();
    }

    //задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
    @Test
    public void taskInHistoryShouldBeSameIfNoChangesAndNotToBeSameAsPreviousVersionIfChanged() {
        String description = "Description1";
        Task task1 = new Task(1, "Name", description, TaskStatus.NEW);
        inMemoryTaskManager.addTask(1, task1);
        inMemoryTaskManager.getTask(1);
        Assertions.assertEquals(description, inMemoryTaskManager.getHistory().get(0).getDescription(),
                "предыдущая версия объекта не соотвествет новому объекту, хотя изменений полей не было");
        task1.setDescription("New description");
        inMemoryTaskManager.getTask(1);
        Assertions.assertNotEquals(description, inMemoryTaskManager.getHistory().get(1).getDescription(),
                "Последний объект в истории совпадает с новым, хотя были изменения");
    }
}