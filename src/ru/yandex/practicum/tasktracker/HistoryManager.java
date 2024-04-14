package ru.yandex.practicum.tasktracker;

import java.util.List;

public interface HistoryManager {
    void add(Task task);//должен помечать задачи как просмотренные

    void remove(int id);

    List<Task> getHistory(); // возвращать список просмотренных задач

}
