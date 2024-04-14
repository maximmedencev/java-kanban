import ru.yandex.practicum.tasktracker.*;

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
//
//        Task task1 = new Task("Ремонт", "Отремонтировать телефон",
//                TaskStatus.NEW);
//        fileBackedTaskManager.addTask(task1);
//        Task task2 = new Task("Сделать уборку в доме",
//                "Сделать влажную уборку в доме",
//                TaskStatus.NEW);
//        fileBackedTaskManager.addTask(task2);
//        Epic epic1 = new Epic("Написать песню",
//                "Записать инструментальную композицию");
//        fileBackedTaskManager.addEpic(epic1);
//        Subtask subtask1 = new Subtask("Записать ритм-гитару",
//                "Сочинить и записать партию ритм-гитары",
//                TaskStatus.DONE);
//        fileBackedTaskManager.addSubtask(epic1.getId(), subtask1);
//        Subtask subtask2 = new Subtask("Записать бас-гитару",
//                "Сочинить и записать партию бас-гитары",
//                TaskStatus.DONE);
//        fileBackedTaskManager.addSubtask(epic1.getId(), subtask2);
//        Subtask subtask3 = new Subtask("Записать ударные",
//                "Сочинить и записать партию ударных",
//                TaskStatus.DONE);
//        fileBackedTaskManager.addSubtask(epic1.getId(), subtask3);
//
//        Epic epic2 = new Epic("Заварить чай", "Отвлечься");
//        fileBackedTaskManager.addEpic(epic2);
//
//        //запрос задач с повторами(в историю сохраняется последний запрос)
//        fileBackedTaskManager.getTask(task1.getId());
//        fileBackedTaskManager.getEpic(epic1.getId());
//        fileBackedTaskManager.getTask(task2.getId());
//        fileBackedTaskManager.getSubtask(subtask3.getId());
//        fileBackedTaskManager.getTask(task2.getId());
//        fileBackedTaskManager.getSubtask(subtask1.getId());
//        fileBackedTaskManager.getEpic(epic2.getId());
//        fileBackedTaskManager.getEpic(epic2.getId());
//
//        printAllTasks(fileBackedTaskManager);
//
//        fileBackedTaskManager.removeTask(task2.getId());
//        fileBackedTaskManager.removeEpic(epic1.getId());

        printAllTasks(fileBackedTaskManager);


    }
}


