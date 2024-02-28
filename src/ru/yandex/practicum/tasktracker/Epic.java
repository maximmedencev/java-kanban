package ru.yandex.practicum.tasktracker;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subtasksIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(int id, String name, String description, TaskStatus status, ArrayList<Integer> subtasksIds) {
        super(id, name, description, status);
        this.subtasksIds = subtasksIds;
        this.status=status;
    }

    public void removeSubtask(Integer subtaskId) {
        subtasksIds.remove(subtaskId);
    }

    public void addSubtaskId(Integer subtaskId) {
        if (!this.subtasksIds.contains(subtaskId)){
            this.subtasksIds.add(subtaskId);
        }
    }

    public void removeAllSubtasksIds(){
        this.subtasksIds.clear();
    }
    public ArrayList<Integer> getSubtasksIds() {
        return this.subtasksIds;
    }

    @Override
    public String toString() {

        return "ru.yandex.practicum.tasktracker.Epic{" +
                "id=" + this.getId() +
                ", subtasksIds=" + subtasksIds +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status=" + this.getStatus() +
                '}';
    }
}
