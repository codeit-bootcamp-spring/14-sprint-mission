package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JCFMessageService implements MessageService {
    private Map<UUID, Message> messageMap = new HashMap<>();

    @Override
    public void messageCreate(Message message){
        if(messageMap.containsKey(message.getId())){
            throw new RuntimeException("생성하시려는 유저가 이미 있습니다." + message.getId());
        }
        messageMap.put(message.getId(),message);
        System.out.println("유저를 생성했습니다.");
    }

    @Override
    public Message messageRead(Integer id) {
        if(!messageMap.containsKey(id)) {
            throw new RuntimeException("읽으시려는 메세지의 아이디가 없습니다.");
        }
        return messageMap.get(id);
    }

    @Override
    public Message messageUpdate(Integer id,String sendMessage, String receiveMessage){
        if(!messageMap.containsKey(id)) {
            throw new RuntimeException("업데이트 할려는 유저가 없습니다");
        }
        Message message = this.messageMap.get(id);
        message.update(sendMessage, receiveMessage);
        return messageMap.get(id);
    }

    @Override
    public void MessageDelete(Message message){
        if(!messageMap.containsKey(message.getId())){
            throw new RuntimeException("삭제하려는 유저가 없습니다");
        }
        messageMap.remove(message.getId());
    }
}
