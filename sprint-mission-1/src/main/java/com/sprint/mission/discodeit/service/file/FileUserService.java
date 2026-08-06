package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.NameExistsException;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Scanner;

public class FileUserService implements UserService {
    private final FileUserRepository fileuserRepository = new FileUserRepository();
    private final FileChannelRepository filechannelRepository = new FileChannelRepository();
    private final Scanner sc = new Scanner(System.in);


//    public void userNameAdd(String channelName) {
//        List<String> userName = fileuserRepository.readUserName();
//
//        for (String name : userName) {
//            if (filechannelRepository.findByChannel(channelName).isPresent()&&fileuserRepository.findByUser(name).isEmpty()) {
//                System.out.println("유저 생성이 완료되었습니다: " + name);
//                fileuserRepository.userAdd(new User(name, filechannelRepository.findByChannel(channelName).get()));
//            }
//        }
//    }

    @Override
    public User userCreate(String channelName, String userName) {
        if (fileuserRepository.findByUser(userName).isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 유저입니다: " + userName);
        }
        Channel channel = filechannelRepository.findByChannel(channelName)
                .orElseThrow(() -> new IllegalArgumentException("채널이 없습니다: " + channelName));

        User user = new User(userName, channel);
        return fileuserRepository.userAdd(user);
    }

    @Override
    public void userUpdate(String userName, String updateUserName) {
        if (fileuserRepository.findByUser(updateUserName).isPresent()){
            throw NameExistsException.ofUser(updateUserName);
        }

        User user = fileuserRepository.findByUser(userName)
                .orElseThrow(() -> new IllegalArgumentException("수정할 이름이 없습니다: " + userName));

        user.updateName(updateUserName);
        System.out.println(userName+" 유저의 이름을 "+updateUserName+"으로 수정했습니다.");

        fileuserRepository.userAdd(user);
    }

    @Override
    public void userDelete(String userName) {
        User user = fileuserRepository.findByUser(userName)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 유저가 없습니다: " + userName));

        System.out.println(userName+" 유저를 삭제했습니다.");
        fileuserRepository.delete(user);
    }

    @Override
    public List<User> allPrintUser() {
        return fileuserRepository.findAllUser();
    }

    @Override
    public void printUser(String userName) {
        User user = fileuserRepository.findByUser(userName)
                .orElseThrow(() -> new IllegalArgumentException("보고자 하는 유저가 없습니다: " + userName));

        System.out.printf("유저 이름: %s, 유저 아이디: %s \n", user.getUserName(),user.getUserId());
    }
}
