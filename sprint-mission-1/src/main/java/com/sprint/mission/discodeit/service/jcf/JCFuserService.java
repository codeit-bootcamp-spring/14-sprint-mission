package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFchannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFuserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Scanner;

public class JCFuserService implements UserService {
    private final JCFuserRepository jcFuserRepository = new JCFuserRepository();
    private final JCFchannelRepository jcFchannelRepository = new JCFchannelRepository();
    private final Scanner sc = new Scanner(System.in);

    @Override
    public User userCreate(String channelName, String userName) {
        if (jcFuserRepository.findByUser(userName).isPresent()){
            throw new IllegalArgumentException("이미 생성된 유저의 이름입니다: "+userName);
        }

        Channel channel = jcFchannelRepository.findByChannel(channelName)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다: " + channelName));

        System.out.println("유저 생성이 완료되었습니다: "+userName);
        User user = new User(userName, channel);

        return jcFuserRepository.userAdd(user);
    }

    @Override
    public void userUpdate(String userName, String updateUserName) {
        User user = jcFuserRepository.findByUser(userName)
                .orElseThrow(() -> new IllegalArgumentException("수정할 유저가 없습니다: " + userName));


        user.updateName(updateUserName);
        System.out.println(userName+" 유저의 이름을 "+updateUserName+"로 수정했습니다.");
    }

    @Override
    public void userDelete(String userName) {
        User user = jcFuserRepository.findByUser(userName)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 유저가 없습니다: " + userName));

        System.out.println(userName+" 유저를 삭제했습니다.");
        jcFuserRepository.delete(user);
    }

    @Override
    public List<User> allPrintUser() {
        return jcFuserRepository.findAllUser();
    }

    @Override
    public void printUser(String userName) {
        User user = jcFuserRepository.findByUser(userName)
                .orElseThrow(() -> new IllegalArgumentException("보고자 하는 유저가 없습니다: " + userName));

        System.out.printf("유저 이름: %s, 유저 아이디: %s \n", user.getUserName(),user.getUserId());
    }
}
