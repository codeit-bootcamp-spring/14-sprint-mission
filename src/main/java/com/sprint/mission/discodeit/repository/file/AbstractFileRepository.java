package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.common.exception.CustomException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.domain.common.BasicEntity;
import com.sprint.mission.discodeit.repository.CrudRepository;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public abstract class AbstractFileRepository<T extends BasicEntity> implements CrudRepository<T> {

    protected final File file;
    protected final Map<UUID, T> EMPTY_BUFFER = new ConcurrentHashMap<>();
    protected Map<UUID, T> buffer;

    protected AbstractFileRepository(File file) {
        createParentDirectoryIfAbsent(file);
        this.file = file;
        this.buffer = Optional.of(file)
                .filter(f -> file.exists() && file.length() != 0)
                .map(f -> readFile())
                .orElseGet(() -> writeFile(EMPTY_BUFFER));
//        this.buffer = writeFile(EMPTY_BUFFER); // test를 위해 실행시마다 빈 파일로 초기화
    }

    private void createParentDirectoryIfAbsent(File file) {
        File parent = file.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new CustomException(ExceptionType.FILE_IO_FAILED);
        }
    }

    @Override
    public T create(T t) {
        UUID id = t.getId();
        return findById(id).orElseGet(() -> {
                buffer.put(id, t);
                writeFromBufferToFile();
                return t;
        });
    }

    @Override
    public Optional<T> findById(UUID id) {
        return Optional.ofNullable(buffer.get(id));
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(buffer.values());
    }

    @Override
    public T deleteById(UUID id) {
        T toBeRemoved = buffer.get(id);
        buffer.remove(id);
        writeFromBufferToFile();
        return toBeRemoved;
    }

    @Override
    public boolean existsById(UUID id) {
        return buffer.values().stream()
                .anyMatch(t -> t.getId().equals(id));
    }

    private Map<UUID, T> readFile() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            Map<UUID, T> retrieved = (Map<UUID, T>) inputStream.readObject();
            return new HashMap<>(retrieved);
        } catch (IOException | ClassNotFoundException e) {
            throw new CustomException(ExceptionType.FILE_IO_FAILED);
        }
    }

    /**
     * buffer의 내용을 file에 덮어쓴다.
     * repository의 신규 데이터가 생성되거나, 기존 데이터가 삭제/수정되면 이 메서드가 호출되어야 한다.
     */
    protected final void writeFromBufferToFile() {
        this.writeFile(buffer);
    }

    private Map<UUID, T> writeFile(Map<UUID, T> toBeSaved) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject(toBeSaved);
            return toBeSaved;
        } catch (IOException e) {
            throw new CustomException(ExceptionType.FILE_IO_FAILED);
        }
    }
}
