package com.sprint.mission.discodeit.channel.repository;

import com.sprint.mission.discodeit.channel.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {
//    void save(Channel channel); // save 같이 쓰면
    //update 만들고 save 불러오는 방식으로
//    Optional<Channel> findById(UUID id); // Optional 로 감싸기
//    List<Channel> findAll();
//    void deleteById(UUID id);

}
