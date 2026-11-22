package com.sprint.mission.discodeit.user.mapper;

import com.sprint.mission.discodeit.binarycontent.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.user.dto.UserDto;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.service.UserOnlineChecker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

// online 계산에 Bean이 필요해서 interface 대신 abstract class로 만든다 (MapStruct는 생성자 주입 불가)
@Mapper(componentModel = "spring", uses = BinaryContentMapper.class)
public abstract class UserMapper {

    @Autowired
    protected UserOnlineChecker onlineChecker;

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "online", expression = "java(onlineChecker.isOnline(user.getId()))")
    public abstract UserDto toDto(User user);
}
