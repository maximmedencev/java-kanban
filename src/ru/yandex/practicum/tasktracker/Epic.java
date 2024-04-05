package ru.yandex.practicum.tasktracker;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Integer> subtasksIds = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(Integer id, String name, String description) {
        super(name, description);
        this.id = id;
    }

    public Epic(Epic epic) {
        super(epic.name, epic.description);
        this.id = epic.id;
        subtasksIds.addAll(epic.getSubtasksIds());
    }

    public void removeSubtask(Integer subtaskId) {
        subtasksIds.remove(subtaskId);
    }

    public int addSubtaskId(Integer subtaskId) {
        if (subtaskId == this.id)
            return -1;

        if (!this.subtasksIds.contains(subtaskId))
            this.subtasksIds.add(subtaskId);

        return 0;
    }

    public void removeAllSubtasksIds() {
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
