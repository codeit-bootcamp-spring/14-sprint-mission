package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.entity.Category;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.enumType.StatusType;
import com.sprint.mission.discodeit.repository.categoryRepository;
import com.sprint.mission.discodeit.repository.channelRepository;
import com.sprint.mission.discodeit.repository.serverRepository;
import com.sprint.mission.discodeit.service.jcf.JCFcategoryService;
import com.sprint.mission.discodeit.service.jcf.JCFchannelService;
import com.sprint.mission.discodeit.service.jcf.JCFserverService;

import java.util.Scanner;

import static com.sprint.mission.discodeit.controller.UserContorller.userContorller;

public class ConsoleController {
    private static final serverRepository SERVER = new JCFserverService();
    private static final categoryRepository CATEGORY = new JCFcategoryService();
    private static final channelRepository CHANNEL = new JCFchannelService();
    public static final Scanner SC = new Scanner(System.in);

    public static void consoleContorller(StatusType status){
        System.out.println("--- 아래 메뉴에서 기능을 선택하세요 ---");
        System.out.print("--------------------\n 1. " + status.getStatusName() +
                " 생성\n 2. " + status.getStatusName() +
                " 출력 \n 3. " + status.getStatusName() +
                " 선택\n 4. " + status.getStatusName() +
                " 수정\n 5. " + status.getStatusName() +
                " 삭제\n 6. 이전으로 돌아가기\n 7. 종료\n--------------------\n 숫자를 입력하세요: ");

        String input = SC.next();
        switch (input){
            case "1" -> {
                System.out.print("생성할 "+ status.getStatusName() +"의 이름을 입력하세요: ");
                if (status.equals(StatusType.SEVER)) {
                    SERVER.serverCreate(SC.next());
                    consoleContorller(StatusType.SEVER);
                } else if (status.equals(StatusType.CATEGORY)) {
                    CATEGORY.categoryCreate(SC.next());
                    consoleContorller(StatusType.CATEGORY);
                } else if (status.equals(StatusType.CHANNEL)) {
                    CHANNEL.channelCreate(SC.next());
                    consoleContorller(StatusType.CHANNEL);
                }
            }
            case "2" -> {
                if (status.equals(StatusType.SEVER)) {
                    SERVER.allPrintServer();
                    consoleContorller(StatusType.SEVER);
                } else if (status.equals(StatusType.CATEGORY)) {
                    CATEGORY.allPrintCategory();
                    consoleContorller(StatusType.CATEGORY);
                } else if (status.equals(StatusType.CHANNEL)) {
                    CHANNEL.allPrintChannel();
                    consoleContorller(StatusType.CHANNEL);
                }
            }
            case "3" -> {
                if (status.equals(StatusType.SEVER)) {
                    Server selectServer = SERVER.selectedServer();
                    if (selectServer == null){
                        consoleContorller(StatusType.SEVER);
                    }else {
                        // 서버 선택 되면 카테고리 함수 호출
                        consoleContorller(StatusType.CATEGORY);
                    }
                } else if (status.equals(StatusType.CATEGORY)) {
                    Category selectCategory = CATEGORY.selectedCategory();
                    if (selectCategory == null){
                        consoleContorller(StatusType.CATEGORY);
                    }else {
                        // 서버 선택 되면 채널 함수 호출
                        consoleContorller(StatusType.CHANNEL);
                    }
                } else if (status.equals(StatusType.CHANNEL)) {
                    Channel selectChannel = CHANNEL.selectedChannel();
                    if (selectChannel == null){
                        consoleContorller(StatusType.CHANNEL);
                    }else {
                        userContorller();
                    }
                }
            }
            case "4" -> {
                if (status.equals(StatusType.SEVER)) {
                    SERVER.allPrintServer();
                    System.out.print("수정할 기존의 "+status.getStatusName()+" 이름을 이름을 입력하세요: ");
                    String name = SC.next();
                    System.out.print("새로운 "+status.getStatusName()+"의 이름을 입력하세요: ");
                    String updateName = SC.next();
                    SERVER.serverUpdate(name, updateName);
                    consoleContorller(StatusType.SEVER);
                } else if (status.equals(StatusType.CATEGORY)) {
                    CATEGORY.allPrintCategory();
                    System.out.print("수정할 기존의 "+status.getStatusName()+" 이름을 이름을 입력하세요: ");
                    String name = SC.next();
                    System.out.print("새로운 "+status.getStatusName()+"의 이름을 입력하세요: ");
                    String updateName = SC.next();
                    CATEGORY.categoryUpdate(name, updateName);
                    consoleContorller(StatusType.CATEGORY);
                } else if (status.equals(StatusType.CHANNEL)) {
                    CHANNEL.allPrintChannel();
                    System.out.print("수정할 기존의 "+status.getStatusName()+" 이름을 이름을 입력하세요: ");
                    String name = SC.next();
                    System.out.print("새로운 "+status.getStatusName()+"의 이름을 입력하세요: ");
                    String updateName = SC.next();
                    CHANNEL.channelUpdate(name, updateName);
                    consoleContorller(StatusType.CHANNEL);
                }
            }
            case "5" -> {
                System.out.print("삭제할 "+status.getStatusName()+"의 이름을 입력하세요: ");
                if (status.equals(StatusType.SEVER)) {
                    SERVER.serverDelete(SC.next());
                    consoleContorller(StatusType.SEVER);
                } else if (status.equals(StatusType.CATEGORY)) {
                    CATEGORY.categoryDelete(SC.next());
                    consoleContorller(StatusType.CATEGORY);
                } else if (status.equals(StatusType.CHANNEL)) {
                    CHANNEL.channelDelete(SC.next());
                    consoleContorller(StatusType.CHANNEL);
                }
            }
            case "6" -> {
                if (status.equals(StatusType.SEVER)) {
                    consoleContorller(StatusType.SEVER);
                } else if (status.equals(StatusType.CATEGORY)) {
                    consoleContorller(StatusType.SEVER);
                } else if (status.equals(StatusType.CHANNEL)) {
                    consoleContorller(StatusType.CATEGORY);
                }
            }
            case "7" -> System.exit(0);
            default -> {
                System.out.print("잘못 된 숫자를 입력하였습니다.\n다시 시도해주세요.\n");
                if (status.equals(StatusType.SEVER)) {
                    consoleContorller(StatusType.SEVER);
                } else if (status.equals(StatusType.CATEGORY)) {
                    consoleContorller(StatusType.CATEGORY);
                } else if (status.equals(StatusType.CHANNEL)) {
                    consoleContorller(StatusType.CHANNEL);
                }
            }
        }

    }
}
