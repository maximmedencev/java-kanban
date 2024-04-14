package ru.yandex.practicum.tasktracker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private File saveFile;

    public FileBackedTaskManager(File saveFile) {
        this.saveFile = saveFile;
        String saveData;

        if (saveFile.exists() && !saveFile.isDirectory()) {
            try {
                saveData = Files.readString(saveFile.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String[] saveDataArray = saveData.split("\n");
            for (int i = 1; i < saveDataArray.length; i++) {
                Task tmpTask = fromString(saveDataArray[i]);

                if (tmpTask.getClass() == Task.class) {
                    super.addTask(tmpTask.getId(), tmpTask);
                }

                if (tmpTask.getClass() == Epic.class) {
                    super.addEpic(tmpTask.getId(), (Epic) tmpTask);
                }

                if (tmpTask.getClass() == Subtask.class) {
                    Subtask tmpSubtask = (Subtask) tmpTask;
                    super.addSubtask(tmpSubtask.getId(), tmpSubtask.getEpicId(), tmpSubtask);
                }

            }
            List<Integer> history = historyFromString(saveData);
            HashMap<Integer, Task> allTasks = new HashMap<>(super.tasks);
            allTasks.putAll(super.epics);
            allTasks.putAll(super.subtasks);

            for (int historyIdElement : history) {
                super.historyManager.add(allTasks.get(historyIdElement));
            }
            super.newTaskId = Collections.max(allTasks.keySet());

        }
    }

    private static TaskTypes getTaskType(Task task) {
        if (task.getClass() == Task.class)
            return TaskTypes.TASK;
        if (task.getClass() == Subtask.class)
            return TaskTypes.SUBTASK;
        if (task.getClass() == Epic.class)
            return TaskTypes.EPIC;
        return null;
    }

    static String historyToString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        List<Task> history = manager.getHistory();

        for (Task task : history) {
            TaskTypes type = getTaskType(task);
            sb.append(task.getId()).append(",").
                    append(type).append(",").
                    append(task.getName()).append(",").
                    append(task.getStatus()).append(",").
                    append(task.getDescription()).append(",");
            if (type == TaskTypes.SUBTASK) {
                Subtask tmpSubtask = (Subtask) task;
                sb.append(tmpSubtask.getEpicId()).append("\n");
            } else {
                sb.append("\n");
            }
        }

        return sb.toString();
    }

    static List<Integer> historyFromString(String value) {
        List<Integer> listToReturn = new ArrayList<>();
        String[] historyDataArray = value.split("\n");
        for (int i = 1; i < historyDataArray.length; i++) {
            String[] elements = historyDataArray[i].split(",");
            if (!elements[3].equals(TaskStatus.NEW.toString()))
                listToReturn.add(Integer.parseInt(elements[0]));
        }
        return listToReturn;
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
                if (task.getStatus() == TaskStatus.NEW)
                    writer.write(toString(task));
            }

            for (Epic epic : epics.values()) {
                if (epic.getStatus() == TaskStatus.NEW)
                    writer.write(toString(epic));
            }

            for (Subtask subtask : subtasks.values()) {
                if (subtask.getStatus() == TaskStatus.NEW)
                    writer.write(toString(subtask));
            }

            writer.write(historyToString(super.historyManager));

        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка во время записи файла сохранения.");
        }
    }


    private String toString(Task task) {

        if (task.getClass() == Epic.class) {
            return task.getId() +
                    "," + TaskTypes.EPIC +
                    "," + task.getName() +
                    "," + task.getStatus() +
                    "," + task.getDescription() +
                    ",\n";
        }

        if (task.getClass() == Subtask.class) {
            Subtask subtask = (Subtask) task;
            return task.getId() +
                    "," + TaskTypes.SUBTASK +
                    "," + task.getName() +
                    "," + task.getStatus() +
                    "," + task.getDescription() +
                    "," + subtask.getEpicId() +
                    ",\n";
        }

        return task.getId() +
                "," + TaskTypes.TASK +
                "," + task.getName() +
                "," + task.getStatus() +
                "," + task.getDescription() +
                ",\n";
    }

    private Task fromString(String value) {
        String[] valueElements = value.split(",");

        int epicId;

        int id = Integer.parseInt(valueElements[0]);
        TaskTypes type = TaskTypes.valueOf(valueElements[1]);
        String name = valueElements[2];
        TaskStatus status = TaskStatus.valueOf(valueElements[3]);
        String description = valueElements[4];

        if (type == TaskTypes.EPIC) {
            return new Epic(id, name, description);
        }

        if (type == TaskTypes.SUBTASK) {
            epicId = Integer.parseInt(valueElements[5]);
            epics.get(epicId).addSubtaskId(id);
            return new Subtask(id, epicId, name, description, status);
        }

        return new Task(id, name, description, status);
    }

    public static class ManagerSaveException extends RuntimeException {
        public ManagerSaveException(final String message) {
            super(message);
        }
    }
}
