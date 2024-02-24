import java.util.ArrayList;

public class Epic extends Task{

    private ArrayList<Subtask> subTasks;

    public Epic(String name, String description, int id) {
        super(name, description, id);
    }

    private void addSubTask(Subtask subtask){
        this.subTasks.add(subtask);
    }


}
