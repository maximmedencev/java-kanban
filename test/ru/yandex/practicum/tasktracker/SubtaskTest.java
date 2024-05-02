package ru.yandex.practicum.tasktracker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.Subtask;
import ru.yandex.practicum.tasktracker.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

class SubtaskTest {


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