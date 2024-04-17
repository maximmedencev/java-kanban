package ru.yandex.practicum.tasktracker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File saveFile;

    public FileBackedTaskManager(File saveFile) {
        this.saveFile = saveFile;
        load();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        return new FileBackedTaskManager(file);
    }

    private void load() {
        String saveData;
        if (saveFile.exists() && !saveFile.isDirectory()) {
            try {
                saveData = Files.readString(saveFile.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
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
                        epics.get(((Subtask) task).getEpicId()).addSubtaskId(task.getId());
                        super.addSubtask((task).getId(), ((Subtask) task).getEpicId(), ((Subtask) task));
                    }
                }
                super.newTaskId = maxIndex + 1;
            }
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(int epicId, Subtask subtask) {
        super.addSubtask(epicId, subtask);
        save();
    }

    private void save() {
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
        switch (task.getClass().toString()) {
            case "class ru.yandex.practicum.tasktracker.Epic":
                return task.getId() +
                        "," + TaskType.EPIC +
                        "," + task.getName() +
                        "," + task.getStatus() +
                        "," + task.getDescription() +
                        ",\n";
            case "class ru.yandex.practicum.tasktracker.Subtask":
                return task.getId() +
                        "," + TaskType.SUBTASK +
                        "," + task.getName() +
                        "," + task.getStatus() +
                        "," + task.getDescription() +
                        "," + ((Subtask) task).getEpicId() +
                        ",\n";
        }
        return task.getId() +
                "," + TaskType.TASK +
                "," + task.getName() +
                "," + task.getStatus() +
                "," + task.getDescription() +
                ",\n";
    }

    protected static Task fromString(String value) {
        String[] valueElements = value.split(",");
        int epicId;
        int id = Integer.parseInt(valueElements[0]);
        TaskType type = TaskType.valueOf(valueElements[1]);
        String name = valueElements[2];
        TaskStatus status = TaskStatus.valueOf(valueElements[3]);
        String description = valueElements[4];

        if (type == TaskType.EPIC) {
            return new Epic(id, name, description);
        }

        if (type == TaskType.SUBTASK) {
            epicId = Integer.parseInt(valueElements[5]);
            return new Subtask(id, epicId, name, description, status);
        }
        return new Task(id, name, description, status);
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }
}