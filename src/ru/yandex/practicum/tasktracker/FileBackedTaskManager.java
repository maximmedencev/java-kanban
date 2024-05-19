package ru.yandex.practicum.tasktracker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File saveFile;
    public static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public FileBackedTaskManager(File saveFile) throws IntersectionException {
        this.saveFile = saveFile;
        load();
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IntersectionException {
        return new FileBackedTaskManager(file);
    }

    private void load() throws IntersectionException {
        String saveData;
        if (saveFile.exists() && !saveFile.isDirectory()) {
            try {
                saveData = Files.readString(saveFile.toPath());
            } catch (IOException e) {
                throw new ManagerSaveException("Произошла ошибка во время чтения файла сохранения.");
            }
            int maxIndex = 0;

            if (!saveData.isEmpty()) {
                String[] saveDataArray = saveData.split("\n");
                for (int i = 1; i < saveDataArray.length; i++) {
                    Task task = fromString(saveDataArray[i]);

                    if (maxIndex < task.getId())
                        maxIndex = task.getId();

                    if (task.getClass() == Epic.class) {
                        super.addEpic(task.getId(), (Epic) task);
                    }

                    if (task.getClass() == Task.class) {
                        super.addTask(task.getId(), task);
                    }

                    if (task.getClass() == Subtask.class) {
                        if (epics.containsKey(((Subtask) task).getEpicId())) {
                            epics.get(((Subtask) task).getEpicId()).addSubtaskId(task.getId());
                            super.addSubtask((task).getId(), ((Subtask) task).getEpicId(), ((Subtask) task));
                        } else {
                            super.addSubtask((Subtask) task);
                        }
                    }
                }
                super.newTaskId = maxIndex + 1;
            }
        }
    }

    @Override
    public void addTask(Task task) throws IntersectionException {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(int epicId, Subtask subtask) throws IntersectionException {
        super.addSubtask(epicId, subtask);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) throws IntersectionException {
        super.addSubtask(subtask);
        save();
    }

    protected void save() {
        try (FileWriter writer = new FileWriter(saveFile.getAbsolutePath())) {
            writer.write("id,type,name,status,description,epic\n");

            for (Task task : tasks.values()) {
                writer.write(toString(task));
            }

            for (Epic epic : epics.values()) {
                writer.write(toString(epic));
            }

            for (Subtask subtask : subtasks.values()) {
                writer.write(toString(subtask));
            }

        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи файла сохранения.");
        }
    }

    protected static String toString(Task task) {
        String startTime = task.getStartTime() == null ? "null" : task.getStartTime().format(DATE_TIME_FORMATTER);
        String duration = task.getDuration() == null ? "null" : task.getDuration().toString();

        switch (task.getClass().getName()) {
            case "ru.yandex.practicum.tasktracker.Epic":
                return task.getId() +
                        "," + TaskType.EPIC +
                        "," + task.getName() +
                        "," + task.getStatus() +
                        "," + task.getDescription() +
                        ",\n";
            case "ru.yandex.practicum.tasktracker.Subtask":
                return task.getId() +
                        "," + TaskType.SUBTASK +
                        "," + task.getName() +
                        "," + task.getStatus() +
                        "," + task.getDescription() +
                        "," + ((Subtask) task).getEpicId() +
                        "," + startTime +
                        "," + duration +
                        ",\n";
            case "ru.yandex.practicum.tasktracker.Task":
                return task.getId() +
                        "," + TaskType.TASK +
                        "," + task.getName() +
                        "," + task.getStatus() +
                        "," + task.getDescription() +
                        "," + startTime +
                        "," + duration +
                        ",\n";
        }

        return null;
    }

    protected static Task fromString(String value) {
        String[] valueElements = value.split(",");
        int epicId;
        int id = Integer.parseInt(valueElements[0]);
        TaskType type = TaskType.valueOf(valueElements[1]);
        String name = valueElements[2];
        TaskStatus status = TaskStatus.valueOf(valueElements[3]);
        String description = valueElements[4];
        LocalDateTime startTime = null;
        Duration duration = null;


        if (type == TaskType.EPIC) {
            return new Epic(id, name, description);
        }

        if (type == TaskType.SUBTASK) {
            epicId = Integer.parseInt(valueElements[5]);
            startTime = valueElements[6].equals("null") ? null : LocalDateTime.parse(valueElements[6], DATE_TIME_FORMATTER);
            duration = valueElements[7].equals("null") ? null : Duration.parse(valueElements[7]);
            return new Subtask(id, epicId, name, description, status, startTime, duration);
        }

        startTime = valueElements[5].equals("null") ? null : LocalDateTime.parse(valueElements[5], DATE_TIME_FORMATTER);
        duration = valueElements[6].equals("null") ? null : Duration.parse(valueElements[6]);

        return new Task(id, name, description, status, startTime, duration);
    }

    @Override
    public void updateTask(Task task) throws IntersectionException {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) throws IntersectionException {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeTask(int taskId) {
        super.removeTask(taskId);
        save();
    }

    @Override
    public void removeEpic(int epicId) {
        super.removeEpic(epicId);
        save();
    }

    @Override
    public void removeSubtask(int subtaskId) {
        super.removeSubtask(subtaskId);
        save();
    }
}