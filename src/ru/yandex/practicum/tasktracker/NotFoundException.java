package ru.yandex.practicum.tasktracker;

public class NotFoundException extends Exception {
    public NotFoundException(final String message) {
        super(message);
    }
}