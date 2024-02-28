package ru.yandex.practicum.tasktracker;

public class Subtask extends Task {
    protected int epicId;

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public Subtask(int id, String name, String description, TaskStatus status) {
        super(id, name, description, status);
    }

    public Subtask(String name, String description, TaskStatus status) {
        super(name, description, status);

    }

    @Override
    public String toString() {
        return "ru.yandex.practicum.tasktracker.Subtask{" +
                "id=" + this.getId() +
                ", epicId=" + epicId +
                ", name='" + this.getName() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", status=" + this.getStatus() +
                '}';
    }
}
