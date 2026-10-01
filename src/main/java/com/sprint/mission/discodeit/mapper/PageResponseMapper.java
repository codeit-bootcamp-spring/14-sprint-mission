package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class PageResponseMapper {

    public <T> PageResponse<T> fromSlice(Slice<T> slice) {
        if (slice == null) {
            return null;
        }
        return new PageResponse<>(
            slice.getContent(),
            slice.getNumber(),
            slice.getSize(),
            slice.hasNext(),
            null // Slice는 총 개수를 알 수 없으므로 null 반환
        );
    }

    public <T> PageResponse<T> fromPage(Page<T> page) {
        if (page == null) {
            return null;
        }
        return new PageResponse<>(
            page.getContent(),
            page.getNumber(),
            page.getSize(),
            page.hasNext(),
            page.getTotalElements() // Page가 계산한 전체 데이터 개수 반환
        );
    }
}
