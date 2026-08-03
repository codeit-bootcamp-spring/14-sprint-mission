package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.common.BasicEntity;
import com.sprint.mission.discodeit.repository.CrudRepository;

import java.io.*;
import java.util.*;

public abstract class MapFileRepository<T extends BasicEntity> implements CrudRepository<T> {
    protected final File file;
    protected Map<UUID, T> buffer;

    protected final Map<UUID, T> EMPTY_BUFFER = new HashMap<>();

    protected MapFileRepository(File file) {
        this.file = file;
        initBuffer();
    }

    private void initBuffer() {
        this.buffer = Optional.of(file)
                .filter(file -> file.exists() && file.length() != 0)
                .map(file -> this.readFile())
                .orElseGet(() -> this.writeFile(EMPTY_BUFFER));
    }

    @Override
    public final T create(T t) {
        UUID id = t.getId();
        return findById(id).orElseGet(() -> {
            buffer.put(id, t);
            this.writeFile(buffer);
            return t;
        });
    }

    @Override
    public final Optional<T> findById(UUID id) {
        readFromFileToBuffer();
        return Optional.ofNullable(buffer.get(id));
    }

    @Override
    public final List<T> findAll() {
        readFromFileToBuffer();
        return new ArrayList<>(buffer.values());
    }

    @Override
    public final void deleteById(UUID id) {
        readFromFileToBuffer();
        findById(id).ifPresent(retrieved -> {
            buffer.remove(id);
            writeFromBufferToFile();
        });
    }

    private Map<UUID, T> readFile() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            Map<UUID, T> retrieved = (Map<UUID, T>) inputStream.readObject();
            return new HashMap<>(retrieved);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected final void readFromFileToBuffer() {
        this.buffer = readFile();
    }

    private Map<UUID, T> writeFile(Map<UUID, T> toBeSaved) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject(toBeSaved);
            return toBeSaved;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected final void writeFromBufferToFile() {
        this.writeFile(buffer);
    }
}
