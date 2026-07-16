package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Category;
import com.sprint.mission.discodeit.repository.categoryRepository;

import java.util.ArrayList;
import java.util.List;


public abstract class JCFcategoryRepository implements categoryRepository {
    protected final List<Category> categories = new ArrayList<>();

    @Override
    public void categoryCreate(String categoryName) {
        boolean categoryExists = true;
        for (Category each:categories){
            if (each.getCategoryName().equals(categoryName)){
                System.out.println("이미 생성된 카테고리의 이름입니다: "+categoryName);
                categoryExists = false;
            }
        }
        if (categoryExists){
            System.out.println("카테고리 생성이 완료되었습니다: "+categoryName);
            categories.add(new Category(categoryName));
        }
    }

    @Override
    public Category findByCategory(String categoryName) {
        for (Category each:categories){
            if (each.getCategoryName().equals(categoryName)){
                return each;
            }
        }
        System.out.println("찾는 카테고리의 이름이 없습니다: "+categoryName);
        return null;
    }

    @Override
    public void categoryUpdate(String categoryName, String updateCategoryName) {
        Category category = findByCategory(categoryName);
        if (category != null){
            System.out.println(categoryName+" 카테고리의 이름을 "+updateCategoryName+"로 수정했습니다.");
            category.setCategoryName(updateCategoryName);
        }
    }

    @Override
    public void categoryDelete(String categoryName) {
        Category category = findByCategory(categoryName);
        if (category != null){
            System.out.println(categoryName+" 카테고리를 삭제했습니다.");
            categories.remove(category);
        }
    }
}
