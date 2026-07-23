package com.sprint.mission.discodeit.repository.file;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class MapFileIO<T> {
    protected final File file;

    protected MapFileIO(File file) {
        this.file = file;
    }

    protected final Map<UUID, T> readFile() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            Map<UUID, T> retrieved = (Map<UUID, T>) inputStream.readObject();
            return new HashMap<>(retrieved);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    protected final Map<UUID, T> writeFile(Map<UUID, T> toBeSaved) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject(toBeSaved);
            return toBeSaved;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
