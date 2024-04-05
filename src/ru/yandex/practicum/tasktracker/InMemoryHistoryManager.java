package ru.yandex.practicum.tasktracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private int size = 0;
    private final Map<Integer, Node> history = new HashMap<>();

    private void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;
        size++;
    }

    private void removeNode(Node node) {

        if (node == null || size == 0)
            return;

        if (size == 1) {
            head = null;
            tail = null;
            size = 0;
            return;
        }

        if (node == head) {
            head = head.next;
            head.prev = null;
            node = null;
            size--;
            return;
        }

        if (node == tail) {
            tail = tail.prev;
            tail.next = null;
            node = null;
            size--;
            return;
        }

        node.prev.next = node.next;
        node.next.prev = node.prev;
        node = null;
        size--;

    }

    @Override
    public void remove(int id) {
        removeNode(history.get(id));
        history.remove(id);
    }

    @Override
    public void add(Task task) {
        if (history.containsKey(task.getId())) {
            removeNode(history.get(task.getId()));
        }
        linkLast(task);
        history.put(task.getId(), tail);
    }

    @Override
    public List<Task> getHistory() {

        List<Task> listToReturn = new ArrayList<>();
        Node currentNode = head;
        while (currentNode != null) {
            listToReturn.add(currentNode.data);
            currentNode = currentNode.next;
        }

        return listToReturn;
    }
}
