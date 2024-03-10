package ru.yandex.practicum.tasktracker;

import java.util.List;

public interface HistoryManager {
    void add(Task task);//должен помечать задачи как просмотренные
    List<Task> getHistory(); // возвращать их список просмотренных задач
}
