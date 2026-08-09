package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.List;
import java.util.UUID;

public interface BinaryContentRepository {
    //메모리 업데이트 반환값이 필요없다고 생각함
    void save(BinaryContent binaryContent);

    //메모리에서 id로 컨텐츠 가져오기
    BinaryContent findById(UUID id);
    // userId로 컨텐츠 가져오기
    BinaryContent findByUserId(UUID userId);

    List<BinaryContent> findAllByMessageId(UUID messageId);

    //userid랑 channelid로 해당 유저가 해당채널에서 마지막 읽기상태를 가져옴
    BinaryContent findByUserIdAndChannelId(UUID userId, UUID channelId);

    //컨텐츠 목록 가져오기
    List<BinaryContent> findAll();

    //메모리에서 컨텐츠 지우기
    void delete(UUID id);
}
