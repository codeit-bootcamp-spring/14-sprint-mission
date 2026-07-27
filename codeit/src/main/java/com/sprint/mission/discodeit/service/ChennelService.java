package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Chennel;
import com.sprint.mission.discodeit.entity.User;

public interface ChennelService {
    default void create(Chennel chennel){
        throw new RuntimeException("이 메세지는 인터페이스 create 내 디폴트 메세지입니다.");
    }
    default Chennel read(Chennel chennel ){
        throw new RuntimeException("이 메세지는 인터페이스 read 내 디폴트 메세지입니다.");
    }default void ChennelUpdate(Chennel chennel, String channelName, int chennelNumber){
        throw new RuntimeException("이 메세지는 인터페이스 update 내 디폴트 메세지입니다.");
    }
    default void delete(Chennel chennel){
        throw new RuntimeException("이 메세지는 인터페이스 delete 내 디폴트 메세지입니다.");
    }
}
