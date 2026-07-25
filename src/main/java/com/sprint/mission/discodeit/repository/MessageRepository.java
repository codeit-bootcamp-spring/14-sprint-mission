package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    public abstract void save(Message message);

    //메모리에서 메세지 단건 id로 읽어오기
    public abstract Message findById(UUID id);

    //메세지 목록 가져오기
    // TODO:근데 이거 메세지 목록 가져올떄 sender도 가져와야할거 같은데 이걸 repo에서 처리? 아니면 서비스단에서 처리? 지금 생각해보니 레포에서 목록만 가져와서 서비스단에서 for문으로 하나하나 getSender로 옆에 붙혀주는게 맞는듯??
    public abstract List<Message> findAll();

    //메모리에서 메세지 지우기
    public abstract void delete(UUID id);
}
