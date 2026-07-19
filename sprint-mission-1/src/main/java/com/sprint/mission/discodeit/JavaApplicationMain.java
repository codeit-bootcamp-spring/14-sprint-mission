package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.enumType.StatusType;
import static com.sprint.mission.discodeit.controller.ConsoleController.consoleContorller;

public class JavaApplicationMain {

    public static void main(String[] args) {
        System.out.println("--- 디스코드잇에 오신 걸 환영합니다 ---");
        consoleContorller(StatusType.SEVER);
    }
}