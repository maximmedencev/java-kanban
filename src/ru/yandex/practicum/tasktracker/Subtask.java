package ru.yandex.practicum.tasktracker;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    protected int epicId;

    public int getEpicId() {
        return epicId;
    }

    public int setEpicId(int epicId) {
        if (epicId == this.id)
            return -1;
        this.epicId = epicId;
        return 0;
    }

    public Subtask(Subtask subtask) {
        super(subtask.id, subtask.name, subtask.description, subtask.status, subtask.startTime, subtask.duration);
        this.epicId = subtask.epicId;
    }

    public Subtask(int id, String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        super(id, name, description, status, startTime, duration);
    }

    public Subtask(int id, int epicId, String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        super(id, name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus status, LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
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
