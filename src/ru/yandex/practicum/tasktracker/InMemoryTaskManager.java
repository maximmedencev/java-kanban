package ru.yandex.practicum.tasktracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Epic> epics;
    private final Map<Integer, Subtask> subtasks;

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
    public ArrayList<Task> getSubtasksList() {
        return new ArrayList<>(this.subtasks.values());
    }


    @Override
    public ArrayList<Task> getEpicSubtaskList(int id) {
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
        this.removeAllSubtasks();
    }

    @Override
    public void removeAllSubtasks() {
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
    public int addTask(int id, Task task) {
        if (tasks.containsKey(id))
            return -1;
        task.setId(id);
        tasks.put(id, task);
        return 0;
    }

    @Override
    public int addEpic(int id, Epic epic) {
        if (epics.containsKey(id))
            return -1;

        epic.setId(id);
        epics.put(id, epic);

        return 0;
    }

    @Override
    public int addSubtask(int id, int epicId, Subtask subtask) {
        if (subtasks.containsKey(id))
            return -1;

        subtask.setId(id);

        epics.get(epicId).addSubtaskId(subtask.getId());
        subtask.setEpicId(epicId);
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epics.get(epicId));
        return 0;

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
    public Subtask getSubtask(int id) {
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