package com.sprint.mission.discodeit;


import com.sprint.mission.discodeit.controller.ConsoleController;
import com.sprint.mission.discodeit.controller.FileController;

public class JavaApplicationMain {
    public static void main(String[] args) {
//        ConsoleController consoleController = new ConsoleController();
//
//        System.out.println("--- 디스코드잇에 오신 걸 환영합니다 ---");
//        consoleController.consoleContorller();

        FileController fileController = new FileController();
        fileController.fileController();
    }
}