package com.sprint.mission.discodeit.storage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 도메인 하나를 파일 한 개에 통째로 직렬화해서 읽고 쓴다.
 * FileIO는 어느 도메인이든 같은 코드라 여기 한 곳에만 둔다.
 */
public final class FileStore<T extends Serializable> {

    private final Path path;

    public FileStore(Path path) {
        this.path = path;
        Path parent = path.getParent();
        if (parent != null) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                throw new UncheckedIOException("저장 디렉터리를 만들 수 없습니다: " + parent, e);
            }
        }
    }

    /** 파일이 없으면 빈 map을 준다. 첫 실행에서 예외가 나지 않도록. */
    @SuppressWarnings("unchecked")
    public Map<UUID, T> load() {
        if (Files.notExists(path)) {
            return new LinkedHashMap<>();
        }
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(path))) {
            return (Map<UUID, T>) in.readObject();
        } catch (IOException e) {
            throw new UncheckedIOException("불러오기에 실패했습니다: " + path, e);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("저장된 데이터를 해석할 수 없습니다: " + path, e);
        }
    }

    public void save(Map<UUID, T> data) {
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(path))) {
            out.writeObject(new LinkedHashMap<>(data));
        } catch (IOException e) {
            throw new UncheckedIOException("저장에 실패했습니다: " + path, e);
        }
    }

    /** 같은 조건에서 다시 시작하려고 비울 때 쓴다. */
    public void clear() {
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new UncheckedIOException("초기화에 실패했습니다: " + path, e);
        }
    }
}
