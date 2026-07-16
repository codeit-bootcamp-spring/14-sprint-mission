package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.StatusType;
import com.sprint.mission.discodeit.entity.Server;
import com.sprint.mission.discodeit.service.jcf.JCFserverService;
import com.sprint.mission.discodeit.service.serverRepository;

import java.util.Scanner;

public class JavaApplicationMain {
    static serverRepository server = new JCFserverService();
    static Scanner sc = new Scanner(System.in);

    public static void input(StatusType status){
        System.out.println("--- 아래 메뉴에서 기능을 선택하세요 ---");
        System.out.print("--------------------\n 1. " + status.getStatusName() +
                " 생성\n 2. " + status.getStatusName() +
                " 출력 \n 3. " + status.getStatusName() +
                " 선택\n 4. " + status.getStatusName() +
                " 수정\n 5. " + status.getStatusName() +
                " 삭제\n 6. 종료\n--------------------\n 숫자를 입력하세요: ");

        String input = sc.next();
        switch (input){
            case "1" -> {
                System.out.print("생성할 "+ status.getStatusName() +"의 이름을 입력하세요: ");
                if (status.equals(StatusType.SEVER)) {
                    server.serverCreate(sc.next());
                    input(StatusType.SEVER);
                }
            }
            case "2" -> {
                if (status.equals(StatusType.SEVER)) {
                    server.allPrintServer();
                    input(StatusType.SEVER);
                }
            }
            case "3" -> {
                Server selectServer = server.selectedServer();
                if (status.equals(StatusType.SEVER)) {
                    if (selectServer == null){
                        input(StatusType.SEVER);
                    }else {
                        // 서버 선택 되면 카테고리 함수 호출
                        input(StatusType.CATEGORY);
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
                }
            }
            case "5" -> {
                System.out.print("삭제할 "+status.getStatusName()+"의 이름을 입력하세요: ");
                if (status.equals(StatusType.SEVER)) {
                    server.serverDelete(sc.next());
                    input(StatusType.SEVER);
                }
            }
            case "6" -> {
                System.exit(0);
            }
            default -> {
                System.out.print("잘못 된 숫자를 입력하였습니다.\n다시 시도해주세요.\n");
                if (status.equals(StatusType.SEVER)) {
                    input(StatusType.SEVER);
                }
            }
        }

    }

    public static void main(String[] args) {
        JCFserverService server = new JCFserverService();

        System.out.println("--- 디스코드잇에 오신 걸 환영합니다 ---");
        input(StatusType.SEVER);
    }
}