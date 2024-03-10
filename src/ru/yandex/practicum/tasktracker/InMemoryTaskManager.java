package ru.yandex.practicum.tasktracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, Subtask> subtasks;

    public HistoryManager historyManager = Managers.getDefaultHistory();

    private int newTaskId = 1;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    @Override
    public List<Task> getHistory() {
        return this.historyManager.getHistory();
    }

    @Override
    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(this.tasks.values());
    }

    @Override
    public ArrayList<Task> getEpicsList() {
        return new ArrayList<>(this.epics.values());
    }

    @Override
    public ArrayList<Task> getSubTasksList() {
        return new ArrayList<>(this.subtasks.values());
    }


    @Override
    public ArrayList<Task> getEpicSubTaskList(int id) {
        ArrayList<Task> returnArrayList = new ArrayList<>();

        for (Subtask subtask : this.subtasks.values()) {
            if (subtask.getEpicId() == id) {
                returnArrayList.add(subtask);
            }
        }


        return returnArrayList;
    }


    @Override
    public void removeAllTasks() {
        this.tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        this.epics.clear();
        this.removeAllSubTasks();
    }

    @Override
    public void removeAllSubTasks() {
        this.subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeAllSubtasksIds();
            updateEpicStatus(epic);
        }
    }

    @Override
    public void removeTask(int taskId) {
        tasks.remove(taskId);
    }

    @Override
    public void removeEpic(int epicId) {
        for (int subtaskId : epics.get(epicId).getSubtasksIds()) {
            subtasks.remove(subtaskId);
        }
        epics.remove(epicId);
    }

    @Override
    public void removeSubtask(int subtaskId) {
        int epicId = subtasks.get(subtaskId).getEpicId();
        subtasks.remove(subtaskId);
        epics.get(epicId).removeSubtask(subtaskId);
        this.updateEpicStatus(epics.get(epicId));
    }

    @Override
    public void addTask(Task task) {
        setIdForNewTask(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        setIdForNewTask(epic);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubtask(int epicId, Subtask subtask) {
        setIdForNewTask(subtask);
        epics.get(epicId).addSubtaskId(subtask.getId());
        subtask.setEpicId(epicId);
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epics.get(epicId));
    }

    @Override
    public Task getTask(int id) {
        historyManager.add(tasks.get(id));
        return this.tasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.add(epics.get(id));
        return this.epics.get(id);
    }

    @Override
    public Subtask getSubTask(int id) {
        historyManager.add(subtasks.get(id));
        return this.subtasks.get(id);
    }


    private void setIdForNewTask(Task task) {
        task.setId(newTaskId++);
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId()))
            tasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId()))
            epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {// проверяем есть ли в хеш-таблицах subtask с таким id
            //по id переданного task находим id epic содержащего subtask с таким же id
            int epicId = subtasks.get(subtask.getId()).getEpicId();

            if (epics.get(epicId).getSubtasksIds().contains(subtask.getId())) {// если в найденом epic есть такой subtask
                //привязываем по id переданный subtask к эпику старого subtask'a
                subtask.setEpicId(epicId);
                epics.get(epicId).removeSubtask(subtask.getId()); //удаляем старый subtask из epic
                epics.get(epicId).addSubtaskId(subtask.getId());  //добавляем в epic новый subtask
                //удаляем старый subtask и кладем в хешмап с epica'ми переданный
                subtasks.put(subtask.getId(), subtask);
                updateEpicStatus(epics.get(subtask.getEpicId()));
            }
        }
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        TaskStatus newEpicStatus = TaskStatus.IN_PROGRESS;
        ArrayList<TaskStatus> epicSubtasksStatuses = new ArrayList<>();

        for (Integer subtaskId : epic.getSubtasksIds()) {
            epicSubtasksStatuses.add(this.subtasks.get(subtaskId).getStatus());
        }
        boolean allSubtaskStatusesIsDone = true;
        boolean allSubtaskStatusesIsNew = true;
        for (TaskStatus epicSubtaskStatus : epicSubtasksStatuses) {

            if (epicSubtaskStatus == TaskStatus.IN_PROGRESS) {
                epic.status = TaskStatus.IN_PROGRESS;
                return;
            }
            if (epicSubtaskStatus == TaskStatus.NEW)
                allSubtaskStatusesIsDone = false;
            if (epicSubtaskStatus == TaskStatus.DONE)
                allSubtaskStatusesIsNew = false;
        }
        if (allSubtaskStatusesIsDone)
            newEpicStatus = TaskStatus.DONE;
        if (allSubtaskStatusesIsNew)
            newEpicStatus = TaskStatus.NEW;

        epic.status = newEpicStatus;
        updateEpic(epic);
    }

}