package com.sprint.mission.discodeit.file.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.NameExistsException;
import com.sprint.mission.discodeit.file.repository.file.FilechannelRepository;
import com.sprint.mission.discodeit.file.repository.file.FileuserRepository;
import com.sprint.mission.discodeit.file.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileuserService implements UserService {
    private final FileuserRepository fileuserRepository = new FileuserRepository();
    private final FilechannelRepository filechannelRepository = new FilechannelRepository();
    private final Scanner sc = new Scanner(System.in);

    @Override
    public void userInit() {
        fileuserRepository.userLoad();
    }

    @Override
    public void userCreate(String channelName) {
        List<String> userName = fileuserRepository.readUserName();
        List<User> newUsers = new ArrayList<>();

        for (String name : userName) {
            if (filechannelRepository.findByChannel(channelName).isPresent()&&fileuserRepository.findByUser(name).isEmpty()) {
                System.out.println("유저 생성이 완료되었습니다: " + name);
                newUsers.add(new User(name, filechannelRepository.findByChannel(channelName).get()));
            }
        }
        fileuserRepository.userAdd(newUsers);
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

        fileuserRepository.userFlush();
    }

    @Override
    public void userDelete(String userName) {
        User user = fileuserRepository.findByUser(userName)
                .orElseThrow(() -> new IllegalArgumentException("삭제할 유저가 없습니다: " + userName));

        System.out.println(userName+" 유저를 삭제했습니다.");
        fileuserRepository.delete(user);
        fileuserRepository.userFlush();
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
