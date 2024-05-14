package ru.yandex.practicum.tasktracker;

import java.util.List;
import java.util.Optional;

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

    void addSubtask(int epicId, Subtask subtask);

    int addTask(int id, Task task);

    int addEpic(int id, Epic epic);

    int addSubtask(int id, int epicId, Subtask subtask);

    Optional<Task> getTask(int id);

    Optional<Epic> getEpic(int id);

    Optional<Subtask> getSubtask(int id);

    List<Task> getEpicSubtaskList(int id);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void updateEpicStatus(Epic epic);

    List<Task> getPrioritizedTasks();
}
