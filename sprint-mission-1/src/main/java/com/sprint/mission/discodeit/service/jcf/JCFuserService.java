package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFuserRepository;
import com.sprint.mission.discodeit.repository.userRepository;

import java.util.Scanner;

public class JCFuserService extends JCFuserRepository implements userRepository {
    Scanner sc = new Scanner(System.in);

    @Override
    public void allPrintUser() {
        for (User user:super.users){
            System.out.printf("사용자의 이름: %s, 역할: %s 입니다.\n", user.getUserName(), user.getRole());
        }
    }

    @Override
    public void printUser() {
        System.out.print("자세히 보고자 하는 사용자의 이름을 말해주세요: ");
        String userName = sc.next();
        User user = findByUser(userName);

        System.out.println(user.toString());
    }

    @Override
    public User selectedUser() {
        allPrintUser();
        System.out.print("선택할 사용자의 이름을 말해주세요: ");
        String userName = sc.next();
        User user = findByUser(userName);

        if (user != null){
            return user;
        }

        System.out.println("사용자를 찾을 수 없습니다.");
        return null;
    }
}
