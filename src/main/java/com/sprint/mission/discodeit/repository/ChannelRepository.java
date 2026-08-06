package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository {
    //메모리 업데이트 반환값이 필요없다고 생각함
    public abstract void save(Channel channel);

    //메모리에서 id로 읽어오기
    public abstract Channel findById(UUID id);

    //채널 목록 가져오기
    public abstract List<Channel> findAll();

    //메모리에서 채널 지우기
    public abstract void delete(UUID id);

    //TODO: 지금 이상한점이 repo와 service를 분리하면 원래 이렇게 레포는 다 코드가 똑같아지는가? 한번 서비스 구현해보고 다시 확인해보기
}
