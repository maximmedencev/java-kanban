package ru.yandex.practicum.tasktracker.http;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import ru.yandex.practicum.tasktracker.Epic;
import ru.yandex.practicum.tasktracker.Subtask;
import ru.yandex.practicum.tasktracker.Task;

import java.lang.reflect.Type;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class JsonResponseDeserialize implements JsonDeserializer<List<? extends Task>> {
    public static Type type = new TypeToken<List<? extends Task>>() {
    }.getType();

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    @Override
    public List<? extends Task> deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        List<Task> listToReturn = new ArrayList<>();
        JsonArray jsonArray = element.getAsJsonArray();
        for (JsonElement jsonElement : jsonArray) {
            Task task;
            if (jsonElement.getAsJsonObject().get("epicId") != null) {
                task = gson.fromJson(jsonElement, Subtask.class);
                if (task != null) {
                    listToReturn.add(task);
                    continue;
                }
            }
            if (jsonElement.getAsJsonObject().get("subtasksIds") != null) {
                task = gson.fromJson(jsonElement, Epic.class);
                if (task != null) {
                    listToReturn.add(task);
                    continue;
                }
            }
            if (jsonElement.getAsJsonObject().get("epicId") == null && jsonElement.getAsJsonObject().get("subtasksIds") == null) {
                task = gson.fromJson(jsonElement, Task.class);
                if (task != null) {
                    listToReturn.add(task);
                }
            }
        }
        return listToReturn;
    }
}

