package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.NameExistsException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Scanner;

public class BasicUserService implements UserService {
    private final UserRepository userRepository ;
    private final ChannelRepository channelRepository;

    public BasicUserService(UserRepository userRepository, ChannelRepository channelRepository) {
        this.userRepository = userRepository;
        this.channelRepository = channelRepository;
    }

    @Override
    public User userCreate(String channelName, String userName) {
        if (userRepository.findByUser(userName).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 유저입니다: " + userName);
        }
        Channel channel = channelRepository.findByChannel(channelName)
                .orElseThrow(() -> new IllegalArgumentException("채널이 없습니다: " + channelName));

        User user = new User(userName, channel);
        return userRepository.userAdd(user);
    }

    @Override
    public void userUpdate(String userName, String updateUserName) {
        if (userRepository.findByUser(updateUserName).isPresent()){
            throw NameExistsException.ofUser(updateUserName);
        }

        User user = userRepository.findByUser(userName)
                .orElseThrow(() -> new IllegalArgumentException("수정할 이름이 없습니다: " + userName));

        user.updateName(updateUserName);
        System.out.println(userName+" 유저의 이름을 "+updateUserName+"으로 수정했습니다.");
    }

    @Override
    public void userDelete(String userName) {
        User user = userRepository.findByUser(userName)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 유저가 없습니다: " + userName));

        System.out.println(userName+" 유저를 삭제했습니다.");
        userRepository.delete(user);
    }

    @Override
    public List<User> allPrintUser() {
        return userRepository.findAllUser();
    }

    @Override
    public void printUser(String userName) {
        User user = userRepository.findByUser(userName)
                .orElseThrow(() -> new IllegalArgumentException("보고자 하는 유저가 없습니다: " + userName));

        System.out.printf("유저 이름: %s, 유저 아이디: %s \n", user.getUserName(),user.getUserId());
    }
}
