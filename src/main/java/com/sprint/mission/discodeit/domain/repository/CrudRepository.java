package com.sprint.mission.discodeit.domain.repository;

import java.util.List;
import java.util.Optional;


/*
    공통으로 모든 리포지토리에서 사용할 인터페이스
    저장 / 아이디별 찾기 / 삭제 / 모든 저장 개체 불러오기
 */
public interface CrudRepository <T, ID>{
    T saveEntity(T entity);
    Optional<T> findById(ID id);
    void deleteEntity(ID id);
    List<T> findAllEntity();
}
