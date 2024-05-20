package ru.yandex.practicum.tasktracker;

import java.util.List;

public interface TaskManager {
    List<Task> getHistory();

    List<Task> getTasksList();

    List<Epic> getEpicsList();

    List<Subtask> getSubtasksList();

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubtasks();

    void removeTask(int taskId);

    void removeEpic(int epicId);

    void removeSubtask(int subtaskId);

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask);

    void addSubtask(int epicId, Subtask subtask);

    int addTask(int id, Task task);

    int addEpic(int id, Epic epic);

    int addSubtask(int id, int epicId, Subtask subtask);

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubtask(int id);

    List<Subtask> getEpicSubtaskList(int id);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void updateEpicStatus(Epic epic);

    List<Task> getPrioritizedTasks();
}
