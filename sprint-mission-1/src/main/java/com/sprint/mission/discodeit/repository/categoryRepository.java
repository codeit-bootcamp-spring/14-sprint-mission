package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Category;
import com.sprint.mission.discodeit.service.categoryService;

public interface categoryRepository extends categoryService {
    void categoryCreate(String categoryName);
    Category findByCategory(String categoryName);
    void categoryUpdate(String categoryName, String updateCategoryName);
    void categoryDelete(String categoryName);
}
