import ru.yandex.practicum.tasktracker.*;

public class Main {
    public static TaskManager taskManager = new TaskManager();

    public static void printAllTasks() {

        System.out.println(taskManager.getTasksList());
        System.out.println(taskManager.getEpicsList());
        System.out.println(taskManager.getSubTasksList());
    }

    public static void main(String[] args) {
        System.out.println("Начало работы программы...");

        Task task1 = new Task("Ремонт", "Отремонтировать телефон",
                TaskStatus.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("Сделать уборку в доме",
                "Сделать влажную уборку в доме",
                TaskStatus.NEW);
        taskManager.addTask(task2);
        Epic epic1 = new Epic("Написать песню",
                "Записать инструментальную композицию");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Записать ритм-гитару",
                "Сочинить и записать партию ритм-гитары",
                TaskStatus.DONE);
        taskManager.addSubtask(epic1.getId(), subtask1);
        Subtask subtask2 = new Subtask("Записать бас-гитару",
                "Сочинить и записать партию бас-гитары",
                TaskStatus.DONE);
        taskManager.addSubtask(epic1.getId(), subtask2);
        Epic epic2 = new Epic("Заварить чай", "Отвлечься");
        taskManager.addEpic(epic2);
        Subtask subtask3 = new Subtask("Заваривание чая",
                "Залить заварку кипятком",
                TaskStatus.NEW);
        taskManager.addSubtask(epic2.getId(), subtask3);

        printAllTasks();

        Subtask subtask1Corrected = new Subtask(subtask1.getId(),
                "Записать ритм-гитару",
                "Сочинить и записать партию ритм-гитары",
                TaskStatus.IN_PROGRESS);
        Subtask subtask2Corrected = new Subtask(subtask2.getId(),
                "Записать бас-гитару",
                "Сочинить и записать партию бас-гитары",
                TaskStatus.DONE);
        taskManager.updateSubtask(subtask1Corrected);
        taskManager.updateSubtask(subtask2Corrected);
        Subtask subtask3Corrected = new Subtask(subtask3.getId(),
                "Заваривание чая",
                "Залить заварку кипятком",
                TaskStatus.DONE);
        taskManager.updateSubtask(subtask3Corrected);
        Task task1Corrected = new Task(task1.getId(),
                "Ремонт",
                "Отремонтировать телефон",
                TaskStatus.DONE);
        taskManager.updateTask(task1Corrected);
        Task task2Corrected = new Task(task2.getId(),
                "Сделать уборку в доме",
                "Сделать влажную уборку в доме",
                TaskStatus.NEW);
        taskManager.updateTask(task2Corrected);

        System.out.println("\nПоменяли статусы. Результат:");
        printAllTasks();

        taskManager.removeTask(task1Corrected.getId());
        taskManager.removeEpic(epic1.getId());

        System.out.println("\nУдалили эпик и одну из задач. Результат:");
        printAllTasks();
    }
}


