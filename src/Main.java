import ru.yandex.practicum.tasktracker.*;

public class Main {
    public static TaskManager inMemoryTaskManager;

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
        inMemoryTaskManager = Managers.getDefault();

        System.out.println("Начало работы программы...");

        Task task1 = new Task("Ремонт", "Отремонтировать телефон",
                TaskStatus.NEW);
        inMemoryTaskManager.addTask(task1);
        Task task2 = new Task("Сделать уборку в доме",
                "Сделать влажную уборку в доме",
                TaskStatus.NEW);
        inMemoryTaskManager.addTask(task2);
        Epic epic1 = new Epic("Написать песню",
                "Записать инструментальную композицию");
        inMemoryTaskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Записать ритм-гитару",
                "Сочинить и записать партию ритм-гитары",
                TaskStatus.DONE);
        inMemoryTaskManager.addSubtask(epic1.getId(), subtask1);
        Subtask subtask2 = new Subtask("Записать бас-гитару",
                "Сочинить и записать партию бас-гитары",
                TaskStatus.DONE);
        inMemoryTaskManager.addSubtask(epic1.getId(), subtask2);
        Subtask subtask3 = new Subtask("Записать ударные",
                "Сочинить и записать партию ударных",
                TaskStatus.DONE);
        inMemoryTaskManager.addSubtask(epic1.getId(), subtask3);

        Epic epic2 = new Epic("Заварить чай", "Отвлечься");
        inMemoryTaskManager.addEpic(epic2);

        //запрос задач с повторами(в историю сохраняется последний запрос)
        inMemoryTaskManager.getTask(task1.getId());
        inMemoryTaskManager.getEpic(epic1.getId());
        inMemoryTaskManager.getTask(task2.getId());
        inMemoryTaskManager.getSubtask(subtask3.getId());
        inMemoryTaskManager.getTask(task2.getId());
        inMemoryTaskManager.getSubtask(subtask1.getId());
        inMemoryTaskManager.getEpic(epic2.getId());
        inMemoryTaskManager.getEpic(epic2.getId());

        printAllTasks(inMemoryTaskManager);

        inMemoryTaskManager.removeTask(task2.getId());
        inMemoryTaskManager.removeEpic(epic1.getId());

        printAllTasks(inMemoryTaskManager);

    }
}


