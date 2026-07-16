package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Server;

public interface categoryRepository extends categoryService{
    void categoryCreate(String categoryName);
    Server findByCategory(String categoryName);
    void categoryUpdate(String categoryName, String updateCategoryName);
    void categoryDelete(String categoryName);
}
