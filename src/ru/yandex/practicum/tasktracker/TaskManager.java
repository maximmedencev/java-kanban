package ru.yandex.practicum.tasktracker;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    List<Task> getHistory();

    List<Task> getTasksList();

    List<Task> getEpicsList();

    List<Task> getSubtasksList();

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    void removeTask(int taskId);

    void removeEpic(int epicId);

    void removeSubtask(int SubtaskId);

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(int epicId, Subtask subtask);

    int addTask(int id, Task task);

    int addEpic(int id, Epic epic);

    int addSubtask(int id, int epicId, Subtask Subtask);
    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);
    List<Task> getEpicSubtaskList(int id);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask Subtask);

    void updateEpicStatus(Epic epic);


}
