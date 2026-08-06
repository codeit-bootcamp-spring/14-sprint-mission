package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
    // 의존성 주입 패턴
    private final ChannelRepository channelRepository;
    private final UserService userService;
    public BasicChannelService(ChannelRepository channelRepository, UserService userService) {
        this.channelRepository = channelRepository;
        this.userService = userService;
    }

    @Override
    public Channel createChannel(String channelName, User... members) {
        //이 로직이 멤버가 유저리스트에 존재하는지 검증하는 로직
        //TODO: 근데 이거 그냥 멤버가 생성될떄 userlist에 존재하는지 검증하면 되는거 아닌가?(질문)
        for (User each : members) {
            if (Objects.isNull(userService.readUser(each.getId()))) {
                throw new RuntimeException("존재하지 않는 유저");
            }
        }
        Channel channel = new Channel(channelName, new ArrayList<>(List.of(members)));
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Channel readChannel(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> readAllChannel() {
        return channelRepository.findAll();
    }

    //TODO: 나중에 그냥 3개로 분리하기(채널명만 변경, 멤버만 변경, 둘다변경 메서드로 오버로드)
    @Override
    public void updateChannel(UUID id, String channelName, User... members) {
        Channel target = readChannel(id);
        //id로 채널 읽어와서 검증
        if (Objects.isNull(target)) {
            throw new RuntimeException("해당 채널이 존재하지 않습니다.");
        }
        // 파라미터로 받는 채널명이 널 or 공백인지 확인
        if (Objects.isNull(channelName) || channelName.isBlank()) {
            throw new RuntimeException("유효하지 않은 채널명 입니다.");
        }
        target.setChannelName(channelName);

        // 멤버리스트가 널이아니고 1명 이상인지 검증
        if (Objects.isNull(members) || members.length < 1) {
            throw new RuntimeException("유효하지 않은 유저입니다.");
        }
        for (User each : members) {
            if (Objects.isNull(userService.readUser(each.getId()))) {
                throw new RuntimeException("존재하지 않는 유저입니다.");
            }
        }
        target.setMembers(new ArrayList<>(List.of(members)));
        target.setUpdatedAt();
        channelRepository.save(target);
    }

    @Override
    public void deleteChannel(UUID uuid) {
        channelRepository.delete(uuid);
    }
}
