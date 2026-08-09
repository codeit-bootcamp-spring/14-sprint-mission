package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusRepository {
    //메모리 업데이트 반환값이 필요없다고 생각함
    void save(ReadStatus readStatus);

    //메모리에서 id로 읽기상태 읽어오기
    ReadStatus findById(UUID id);

    //읽기상태 목록 가져오기
    List<ReadStatus> findAll();

    List<ReadStatus> findAllByChannelId(UUID channelId);

    //메모리에서 읽기상태 지우기
    void delete(UUID id);
}
