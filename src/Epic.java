import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private final HashMap<Integer,Subtask> subtasks = new HashMap<>();
    public Epic(String name, String description) {
        super(name, description);
    }
    public void removeSubtask(int subtaskId) {
        subtasks.remove(subtaskId);
    }

    public void addSubtask(Subtask subtask) {
        if (!this.subtasks.containsValue(subtask))
            this.subtasks.put(subtask.getId(), subtask);
    }

    @Override
    public TaskStatus getStatus() {

        //если есть хоть один статус IN_PROGRESS, назначаес epic'у статус IN_PROGRESS и завершаем метод
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getStatus() == TaskStatus.IN_PROGRESS) {
                return TaskStatus.IN_PROGRESS;
            }
        }

        boolean isAllEpicStatusesIsDone = true;

        for (Subtask subtask : subtasks.values()) {
            if (subtask.getStatus() == TaskStatus.NEW) {
                isAllEpicStatusesIsDone = false;
                break;
            }
        }

        if (isAllEpicStatusesIsDone) {//  если все статусы DONE(нет статусов NEW)
            return TaskStatus.DONE;
        }

        boolean isAllEpicStatusesIsNew = true;

        for (Subtask subtask : subtasks.values()) {
            if (subtask.getStatus() == TaskStatus.DONE) {
                isAllEpicStatusesIsNew = false;
                break;
            }
        }

        if (isAllEpicStatusesIsNew) {//  если все статусы NEW(нет статусов DONE)
            return TaskStatus.NEW;
        }
        //если DONE и NEW  вперемежку назначаем epic'у статус IN_PROGRESS
        return TaskStatus.IN_PROGRESS;
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(this.subtasks.values());
    }

    @Override
    public String toString() {
        ArrayList<Integer> subtasksIds = new ArrayList<>();

        for (Subtask subtask : subtasks.values()) {
            subtasksIds.add(subtask.getId());
        }

        return "Epic{" +
                "id=" + this.getId() +
                ", subtasksIds=" + subtasksIds +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status=" + this.getStatus() +
                '}';
    }
}
