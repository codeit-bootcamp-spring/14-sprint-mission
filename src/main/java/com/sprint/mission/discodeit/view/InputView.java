package com.sprint.mission.discodeit.view;

import java.util.Scanner;
import java.util.UUID;

// 오직 사용자의 input 관련
public class InputView {

    private final Scanner scanner = new Scanner(System.in);

    public int inputSelection() {
        System.out.print("메뉴 선택: ");
        return Integer.parseInt(scanner.nextLine());
    }

    public String inputString(String field) {
        System.out.printf("%s: ", field);
        return scanner.nextLine();
    }

    public UUID inputId(String entity) {
        System.out.printf("%s ID: ", entity);
        return UUID.fromString(scanner.nextLine());
    }

}
