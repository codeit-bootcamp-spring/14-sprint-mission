package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository {
    void save(Channel channel); // save 같이 쓰면
    //update 만들고 save 불러오는 방식으로
    Optional<Channel> findById(UUID id); // Optional 로 감싸기
    List<Channel> findAll();
    void deleteById(UUID id);

}
// 업데이트 가읎어