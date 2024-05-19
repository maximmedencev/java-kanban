package ru.yandex.practicum.tasktracker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    private Node head;
    private Node tail;
    private final Map<Integer, Node> history = new HashMap<>();

    private void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(oldTail, task, null);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.setNext(newNode);
    }

    private void removeNode(Node node) {

        if (node == null || history.isEmpty())
            return;

        final Node next = node.getNext();
        final Node prev = node.getPrev();

        if (prev == null) {
            head = next;
        } else {
            prev.setNext(next);
            node.setNext(null);
        }

        if (next == null) {
            tail = prev;
        } else {
            next.setPrev(prev);
            node.setNext(null);
        }
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
            listToReturn.add(currentNode.getData());
            currentNode = currentNode.getNext();
        }

        return listToReturn;
    }
}
