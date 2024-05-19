package ru.yandex.practicum.tasktracker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.Subtask;
import ru.yandex.practicum.tasktracker.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubtaskTest {

    public static boolean subtasksFieldsEquals(Subtask subtask1, Subtask subtask2) {
        return (subtask1.getId() == subtask2.getId())
                && (subtask1.getEpicId() == subtask2.getEpicId())
                && (subtask1.getName().equals(subtask2.getName()))
                && (subtask1.getDescription().equals(subtask2.getDescription()))
                && (subtask1.getStatus().equals(subtask2.getStatus()))
                && (subtask1.getStartTime().equals(subtask2.getStartTime()))
                && (subtask1.getDuration().equals(subtask2.getDuration()));
    }

    //наследники класса Task равны друг другу, если равен их id
    @Test
    public void tasksEqualsIfIdsEquals() {
        int id = 1;
        Subtask subtask1 = new Subtask(id, "Name1", "Description1", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        Subtask subtask2 = new Subtask(id, "Name2", "Description2", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));

        Assertions.assertEquals(subtask1, subtask2, "Объекты не равны, но id одинаковы");
    }

    //объект Subtask нельзя сделать своим же эпиком
    @Test
    public void shouldBeMinus1IfSetSubtaskItselfEpic() {
        Subtask subtask1 = new Subtask(1, "Subtask name", "Description", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        Assertions.assertEquals(-1, subtask1.setEpicId(subtask1.getId()),
                "Subtask является своим же эпиком");
    }

}