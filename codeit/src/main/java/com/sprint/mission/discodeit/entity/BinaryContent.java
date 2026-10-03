package com.sprint.mission.discodeit.entity;

import java.time.Instant;
import java.util.UUID;

public class BinaryContent extends BaseEntity{

    protected BinaryContent(UUID id, Integer userNum) {
        super(id, userNum);
    }

    @Override
    protected void updated(Integer userNum) {
        throw new RuntimeException("수정 불가능한 도메인입니다.");
    }
}
