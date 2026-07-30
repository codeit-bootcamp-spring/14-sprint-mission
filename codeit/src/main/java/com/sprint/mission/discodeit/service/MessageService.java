package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

public interface MessageService {
    default void messageCreate(Message massage){
        throw new RuntimeException("이 메세지는 인터페이스 create 내 디폴트 메세지입니다.");
    }
    default Message messageRead(Integer id) {
        throw new RuntimeException("이 메세지는 인터페이스 read 내 디폴트 메세지입니다.");
    }
    default Message messageUpdate(Integer id,String sendMessage, String receiveMessage) {
        throw new RuntimeException("이 메세지는 인터페이스 update 내 디폴트 메세지입니다.");
    }
    default void MessageDelete(Message massage){
        throw new RuntimeException("이 메세지는 인터페이스 delete 내 디폴트 메세지입니다.");
    }}
