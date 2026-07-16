package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Category;
import com.sprint.mission.discodeit.repository.categoryRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFcategoryRepository;

import java.util.Scanner;

public class JCFcategoryService extends JCFcategoryRepository implements categoryRepository {
    Scanner sc = new Scanner(System.in);

    @Override
    public void allPrintCategory() {
        for (Category category:super.categories){
            System.out.printf("카테고리의 이름: %s 입니다.\n", category.getCategoryName());
        }
    }

    @Override
    public void printCategory() {
        System.out.print("자세히 보고자 하는 카테고리의 이름을 말해주세요: ");
        String categoryName = sc.next();
        Category category = findByCategory(categoryName);

        System.out.println(category.toString());
    }

    @Override
    public Category selectedCategory() {
        allPrintCategory();
        System.out.print("선택할 카테고리의 이름을 말해주세요: ");
        String categoryName = sc.next();
        Category category = findByCategory(categoryName);

        if (category != null){
            return category;
        }

        System.out.println("카테고리를 찾을 수 없습니다.");
        return null;
    }
}
