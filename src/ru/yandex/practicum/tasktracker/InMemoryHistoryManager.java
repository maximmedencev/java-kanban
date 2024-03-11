package ru.yandex.practicum.tasktracker;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (history.size() == 10)
            history.remove(0);

        if (task instanceof Subtask) {
            history.add(new Subtask((Subtask) task));
        } else if (task instanceof Epic) {
            history.add(new Epic((Epic) task));
        } else {
            history.add(new Task(task));
        }

    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
