package ru.yandex.practicum.tasktracker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.Managers;

class ManagersTest {
    //утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров
    @Test
    public void inMemoryTaskManagerShouldNotBeNull() {
        Assertions.assertNotNull(Managers.getDefault(), "Возвращен null");
    }

    //утилитарный класс всегда возвращает проинициализированные и готовые к работе экземпляры менеджеров
    @Test
    public void InMemoryHistoryManagerShouldNotBeNull() {
        Assertions.assertNotNull(Managers.getDefaultHistory(), "Возвращен null");
    }
}