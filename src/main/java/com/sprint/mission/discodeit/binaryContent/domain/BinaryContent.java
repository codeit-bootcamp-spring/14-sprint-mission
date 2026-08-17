package com.sprint.mission.discodeit.binaryContent.domain;

import com.sprint.mission.discodeit.common.exception.NoSuchElementException;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
public class BinaryContent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private UUID id;
    private byte[] data;
    private Instant createdAt;

    public BinaryContent(byte[] data) {
        if(data == null){
            throw new NoSuchElementException();
        }
        this.data = data;

        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();

    }

}
