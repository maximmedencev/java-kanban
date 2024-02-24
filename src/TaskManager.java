import java.util.HashMap;

public class TaskManager {

    private HashMap <Integer,Task> tasks;
    private HashMap <Integer,Epic> epics;
    private HashMap <Integer,Subtask> subtasks;

    private static int newTaskId = 0;

    private void setIdForNewTask(Task task){
        task.setId(newTaskId++);

    }

}
