package ru.yandex.practicum.tasktracker.http;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import ru.yandex.practicum.tasktracker.FileBackedTaskManager;

import java.io.IOException;
import java.time.LocalDateTime;

class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {

    @Override
    public void write(JsonWriter jsonWriter, LocalDateTime localDateTime) throws IOException {
        if (localDateTime != null) {
            jsonWriter.value(localDateTime.format(FileBackedTaskManager.DATE_TIME_FORMATTER));
        } else {
            jsonWriter.value(LocalDateTime.now().format(FileBackedTaskManager.DATE_TIME_FORMATTER));
        }
    }

    @Override
    public LocalDateTime read(JsonReader jsonReader) throws IOException {
        return LocalDateTime.parse(jsonReader.nextString(), FileBackedTaskManager.DATE_TIME_FORMATTER);
    }
}