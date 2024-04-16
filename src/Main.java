import ru.yandex.practicum.tasktracker.Managers;
import ru.yandex.practicum.tasktracker.Task;
import ru.yandex.practicum.tasktracker.TaskManager;

import java.io.File;

public class Main {
    public static TaskManager fileBackedTaskManager;

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasksList()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpicsList()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubtaskList(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasksList()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }

    public static void main(String[] args) {
        File file = new File("java-kanban-save.csv");
        fileBackedTaskManager = Managers.loadFromFile(file);
        System.out.println("Начало работы программы...");
        printAllTasks(fileBackedTaskManager);
    }
}


