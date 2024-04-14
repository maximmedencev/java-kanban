package ru.yandex.practicum.tasktracker;

import java.io.File;

public class Managers {
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTaskManager loadFromFile(File file){
        return  new FileBackedTaskManager(file);

    }
}
