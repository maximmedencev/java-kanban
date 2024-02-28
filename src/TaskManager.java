import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Epic> epics;
    private final HashMap<Integer, Subtask> subtasks;

    private static int newTaskId = 1;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    public ArrayList<Task> getTasksList() {
        return new ArrayList<>(this.tasks.values());
    }

    public ArrayList<Task> getEpicsList() {
        return new ArrayList<>(this.epics.values());
    }

    public ArrayList<Task> getSubTasksList() {
        return new ArrayList<>(this.subtasks.values());
    }

    public void removeAllTasks() {
        this.tasks.clear();
    }

    public void removeAllEpics() {
        this.epics.clear();
    }

    public void removeAllSubTasks() {
        this.subtasks.clear();
    }

    public void removeTask(int taskId) {
        tasks.remove(taskId);
    }

    public void removeEpic(int epicId) {
        epics.remove(epicId);
    }

    public void removeSubtask(int subtaskId) {
        int epicId = subtasks.get(subtaskId).getEpicId();
        subtasks.remove(subtaskId);
        epics.get(epicId).removeSubtask(subtaskId);
    }

    public void addTask(Task task) {
        if (tasks.containsValue(task))
            return;
        if (task.getId() == 0)
            setIdForNewTask(task);
        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        if (epics.containsValue(epic))
            return;
        if (epic.getId() == 0)
            setIdForNewTask(epic);
        epics.put(epic.getId(), epic);
    }

    public void addSubtask(int epicId, Subtask subtask) {
        if (subtask.getId() == 0)
            setIdForNewTask(subtask);
        epics.get(epicId).addSubtaskId(subtask.getId());
        subtask.setEpicId(epicId);
        subtasks.put(subtask.getId(), subtask);
    }

    public Task getTask(int id) {
        return this.tasks.get(id);
    }

    public Task getEpic(int id) {
        return this.epics.get(id);
    }

    public Task getSubTask(int id) {
        return this.subtasks.get(id);
    }

    private void setIdForNewTask(Task task) {
        task.setId(newTaskId++);
    }

    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId()))
            return;
        tasks.remove(task.getId());
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId()))
            return;
        epics.remove(epic.getId());
        epics.put(epic.getId(), epic);
    }


    public void updateEpicStatus(Epic epic) {
        TaskStatus newEpicStatus = TaskStatus.IN_PROGRESS;
        ArrayList<TaskStatus> epicSubtasksStatuses = new ArrayList<>();

        for (Integer subtaskId : epic.getSubtasksIds()) {
            epicSubtasksStatuses.add(this.subtasks.get(subtaskId).getStatus());
        }
        boolean allSubtaskStatusesIsDone = true;
        boolean allSubtaskStatusesIsNew = true;
        for (TaskStatus epicSubtaskStatus : epicSubtasksStatuses) {

            if (epicSubtaskStatus == TaskStatus.IN_PROGRESS){
                allSubtaskStatusesIsNew = false;
                allSubtaskStatusesIsDone = false;
                break;
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
        int oldEpicId = epic.getId();
        String oldEpicName = epic.getName();
        String oldEpicDescription = epic.getDescription();
        ArrayList<Integer> oldSubtasksIds = epic.getSubtasksIds();
        Epic newEpic = new Epic(oldEpicId, oldEpicName, oldEpicDescription,newEpicStatus,oldSubtasksIds);
        updateEpic(newEpic);
    }


    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {// проверяем есть ли в хеш-таблицах subtask с таким id
            //по id переданного task находим id epic содержащего subtask с таким же id
            int epicId = subtasks.get(subtask.getId()).getEpicId();

            if (epics.get(epicId).getSubtasksIds().contains(subtask.getId())) {// если в найденом epic есть такой subtask
                //привязываем по id переданный subtask к эпику старого subtask'a
                subtask.setEpicId(subtasks.get(subtask.getId()).getEpicId());
                epics.get(epicId).removeSubtask(subtask.getId()); //удаляем старый subtask из epic
                epics.get(epicId).addSubtaskId(subtask.getId());            //добавляем в epic новый subtask
                //удаляем старый subtask и кладем в хешмап с epica'ми переданный
                subtasks.remove(subtask.getId());
                subtasks.put(subtask.getId(), subtask);
                updateEpicStatus(epics.get(subtask.getEpicId()));
            }
        }
    }
}