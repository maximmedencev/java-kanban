import ru.yandex.practicum.tasktracker.FileBackedTaskManager;
import ru.yandex.practicum.tasktracker.TaskManager;

import java.io.File;

public class Main {
    public static TaskManager fileBackedTaskManager;

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        manager.getTasksList().forEach(System.out::println);

        System.out.println("Эпики:");
        manager.getEpicsList().forEach(epic -> {
            System.out.println(epic);
            manager.getEpicSubtaskList(epic.getId())
                    .forEach(subtask -> System.out.println("--> " + subtask));
        });

        System.out.println("Подзадачи:");
        manager.getSubtasksList().forEach(System.out::println);

        System.out.println("История:");
        manager.getHistory().forEach(System.out::println);
    }

    public static void main(String[] args) {
        File file = new File("java-kanban-save.csv");
        fileBackedTaskManager = FileBackedTaskManager.loadFromFile(file);
        System.out.println("Начало работы программы...");
        printAllTasks(fileBackedTaskManager);
        //System.out.println(fileBackedTaskManager.getPrioritizedTasks());
//        System.out.println(fileBackedTaskManager.getPrioritizedTasks().size());
////        for(Task t: fileBackedTaskManager.getPrioritizedTasks()){
//            System.out.println(t.getId()+"*)"+t.getStartTime());
//        }

    }
}


