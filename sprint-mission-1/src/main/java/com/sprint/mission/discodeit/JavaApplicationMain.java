package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.categoryRepository;
import com.sprint.mission.discodeit.repository.channelRepository;
import com.sprint.mission.discodeit.repository.serverRepository;
import com.sprint.mission.discodeit.service.jcf.JCFcategoryService;
import com.sprint.mission.discodeit.service.jcf.JCFchannelService;
import com.sprint.mission.discodeit.service.jcf.JCFserverService;

import java.util.Scanner;

public class JavaApplicationMain {
    // 어떻게 클래스로 분류할지 고민(메서드 + 필드)
    static serverRepository server = new JCFserverService();
    static categoryRepository category = new JCFcategoryService();
    static channelRepository channel = new JCFchannelService();
    static Scanner sc = new Scanner(System.in);

    public static void input(StatusType status){
        System.out.println("--- 아래 메뉴에서 기능을 선택하세요 ---");
        System.out.print("--------------------\n 1. " + status.getStatusName() +
                " 생성\n 2. " + status.getStatusName() +
                " 출력 \n 3. " + status.getStatusName() +
                " 선택\n 4. " + status.getStatusName() +
                " 수정\n 5. " + status.getStatusName() +
                " 삭제\n 6. 이전으로 돌아가기\n 7. 종료\n--------------------\n 숫자를 입력하세요: ");

        String input = sc.next();
        switch (input){
            case "1" -> {
                System.out.print("생성할 "+ status.getStatusName() +"의 이름을 입력하세요: ");
                if (status.equals(StatusType.SEVER)) {
                    server.serverCreate(sc.next());
                    input(StatusType.SEVER);
                } else if (status.equals(StatusType.CATEGORY)) {
                    category.categoryCreate(sc.next());
                    input(StatusType.CATEGORY);
                } else if (status.equals(StatusType.CHANNEL)) {
                    channel.channelCreate(sc.next());
                    input(StatusType.CHANNEL);
                }
            }
            case "2" -> {
                if (status.equals(StatusType.SEVER)) {
                    server.allPrintServer();
                    input(StatusType.SEVER);
                } else if (status.equals(StatusType.CATEGORY)) {
                    category.allPrintCategory();
                    input(StatusType.CATEGORY);
                } else if (status.equals(StatusType.CHANNEL)) {
                    channel.allPrintChannel();
                    input(StatusType.CHANNEL);
                }
            }
            case "3" -> {
                if (status.equals(StatusType.SEVER)) {
                    Server selectServer = server.selectedServer();
                    if (selectServer == null){
                        input(StatusType.SEVER);
                    }else {
                        // 서버 선택 되면 카테고리 함수 호출
                        input(StatusType.CATEGORY);
                    }
                } else if (status.equals(StatusType.CATEGORY)) {
                    Category selectCategory = category.selectedCategory();
                    if (selectCategory == null){
                        input(StatusType.CATEGORY);
                    }else {
                        // 서버 선택 되면 채널 함수 호출
                        input(StatusType.CHANNEL);
                    }
                } else if (status.equals(StatusType.CHANNEL)) {
                    Channel selectChannel = channel.selectedChannel();
                    if (selectChannel == null){
                        input(StatusType.CHANNEL);
                    }else {
                        // 채널 선택 시 뭐 할지 정하기
                    }
                }
            }
            case "4" -> {
                server.allPrintServer();
                System.out.print("수정할 기존의 "+status.getStatusName()+" 이름을 이름을 입력하세요: ");
                String name = sc.next();
                System.out.print("새로운 "+status.getStatusName()+"의 이름을 입력하세요: ");
                String updateName = sc.next();
                if (status.equals(StatusType.SEVER)) {
                    server.serverUpdate(name, updateName);
                    input(StatusType.SEVER);
                } else if (status.equals(StatusType.CATEGORY)) {
                    category.categoryUpdate(name, updateName);
                    input(StatusType.CATEGORY);
                } else if (status.equals(StatusType.CHANNEL)) {
                    channel.channelUpdate(name, updateName);
                    input(StatusType.CHANNEL);
                }
            }
            case "5" -> {
                System.out.print("삭제할 "+status.getStatusName()+"의 이름을 입력하세요: ");
                if (status.equals(StatusType.SEVER)) {
                    server.serverDelete(sc.next());
                    input(StatusType.SEVER);
                } else if (status.equals(StatusType.CATEGORY)) {
                    category.categoryDelete(sc.next());
                    input(StatusType.CATEGORY);
                } else if (status.equals(StatusType.CHANNEL)) {
                    channel.channelDelete(sc.next());
                    input(StatusType.CHANNEL);
                }
            }
            case "6" -> {
                if (status.equals(StatusType.SEVER)) {
                    input(StatusType.SEVER);
                } else if (status.equals(StatusType.CATEGORY)) {
                    input(StatusType.SEVER);
                } else if (status.equals(StatusType.CHANNEL)) {
                    input(StatusType.CATEGORY);
                }
            }
            case "7" -> System.exit(0);
            default -> {
                System.out.print("잘못 된 숫자를 입력하였습니다.\n다시 시도해주세요.\n");
                if (status.equals(StatusType.SEVER)) {
                    input(StatusType.SEVER);
                } else if (status.equals(StatusType.CATEGORY)) {
                    input(StatusType.CATEGORY);
                } else if (status.equals(StatusType.CHANNEL)) {
                    input(StatusType.CHANNEL);
                }
            }
        }

    }

    public static void main(String[] args) {
        System.out.println("--- 디스코드잇에 오신 걸 환영합니다 ---");
        input(StatusType.SEVER);
    }
}