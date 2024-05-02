package ru.yandex.practicum.tasktracker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.tasktracker.Task;
import ru.yandex.practicum.tasktracker.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

class TaskTest {

    //экземпляры класса Task равны друг другу, если равен их id;
    @Test
    public void tasksEqualsIfIdsEquals(){
        int id = 1;
        Task task1 = new Task(id,"Name1","Description1", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));
        Task task2 = new Task(id,"Name2","Description2", TaskStatus.NEW,
                LocalDateTime.now(), Duration.ofMinutes(30));

        Assertions.assertEquals(task1,task2,"Объекты не равны, но id одинаковы");
    }



}