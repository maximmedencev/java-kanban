import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {

    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, Epic> epics;
    private HashMap<Integer, Subtask> subtasks;

    private static int newTaskId = 0;

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
        for(Epic e: epics.values()){
            e.calculateStatus();
        }

    }

    public void removeTask(int taskId){
        tasks.remove(taskId);
    }

    public void removeEpic(int epicId){
        epics.remove(epicId);
    }

    public void removeSubtask(int subtaskId){
        subtasks.remove(subtaskId);
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
        setIdForNewTask(subtask);
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
        tasks.remove(task.getId());
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        epics.remove(epic.getId());
        epics.put(epic.getId(), epic);
    }


    public ArrayList<Subtask> getEpicSubtasksList(int epicId){

        return epics.get(epicId).getSubtasksList();
    }

    public void updateSubtask(Subtask subtask) {
        TaskStatus subTaskOldStatus = this.getSubTask(subtask.getId()).getStatus();

        //для хранения всех статусов subtask'ов эпика, которому принадлежит subtask
        ArrayList<TaskStatus> allEpicSubtasksStatuses = new ArrayList<>();

        //привязываем по id переданный subtask к эпику старого subtask'a
        subtask.setEpicId(subtasks.get(subtask.getId()).getEpicId());

        //удаляем старый subtask и кладем в хешмап с epica'ми переданный
        subtasks.remove(subtask.getId());
        subtasks.put(subtask.getId(), subtask);


        if (subTaskOldStatus != subtask.getStatus()) {// проверяем другой ли статус в новом subtask'e
                                                      // если да, то определяем новый статус epic'a



            //если есть хоть один статус IN_PROGRESS, назначаес epic'у статус IN_PROGRESS и завершаем метод


        }

    }
}
