import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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

    public void removeAllEpicsWithSubtasks() {
        this.epics.clear();
        this.subtasks.clear();
    }

    public void removeAllSubTasks() {
        this.subtasks.clear();
        for (Epic e : epics.values()) {
            e.setStatus(TaskStatus.NEW);
        }
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
        setIdForNewTask(task);
        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        setIdForNewTask(epic);
        epics.put(epic.getId(), epic);
    }

    public void addSubtask(int epicId, Subtask subtask) {
        if (subtask.getId() == 0)
            setIdForNewTask(subtask);
        epics.get(epicId).addSubtask(subtask.getId());
        subtask.setEpicId(epicId);
        subtasks.put(subtask.getId(), subtask);
        refreshEpicStatus(subtask.getEpicId());
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
        tasks.remove(task.getId());
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epics.remove(epic.getId());
        epics.put(epic.getId(), epic);
    }


    public ArrayList<Subtask> getEpicSubtasksList(int epicId) {
        ArrayList<Subtask> subtaskList = new ArrayList<>();


        for (Subtask st : this.subtasks.values()) {
            if (st.getEpicId() == epicId) {
                subtaskList.add(st);
            }
        }

        return subtaskList;

    }

    private void refreshEpicStatus(int epicId) {
        //для хранения всех статусов subtask'ов эпика, которому принадлежит subtask
        ArrayList<TaskStatus> allEpicSubtasksStatuses = new ArrayList<>();
        for (Subtask st : this.getEpicSubtasksList(epicId)) {    // заполняем список статусов  всех  subtask'oв epic'a
            allEpicSubtasksStatuses.add(st.getStatus());
        }

        //если есть хоть один статус IN_PROGRESS, назначаес epic'у статус IN_PROGRESS и завершаем метод
        for (TaskStatus status : allEpicSubtasksStatuses) {
            if (status == TaskStatus.IN_PROGRESS) {
                epics.get(epicId).setStatus(TaskStatus.IN_PROGRESS);
                return;
            }
        }

        boolean isAllEpicStatusesIsDone = true;

        for (TaskStatus status : allEpicSubtasksStatuses) {
            if (status == TaskStatus.NEW) {
                isAllEpicStatusesIsDone = false;
                break;
            }
        }

        if (isAllEpicStatusesIsDone) {//  если все статусы DONE(нет статусов NEW)
            epics.get(epicId).setStatus(TaskStatus.DONE);
            return;
        }

        boolean isAllEpicStatusesIsNew = true;

        for (TaskStatus status : allEpicSubtasksStatuses) {
            if (status == TaskStatus.DONE) {
                isAllEpicStatusesIsNew = false;
                break;
            }
        }

        if (isAllEpicStatusesIsNew) {//  если все статусы NEW(нет статусов DONE)
            epics.get(epicId).setStatus(TaskStatus.NEW);
            return;
        }

        //если DONE и NEW  вперемежку назначаем epic'у статус IN_PROGRESS
        epics.get(epicId).setStatus(TaskStatus.IN_PROGRESS);
    }

    public void updateSubtask(Subtask subtask) {

        if (subtasks.containsKey(subtask.getId())) {// проверяем есть ли в хеш-таблицах subtask с таким id
            //по id переданного task находим id epic содержащего subtask с таким же id
            int epicId = subtasks.get(subtask.getId()).getEpicId();

            if (getEpicSubtasksList(epicId).contains(subtask)) {// если в найденом epic есть такой subtask
                TaskStatus subTaskOldStatus = this.getSubTask(subtask.getId()).getStatus();

                //привязываем по id переданный subtask к эпику старого subtask'a
                subtask.setEpicId(subtasks.get(subtask.getId()).getEpicId());

                //удаляем старый subtask и кладем в хешмап с epica'ми переданный
                subtasks.remove(subtask.getId());
                subtasks.put(subtask.getId(), subtask);

                this.refreshEpicStatus(subtask.getEpicId());


            }
        }
    }
}