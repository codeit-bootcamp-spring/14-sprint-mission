package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    final List<Channel> channels;
    UserService userService;


    public FileChannelService(UserService userService) {
        this.channels = new ArrayList<>();
        this.userService = userService;
    }

    @Override
    public Channel createChannel(String channelName, User ... members) {
        //멤버 아이디 받아와서 유저 찾은후 그 유저가 유저리스트에 존재하는지 검증
        for(User each : members){
            if (Objects.isNull(userService.readUser(each.getId()))) {
                throw new RuntimeException("존재하지 않는 유저 입니다.");
            }
        }
        //있으면 채널 만듬
        Channel channel = new Channel(channelName, new ArrayList<>(
                List.of(members)
        ));
        channels.add(channel);
        saveChannelFile();
        return channel;
    }

    @Override
    public Channel readChannel(UUID id) {
        for (Channel each : channels) {
            if (each.getId().equals(id)) {
                return each;
            }
        }
        return null;
    }

    @Override
    public List<Channel> readAllChannel() {
        return channels;
    }

    @Override
    public void updateChannel(UUID id, String channelName, User... members) {
        Channel channel = readChannel(id);
        //id로 채널 읽어와서 검증 (readChannel()은 채널이 없으면 null 반환하기 때문)
        if (Objects.isNull(channel)) {
            throw new RuntimeException("해당 채널이 존재하지 않습니다.");
        }
        //채널 널 or 공백인지 확인
        if (channelName != null && !channelName.isBlank()) {
            channel.setChannelName(channelName);
        }
        //멤버리스트가 널이아니고 1명 이상인지 검증
        if (members != null && members.length > 0) {
            for (User each : members) {
                if (Objects.isNull(userService.readUser(each.getId()))) {
                    throw new RuntimeException("존재하지 않는 유저입니다.");
                }
            }
            channel.setMembers(new ArrayList<>(List.of(members)));
        }
        channel.setUpdatedAt();
        saveChannelFile();
    }

    @Override
    public void deleteChannel(UUID id) {
        Channel channel = readChannel(id);
        channels.remove(channel);
        //항상 정상로직 이후에 FileIO 또는 역/직렬화가 이루어져야함 업데이트된 메모리를 기준으로 쓰기때문에
        saveChannelFile();
    }

    private void saveChannelFile() {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("channels.ser"))){
            objectOutputStream.writeObject(channels);
            System.out.println("직렬화 완료: channel.ser");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}