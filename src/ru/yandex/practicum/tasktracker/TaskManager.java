package ru.yandex.practicum.tasktracker;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    List<Task> getHistory();

    List<Task> getTasksList();

    List<Task> getEpicsList();

    List<Task> getSubTasksList();

    void removeAllTasks();

    void removeAllEpics();

    void removeAllSubTasks();

    void removeTask(int taskId);

    void removeEpic(int epicId);

    void removeSubtask(int subtaskId);

    void addTask(Task task);

    void addEpic(Epic epic);

    void addSubtask(int epicId, Subtask subtask);

    Task getTask(int id);

    Epic getEpic(int id);

    Subtask getSubTask(int id);
    ArrayList<Task> getEpicSubTaskList(int id);

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    void updateEpicStatus(Epic epic);


}
