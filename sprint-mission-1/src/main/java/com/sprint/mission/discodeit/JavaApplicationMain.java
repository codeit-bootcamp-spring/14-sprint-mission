package com.sprint.mission.discodeit;


import com.sprint.mission.discodeit.controller.ConsoleController;

public class JavaApplicationMain {
    public static void main(String[] args) {
        ConsoleController consoleContorller = new ConsoleController();

        System.out.println("--- 디스코드잇에 오신 걸 환영합니다 ---");
        consoleContorller.consoleContorller();
    }
}