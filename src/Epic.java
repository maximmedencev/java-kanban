import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {

    private ArrayList <Integer> subtasksIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(int id, String name, String description) {
        super(id, name, description);
    }

    public void removeSubtask(int id) {
        subtasksIds.remove(id);
    }

    public void addSubtask(int subtaskId) {
        if(!this.subtasksIds.contains(subtaskId))
            this.subtasksIds.add(subtaskId);
    }

    public ArrayList<Integer> getSubtasks() {
        return this.subtasksIds;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + this.getId() +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status=" + this.getStatus() +
                ", subtasksIds=" + subtasksIds +
                '}';
    }
}
