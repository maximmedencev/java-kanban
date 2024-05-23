package ru.yandex.practicum.tasktracker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.Epic;

public class EpicTest {

    public static boolean epicsFieldsEquals(Epic epic1, Task epic2) {
        return (epic1.getId() == epic2.getId())
                && (epic1.getName().equals(epic2.getName()))
                && (epic1.getDescription().equals(epic2.getDescription()));
    }

    //наследники класса Task равны друг другу, если равен их id
    @Test
    public void epicsEqualsIfIdsEquals() {
        Integer id = 1;
        Epic epic1 = new Epic(id, "Name1", "Description1");
        Epic epic2 = new Epic(id, "Name2", "Description2");

        Assertions.assertEquals(epic1, epic2, "Объекты не равны, но id одинаковы");
    }

    //объект Epic нельзя добавить в самого себя в виде подзадачи;
    @Test
    public void shouldBeMinus1IfSetEpicItselfSubtask() {
        Epic epic1 = new Epic(1, "Name1", "Description1");
        Assertions.assertEquals(-1, epic1.addSubtaskId(epic1.getId()),
                "Эпик добавлен как своя подзадача");
    }

}