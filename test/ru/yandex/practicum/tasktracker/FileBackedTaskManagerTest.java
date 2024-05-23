package ru.yandex.practicum.tasktracker;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    FileBackedTaskManager fileBackedTaskManager;
    File saveFile;

    @BeforeEach
    public void setUp() throws IOException {
        saveFile = File.createTempFile("java-kanban-save-test", ".csv");
        super.taskManager = FileBackedTaskManager.loadFromFile(saveFile);
    }

    @Test
    public void testManagerSaveExceptionException() {
        assertThrows(ManagerSaveException.class, () -> {
            try (FileWriter writer = new FileWriter(saveFile.getAbsolutePath())) {
                writer.write("Some string\n");
                throw new IOException("Test IOException");
            } catch (IOException e) {
                throw new ManagerSaveException("Произошла ошибка во время записи файла.");
            }
        }, "ManagerSaveException не было выброшено");
    }


    @Test
    public void taskListsShouldBeEmptyForEmptySaveFile() {
        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(saveFile);
        Assertions.assertTrue(fileBackedTaskManager.getTasksList().isEmpty(), "Список задач не пустой!");
        Assertions.assertTrue(fileBackedTaskManager.getEpicsList().isEmpty(), "Список эпиков не пустой!");
        Assertions.assertTrue(fileBackedTaskManager.getSubtasksList().isEmpty(), "Список подзадач не пустой!");
    }

    @Test
    public void fileShouldBeEmptyForEmptyTaskLists() throws IOException {
        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(saveFile);
        String saveData;
        saveData = Files.readString(saveFile.toPath());
        Assertions.assertTrue(saveData.isEmpty());
    }

    @Test
    public void fileAndTaskListsShouldHaveSameDataAfterLoad() throws IOException {
        String data =
                "id,type,name,status,description,epic\n" +
                        "1,TASK,Имя таска 1,NEW,Описание таска 1,\n" +
                        "2,TASK,Имя таска 2,NEW,Описание таска 2,\n" +
                        "8,TASK,Имя таска 3,NEW,Описание таска 3,\n" +
                        "7,EPIC,Имя эпика 1,NEW,Описание эпика 1,\n" +
                        "3,EPIC,Имя эпика 2,DONE,Описание эпика 2,\n" +
                        "4,SUBTASK,Имя сабтаска 1,DONE,Описание сабтаска 1,3\n" +
                        "5,SUBTASK,Имя сабтаска 2,DONE,Описание сабтаска 2,3\n" +
                        "6,SUBTASK,Имя сабтаска 3,NEW,Описание сабтаска 3,7";

        FileWriter writer = new FileWriter(saveFile.getAbsolutePath());
        writer.write(data);

        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(saveFile);
        String saveData;
        saveData = Files.readString(saveFile.toPath());
        if (!saveData.isEmpty()) {
            String[] saveDataArray = saveData.split("\n");
            for (int i = 1; i < saveDataArray.length; i++) {
                String[] valueElements = saveDataArray[i].split(",");
                int epicId;
                int id = Integer.parseInt(valueElements[0]);
                TaskType type = TaskType.valueOf(valueElements[1]);
                String name = valueElements[2];
                TaskStatus status = TaskStatus.valueOf(valueElements[3]);
                String description = valueElements[4];

                if (type == TaskType.TASK) {
                    Assertions.assertEquals(id, fileBackedTaskManager.
                            tasks.get(id).getId(), "task.id не совпадают");
                    Assertions.assertEquals(name, fileBackedTaskManager.
                            tasks.get(id).getName(), "task.name не совпадают");
                    Assertions.assertEquals(description, fileBackedTaskManager.
                            tasks.get(id).getDescription(), "task.description не совпадают");
                    Assertions.assertEquals(status, fileBackedTaskManager.
                            tasks.get(id).getStatus(), "task.status не совпадают");
                }

                if (type == TaskType.EPIC) {
                    Assertions.assertEquals(id, fileBackedTaskManager.
                            epics.get(id).getId(), "epic.id не совпадают");
                    Assertions.assertEquals(name, fileBackedTaskManager.
                            epics.get(id).getName(), "epic.name не совпадают");
                    Assertions.assertEquals(description, fileBackedTaskManager.
                            epics.get(id).getDescription(), "epic.description не совпадают");
                    Assertions.assertEquals(status, fileBackedTaskManager.
                            epics.get(id).getStatus(), "epic.status не совпадают");
                }

                if (type == TaskType.SUBTASK) {
                    epicId = Integer.parseInt(valueElements[5]);
                    Assertions.assertEquals(id, fileBackedTaskManager.
                            subtasks.get(id).getId(), "id не совпадают");
                    Assertions.assertEquals(name, fileBackedTaskManager.
                            subtasks.get(id).getName(), "name не совпадают");
                    Assertions.assertEquals(description, fileBackedTaskManager.
                            subtasks.get(id).getDescription(), "description не совпадают");
                    Assertions.assertEquals(epicId, fileBackedTaskManager.
                            subtasks.get(id).getEpicId(), "description не совпадают");
                    Assertions.assertEquals(status, fileBackedTaskManager.
                            subtasks.get(id).getStatus(), "subtask.status не совпадают");
                }
            }
        }
    }

    @Test
    public void fileAndTaskListsShouldHaveSameDataAfterSave() throws IOException {
        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(saveFile);
        Epic epic1 = new Epic("Тестовый эпик №1",
                "Описание эпик №1");
        fileBackedTaskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Тестовый сабтаск №1",
                "Описание сабтаск №1",
                TaskStatus.NEW,
                LocalDateTime.of(2024, 5, 2, 11, 0, 0),
                Duration.ofMinutes(10));
        fileBackedTaskManager.addSubtask(epic1.getId(), subtask1);
        Subtask subtask2 = new Subtask("Тестовый сабтаск №2",
                "Описание сабтаск №2",
                TaskStatus.DONE,
                LocalDateTime.of(2024, 5, 2, 12, 0, 0),
                Duration.ofMinutes(30));
        fileBackedTaskManager.addSubtask(epic1.getId(), subtask2);
        Task task1 = new Task("Тестовый таск №1",
                "Описание таск №1",
                TaskStatus.DONE,
                LocalDateTime.of(2024, 5, 2, 13, 0, 0),
                Duration.ofMinutes(30));
        fileBackedTaskManager.addTask(task1);

        String saveData;
        if (saveFile.exists()) {
            saveData = Files.readString(saveFile.toPath());
            if (!saveData.isEmpty()) {
                String[] saveDataArray = saveData.split("\n");
                for (int i = 1; i < saveDataArray.length; i++) {
                    Task task = FileBackedTaskManager.fromString(saveDataArray[i]);
                    if (task.getClass() == Epic.class) {
                        Assertions.assertEquals(task.getId(), fileBackedTaskManager.
                                epics.get(task.getId()).getId(), "epic.id не совпадают");
                        Assertions.assertEquals(task.getName(), fileBackedTaskManager.
                                epics.get(task.getId()).getName(), "epic.name не совпадают");
                        Assertions.assertEquals(task.getDescription(), fileBackedTaskManager.
                                epics.get(task.getId()).getDescription(), "epic.description не совпадают");
                    }

                    if (task.getClass() == Task.class) {
                        Assertions.assertEquals(task.getId(), fileBackedTaskManager.
                                tasks.get(task.getId()).getId(), "task.id не совпадают");
                        Assertions.assertEquals(task.getName(), fileBackedTaskManager.
                                tasks.get(task.getId()).getName(), "task.name не совпадают");
                        Assertions.assertEquals(task.getDescription(), fileBackedTaskManager.
                                tasks.get(task.getId()).getDescription(), "task.description не совпадают");
                        Assertions.assertEquals(task.getStatus(), fileBackedTaskManager.
                                tasks.get(task.getId()).getStatus(), "task.status не совпадают");
                    }

                    if (task.getClass() == Subtask.class) {
                        Assertions.assertEquals(task.getId(), fileBackedTaskManager.
                                subtasks.get(task.getId()).getId(), "subtask.id не совпадают");
                        Assertions.assertEquals(task.getName(), fileBackedTaskManager.
                                subtasks.get(task.getId()).getName(), "subtask.name не совпадают");
                        Assertions.assertEquals(task.getDescription(), fileBackedTaskManager.
                                subtasks.get(task.getId()).getDescription(), "subtask.description не совпадают");
                        Assertions.assertEquals(task.getStatus(), fileBackedTaskManager.
                                subtasks.get(task.getId()).getStatus(), "subtask.status не совпадают");
                    }
                }
            }
        }
    }
}