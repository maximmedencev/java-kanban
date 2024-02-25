public class Main {
    public static TaskManager taskManager = new TaskManager();

    public static void printAll(){
        System.out.println("------------------------------------------------------------------");
        System.out.println(taskManager.getTasksList());
        System.out.println(taskManager.getEpicsList());
        System.out.println(taskManager.getSubTasksList());
    }
    public static void main(String[] args) {



        Task task1 = new Task("Ремонт", "Отремонтировать телефон");
        taskManager.addTask(task1);

        Task task2 = new Task("Сделать уборку в доме", "Сделать влажную уборку в доме");
        taskManager.addTask(task2);


        Epic epic1 = new Epic("Написать песню","Записать инструментальную композицию");
        taskManager.addEpic(epic1);
        Subtask subtask1 = new Subtask("Записать ритм-гитару","Сочинить и записать партию ритм-гитары");
        taskManager.addSubtask(epic1.getId(), subtask1);
        Subtask subtask2 = new Subtask("Записать бас-гитару","Сочинить и записать партию бас-гитары");
        taskManager.addSubtask(epic1.getId(), subtask2);

        Epic epic2=new Epic("Заварить чай","Отвлечься");
        taskManager.addEpic(epic2);
        Subtask subtask3 = new Subtask("Заваривание чая","Залить заварку кипятком");
        taskManager.addSubtask(epic2.getId(), subtask3);

        printAll();

        subtask1.setStatus(TaskStatus.IN_PROGRESS);
        subtask2.setStatus(TaskStatus.DONE);
        subtask3.setStatus(TaskStatus.DONE);

        taskManager.updateSubtask(subtask1);
        taskManager.updateSubtask(subtask2);
        taskManager.updateSubtask(subtask3);

        printAll();

        taskManager.removeTask(task1.getId());
        taskManager.removeEpic(epic1.getId());

        printAll();

    }

}


