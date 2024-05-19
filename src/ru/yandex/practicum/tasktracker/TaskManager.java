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

    void addTask(Task task) throws IntersectionException;

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask) throws IntersectionException;

    void addSubtask(int epicId, Subtask subtask) throws IntersectionException;

    int addTask(int id, Task task);

    int addEpic(int id, Epic epic);

    int addSubtask(int id, int epicId, Subtask subtask);

    Task getTask(int id) throws NotFoundException;

    Epic getEpic(int id) throws NotFoundException;

    Subtask getSubtask(int id) throws NotFoundException;

    List<Subtask> getEpicSubtaskList(int id);

    void updateTask(Task task) throws IntersectionException;

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask) throws IntersectionException;

    void updateEpicStatus(Epic epic);

    List<Task> getPrioritizedTasks();
}
