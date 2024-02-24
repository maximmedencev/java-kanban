public class TaskManager {

    private static int newTaskId = 0;

    private void setIdForNewTask(Task task){
        task.setId(newTaskId++);

    }

}
