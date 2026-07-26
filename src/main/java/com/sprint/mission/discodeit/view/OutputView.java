package com.sprint.mission.discodeit.view;

// 오직 사용자에게 보여주는 부분만 관련
public class OutputView {
    public void printEntitySelection() {
        System.out.println(
                "=== 메뉴 ===\n" +
                "1. User\n" +
                "2. Channel\n" +
                "3. Message\n"+
                "4. 그만두기\n" +
                "메뉴를 선택하세요: ");
    }

    public void printCrudSelection(String entity) {
        System.out.printf(
                "=== %s ====\n" +
                    "1. Create\n" +
                    "2. Read\n" +
                    "3. Read All\n" +
                    "4. Update\n" +
                    "5. Delete\n" +
                    "6. 뒤로가기\n" +
                    "7. 그만두기\n" +
                    "메뉴를 선택하세요:"
                , entity);
    }
}
