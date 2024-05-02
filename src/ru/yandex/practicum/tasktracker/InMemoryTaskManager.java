package ru.yandex.practicum.tasktracker;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasks;
    protected final Map<Integer, Epic> epics;
    protected final Map<Integer, Subtask> subtasks;
    protected final TreeSet<Task> prioritizedTasks;

    public HistoryManager historyManager = Managers.getDefaultHistory();

    protected int newTaskId = 1;

    public InMemoryTaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.prioritizedTasks = new TreeSet<>(Comparator.comparing(task -> task.startTime));
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

    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    protected void refreshEpicStartTimeAndEndTime(Epic epic) {
        if (epic.getSubtasksIds().isEmpty()) {
            epic.startTime = null;
            epic.endTime = null;
            return;
        }
        epic.startTime = getEpicSubtaskList(epic.getId()).stream()
                .min(Comparator.comparing(Task::getStartTime))
                .get()
                .getStartTime();
        epic.endTime = getEpicSubtaskList(epic.getId()).stream()
                .max(Comparator.comparing(Task::getEndTime))
                .get()
                .getEndTime();
    }

    protected void resortPrioritizedTasks() {
        prioritizedTasks.clear();
        prioritizedTasks.addAll(tasks.values().stream()
                .filter(task -> task.getStartTime() != null)
                .collect(Collectors.toList()));
        prioritizedTasks.addAll(subtasks.values().stream()
                .filter(task -> task.getStartTime() != null)
                .collect(Collectors.toList()));

    }

    protected boolean areTasksIntersect(Task t1, Task t2) {
        if (t1.getStartTime() == null || t2.getStartTime() == null)
            return true;
        return (t1.getEndTime().isAfter(t2.getStartTime()) && t1.getEndTime().isBefore(t2.getEndTime())) ||
                (t1.getStartTime().isAfter(t2.getStartTime()) && t1.getStartTime().isBefore(t2.getEndTime())) ||
                (t2.getEndTime().isAfter(t1.getStartTime()) && t2.getEndTime().isBefore(t1.getEndTime())) ||
                (t2.getStartTime().isAfter(t1.getStartTime()) && t2.getStartTime().isBefore(t1.getEndTime()));
    }

    @Override
    public List<Task> getEpicSubtaskList(int id) {
        return this.subtasks.values().stream()
                .filter(subtask -> subtask.getEpicId() == id)
                .collect(Collectors.toList());
    }


    @Override
    public void removeAllTasks() {
        tasks.values().forEach(task -> historyManager.remove(task.getId()));
        this.tasks.clear();
    }

    @Override
    public void removeAllEpics() {
        subtasks.values()
                .forEach(subtask -> historyManager.remove(subtask.getId()));
        epics.values()
                .forEach(epic -> historyManager.remove(epic.getId()));
        this.epics.clear();
        this.removeAllSubtasks();
    }

    @Override
    public void removeAllSubtasks() {
        subtasks.values()
                .forEach(subtask -> historyManager.remove(subtask.getId()));
        this.subtasks.clear();
        if (!epics.isEmpty()) {
            epics.values().stream()
                    .peek(Epic::removeAllSubtasksIds)
                    .peek(this::updateEpicStatus)
                    .forEach(this::refreshEpicStartTimeAndEndTime);
        }
    }

    @Override
    public void removeTask(int taskId) {
        historyManager.remove(taskId);
        tasks.remove(taskId);
    }

    @Override
    public void removeEpic(int epicId) {
        historyManager.remove(epicId);
        epics.get(epicId).getSubtasksIds().stream()
                .peek(subtaskId -> historyManager.remove(subtaskId))
                .forEach(subtasks::remove);
        epics.remove(epicId);
    }

    @Override
    public void removeSubtask(int subtaskId) {
        int epicId = subtasks.get(subtaskId).getEpicId();
        historyManager.remove(subtaskId);
        subtasks.remove(subtaskId);
        epics.get(epicId).removeSubtask(subtaskId);
        this.updateEpicStatus(epics.get(epicId));
        refreshEpicStartTimeAndEndTime(epics.get(epicId));
    }

    @Override
    public void addTask(Task task) {
        if (task == null || task.getClass() != Task.class)
            return;
        setIdForNewTask(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        if (epic == null)
            return;
        setIdForNewTask(epic);
        epics.put(epic.getId(), epic);
    }

    @Override
    public void addSubtask(int epicId, Subtask subtask) {
        if (subtask == null)
            return;
        setIdForNewTask(subtask);
        epics.get(epicId).addSubtaskId(subtask.getId());
        subtask.setEpicId(epicId);
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epics.get(epicId));
        refreshEpicStartTimeAndEndTime(epics.get(epicId));
    }

    @Override
    public int addTask(int id, Task task) {
        if (task == null || task.getClass() != Task.class)
            return -2;
        if (tasks.containsKey(id))
            return -1;
        Optional<Task> optionalIntersectedTask = tasks
                .values()
                .stream()
                .filter(t -> areTasksIntersect(t, task))
                .findFirst();
        if (optionalIntersectedTask.isPresent())
            return -3;
        task.setId(id);
        tasks.put(id, task);
        return 0;
    }

    @Override
    public int addEpic(int id, Epic epic) {
        if (epic == null || epic.getClass() != Epic.class)
            return -2;
        if (epics.containsKey(id))
            return -1;
        epic.setId(id);
        epics.put(id, epic);
        updateEpicStatus(epic);
        return 0;
    }

    @Override
    public int addSubtask(int id, int epicId, Subtask subtask) {
        if (subtask == null || subtask.getClass() != Subtask.class)
            return -2;
        if (subtasks.containsKey(id))
            return -1;
        Optional<Subtask> optionalIntersectedTask = subtasks
                .values()
                .stream()
                .filter(t -> areTasksIntersect(t, subtask))
                .findFirst();
        if (optionalIntersectedTask.isPresent()) {
            Integer subtaskIdToRemove = subtask.getId();
            epics.get(subtask.getEpicId()).getSubtasksIds().remove(subtaskIdToRemove);
            return -3;
        }

        subtask.setId(id);
        epics.get(epicId).addSubtaskId(subtask.getId());
        subtask.setEpicId(epicId);
        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epics.get(epicId));
        refreshEpicStartTimeAndEndTime(epics.get(epicId));
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
        if (tasks.containsKey(task.getId())) {
            Optional<Task> optionalIntersectedTask = tasks
                    .values()
                    .stream()
                    .filter(t -> t.getId() != task.getId())
                    .filter(t -> areTasksIntersect(t, task))
                    .findFirst();
            if (optionalIntersectedTask.isPresent())
                return;
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId()))
            epics.put(epic.getId(), epic);
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            int epicId = subtasks.get(subtask.getId()).getEpicId();
            Optional<Subtask> optionalIntersectedTask = subtasks
                    .values()
                    .stream()
                    .filter(t -> areTasksIntersect(t, subtask))
                    .filter(t -> t.getId() != subtask.getId())
                    .findFirst();
            if (optionalIntersectedTask.isPresent()) {
                Integer subtaskIdToRemove = subtask.getId();
                epics.get(subtask.getEpicId()).getSubtasksIds().remove(subtaskIdToRemove);
                return;
            }
            if (epics.get(epicId).getSubtasksIds().contains(subtask.getId())) {
                subtask.setEpicId(epicId);
                epics.get(epicId).removeSubtask(subtask.getId());
                epics.get(epicId).addSubtaskId(subtask.getId());
                subtasks.put(subtask.getId(), subtask);
                updateEpicStatus(epics.get(subtask.getEpicId()));
                refreshEpicStartTimeAndEndTime(epics.get(subtask.getEpicId()));
            }
        }
    }

    @Override
    public void updateEpicStatus(Epic epic) {
        ArrayList<TaskStatus> epicSubtasksStatuses = new ArrayList<>();
        if (epic.getSubtasksIds().isEmpty()) {
            epic.status = TaskStatus.NEW;
            return;
        }
        epic.getSubtasksIds()
                .forEach(subtaskId -> epicSubtasksStatuses.add(this.subtasks.get(subtaskId).getStatus()));

//        boolean allSubtaskStatusesIsDone = true;
//        boolean allSubtaskStatusesIsNew = true;


        if (epicSubtasksStatuses.stream()
                .anyMatch(taskStatus -> taskStatus == TaskStatus.IN_PROGRESS)) {
            epic.status = TaskStatus.IN_PROGRESS;
            return;
        }
        if (epicSubtasksStatuses.stream()
                .anyMatch(taskStatus -> taskStatus == TaskStatus.NEW) &&
                epicSubtasksStatuses.stream()
                        .anyMatch(taskStatus -> taskStatus == TaskStatus.DONE)
        ) {
            epic.status = TaskStatus.IN_PROGRESS;
            return;
        }
        if (epicSubtasksStatuses.stream()
                .anyMatch(taskStatus -> taskStatus == TaskStatus.NEW)) {
            epic.status = TaskStatus.NEW;
            return;
        }
//
//        for (TaskStatus epicSubtaskStatus : epicSubtasksStatuses) {
//            if (epicSubtaskStatus == TaskStatus.IN_PROGRESS) {
//                epic.status = TaskStatus.IN_PROGRESS;
//                return;
//            }
//            if (epicSubtaskStatus == TaskStatus.NEW)
//                allSubtaskStatusesIsDone = false;
//            if (epicSubtaskStatus == TaskStatus.DONE)
//                allSubtaskStatusesIsNew = false;
//        }
//        if (allSubtaskStatusesIsDone) {
//            epic.status = TaskStatus.DONE;
//            return;
//        }
//        if (allSubtaskStatusesIsNew) {
//            epic.status = TaskStatus.NEW;
//            return;
//        }
        epic.status = TaskStatus.DONE;
    }
}