package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.Category;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.enumType.StatusType;
import com.sprint.mission.discodeit.enumType.userRole;
import com.sprint.mission.discodeit.repository.userRepository;
import com.sprint.mission.discodeit.service.jcf.JCFuserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.sprint.mission.discodeit.controller.ChatController.chatContorller;
import static com.sprint.mission.discodeit.controller.ConsoleController.SC;
import static com.sprint.mission.discodeit.controller.ConsoleController.consoleContorller;

public class UserContorller {
    private static final userRepository USER = new JCFuserService();
    private static userRole role;

/*    public static final Map<String, User> USERS = new HashMap<>(Map.of(
            "user1", USER.userCreate("김예준", role.ADMINISTRATOR.getRoleName()),
            "user2", USER.userCreate("조윤우", role.ADMINISTRATOR.getRoleName()),
            "user3", USER.userCreate("이서준", role.MANAGER.getRoleName()),
            "user4", USER.userCreate("박도윤", role.MANAGER.getRoleName()),
            "user5", USER.userCreate("최시우", role.TEAMLEADER.getRoleName()),
            "user6", USER.userCreate("정하준", role.TEAMLEADER.getRoleName()),
            "user7", USER.userCreate("강주원", role.JUNIOR.getRoleName()),
            "user8", USER.userCreate("윤준서", role.JUNIOR.getRoleName())
    ));*/

    public static void userContorller(){
        System.out.println("--- 아래 메뉴에서 기능을 선택하세요 ---");
        System.out.print("--------------------\n" +
                " 1. 유저 선택\n" +
                " 2. 유저 출력\n" +
                " 3. 역할 출력\n" +
                " 4. 유저 생성\n" +
                " 5. 유저 수정\n" +
                " 6. 유저 삭제\n" +
                " 7. 서버 다시 선택하기\n" +
                "--------------------\n 숫자를 입력하세요: ");

        String input = SC.next();
        switch (input){
            case "1" -> {
                USER.allPrintUser();
                System.out.print("선택할 유저의 이름을 입력하세요: ");
                // 선택 시 채팅방에서 그 유저 이름으로 채팅 가능
                String userName = SC.next();
                User user = USER.findByUser(userName);
                if (user == null){
                    userContorller();
                }else {
                    chatContorller(user);
                }
            }
            case "2" -> {
                USER.allPrintUser();
                userContorller();
            }
            case "3" -> {
                for (userRole userRole:userRole.values()){
                    System.out.println("역할: "+userRole.getRoleName());
                }
                userContorller();
            }
            case "4" -> {
                System.out.print("생성할 유저의 이름을 입력하세요: ");
                String userName = SC.next();
                System.out.print("생성할 유저의 역할을 입력해주세요: ");
                String userRole = SC.next();
                USER.userCreate(userName, userRole);
                userContorller();
            }
            case "5" -> {
                System.out.print("수정할 기존의 유저 이름을 입력하세요: ");
                String name = SC.next();
                System.out.print("새로운 유저의 이름을 입력하세요: ");
                String updateName = SC.next();
                USER.userUpdate(name, updateName);
                userContorller();
            }
            case "6" -> {
                System.out.print("삭제할 유저의 이름을 입력하세요: ");
                String userName = SC.next();
                USER.userDelete(userName);
                userContorller();
            }
            case "7" -> {
                consoleContorller(StatusType.SEVER);
            }
            default -> {
                System.out.print("잘못 된 숫자를 입력하였습니다.\n다시 시도해주세요.\n");
                userContorller();
            }
        }

    }
}