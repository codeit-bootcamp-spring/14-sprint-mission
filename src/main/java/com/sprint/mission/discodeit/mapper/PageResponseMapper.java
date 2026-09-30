package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PageResponseMapper {

    //slice는 가벼운 페이지 조회 결과
    public <T>PageResponse<T> fromSlice(Slice<T> slice) {
        List<T> content = slice.getContent();
        int number = slice.getNumber();
        int size = slice.getSize();
        boolean hasNext = slice.hasNext();

        //Slice는 총 개수를 안 세니까, totalElements 자리에는 null을 넣는다.
        return new PageResponse<>(content,number,size,hasNext,null);
    }
    public <T> PageResponse<T> fromPage(Page<T> page) {
        List<T> content = page.getContent();
        int number = page.getNumber();
        int size = page.getSize();
        boolean hasNext = page.hasNext();
        Long totalElements = page.getTotalElements();

        return new PageResponse<>(content, number, size, hasNext, totalElements);
    }
}
