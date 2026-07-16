package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Category;

public interface categoryService {
    // 서버 목록 출력
    void allPrintCategory();
    void printCategory();
    // 서버 선택
    Category selectedCategory();
}
