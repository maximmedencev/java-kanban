public class Main {
    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();

        Task t1 = new Task("Ремонт", "Отремонтировать телефон");

        taskManager.addTask(t1);
        System.out.println(taskManager.getTask(t1.getId()).getDescription());


        Task t2 = new Task("Ремонт", "Отремонтировать планшет");

        taskManager.updateTask(t2);
        System.out.println(taskManager.getTask(t2.getId()).getDescription());


        Epic e1=new Epic("Попить чай","Отвлечься");

        Subtask st1 = new Subtask("Вставание","Надо встать");
        Subtask st2 = new Subtask("Вскипятить воду","Дойти до чайника");


        taskManager.addEpic(e1);
        taskManager.addSubtask(e1.getId(),st1);
        taskManager.addSubtask(e1.getId(),st2);

        Subtask st3 = new Subtask(st1.getId(), "Вставание","Надо встать");
        Subtask st4 = new Subtask(st2.getId(), "Вскипятить воду","Дойти до чайника");
        st3.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(st3);

        System.out.println(e1.getStatus());

        Epic e2=new Epic(e1.getId(), "Попить морс","Отвлечься");


        taskManager.updateEpic(e2);

        System.out.println(taskManager.getEpicSubtasksList(e2.getId()).get(1).getName());

    }
}


