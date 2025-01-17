package com.bakuard.pomodoro.dal;

import com.bakuard.pomodoro.model.counter.Counter;
import com.bakuard.pomodoro.model.counter.CounterList;
import com.bakuard.pomodoro.model.exception.RepositoryException;
import com.bakuard.pomodoro.model.repository.CounterListRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

public class CounterListRepositoryFile implements CounterListRepository {

    private final Gson gson;
    private final Path saveFile;
    private List<CounterList> counterLists;

    public CounterListRepositoryFile(Path appDirectory) {
        this.saveFile = appDirectory.resolve("saves").resolve("save.json");
        gson = new GsonBuilder().
                setPrettyPrinting().
                serializeNulls().
                create();
    }

    @Override
    public void save(CounterList counterList) {
        Objects.requireNonNull(counterList, "counterList can't be null");

        List<CounterList> counterLists = loadAll();

        int index = counterLists.indexOf(counterList);
        if(index != - 1) counterLists.set(index, counterList);
        else counterLists.add(counterList);

        try{
            saveAll(counterLists);
        } catch(IOException e) {
            throw new RepositoryException(
                    "Fail to save counterList = " + counterLists,
                    e,
                    "CounterListRepositoryFile.save.fail",
                    false);
        }
    }

    @Override
    public void deleteById(UUID counterListId) {
        loadAll().removeIf(counterList -> counterList.getId().equals(counterListId));
    }

    @Override
    public Optional<CounterList> findById(UUID counterListId) {
        return loadAll().stream().
                filter(counterList -> counterList.getId().equals(counterListId)).
                findFirst();
    }

    @Override
    public Stream<CounterList> findAll() {
        return loadAll().stream();
    }

    @Override
    public long count() {
        return loadAll().size();
    }


    private void createFileIfNotExists() throws IOException {
        if(!Files.exists(saveFile)) {
            Files.createDirectories(saveFile.getParent());
            Files.createFile(saveFile);
        }
    }

    private boolean isFileEmpty() {
        File file = saveFile.toFile();
        return file.length() == 0;
    }

    private void saveAll(List<CounterList> counterLists) throws IOException {
        try(JsonWriter writer = gson.newJsonWriter(
                new OutputStreamWriter(new FileOutputStream(saveFile.toFile()), StandardCharsets.UTF_8))) {
            writer.beginArray();
            for(CounterList counterList : counterLists) {
                writer.beginObject().
                    name("id").value(counterList.getId().toString()).
                    name("activeCounterId").value(counterList.getActiveCounter().
                                map(c -> c.getId().toString()).orElse(null)).
                    name("counters").beginArray();
                for(Counter counter : counterList.stream().toList()) {
                    writer.beginObject().
                        name("id").value(counter.getId().toString()).
                        name("name").value(counter.getName()).
                        name("currentTotalSeconds").value(counter.getCurrentTotalSeconds()).
                        name("initTotalSeconds").value(counter.getInitTotalSeconds()).
                        endObject();
                }
                writer.endArray();
                writer.endObject();
            }
            writer.endArray();
        }
    }

    private List<CounterList> loadAll() {
        if(counterLists != null) return counterLists;
        else counterLists = new ArrayList<>();

        try {
            createFileIfNotExists();

            if(!isFileEmpty()) {
                try (JsonReader reader = gson.newJsonReader(
                        new InputStreamReader(new FileInputStream(saveFile.toFile()), StandardCharsets.UTF_8))) {
                    if (reader.hasNext()) {
                        reader.beginArray();
                        while (reader.hasNext()) counterLists.add(loadCounterList(reader));
                        reader.endArray();
                    }
                }
            }

            return counterLists;
        } catch(IOException e) {
            throw new RepositoryException(
                    "Fail to load all counterList",
                    e,
                    "CounterListRepositoryFile.loadAll.fail",
                    false);
        }
    }

    private CounterList loadCounterList(JsonReader reader) throws IOException {
        UUID id = null, activeCounterId = null;
        List<Counter> counters = null;

        reader.beginObject();
        while(reader.hasNext()) {
            String name = reader.nextName();
            switch(name) {
                case "id" -> id = UUID.fromString(reader.nextString());
                case "activeCounterId" -> {
                    if(reader.peek() != JsonToken.NULL) activeCounterId = UUID.fromString(reader.nextString());
                    else reader.skipValue();
                }
                case "counters" -> counters = loadCounters(reader);
            }
        }
        reader.endObject();

        return new CounterList(id, counters, activeCounterId);
    }

    private List<Counter> loadCounters(JsonReader reader) throws IOException {
        ArrayList<Counter> counters = new ArrayList<>();

        reader.beginArray();
        while(reader.hasNext()) {
            UUID id = null;
            String counterName = null;
            long currentTotalSeconds = 0, initTotalSeconds = 0;

            reader.beginObject();
            while(reader.hasNext()) {
                String name = reader.nextName();
                switch (name) {
                    case "id" -> id = UUID.fromString(reader.nextString());
                    case "name" -> counterName = reader.nextString();
                    case "currentTotalSeconds" -> currentTotalSeconds = reader.nextLong();
                    case "initTotalSeconds" -> initTotalSeconds = reader.nextLong();
                }
            }
            reader.endObject();

            counters.add(new Counter(id, counterName, currentTotalSeconds, initTotalSeconds));
        }
        reader.endArray();

        return counters;
    }

}
