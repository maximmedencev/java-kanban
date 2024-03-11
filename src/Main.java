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
        Epic epic2 = new Epic("Заварить чай", "Отвлечься");
        inMemoryTaskManager.addEpic(epic2);
        Subtask subtask3 = new Subtask("Заваривание чая",
                "Залить заварку кипятком",
                TaskStatus.NEW);
        inMemoryTaskManager.addSubtask(epic2.getId(), subtask3);


        //-----Для проверки getHistory()----------------
        inMemoryTaskManager.getEpic(epic2.getId());
        for(int i=0; i<9; i++){
            //subtask3.setDescription(subtask3.getDescription()+"1");
            //inMemoryTaskManager.getSubtask(subtask3.getId());
            inMemoryTaskManager.getEpic(epic2.getId());
        }

        printAllTasks(inMemoryTaskManager);

    }
}


