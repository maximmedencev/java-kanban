package ru.yandex.practicum.tasktracker;

public class Managers {
    public TaskManager getDefault(){
      return new InMemoryTaskManager();
    }
}
