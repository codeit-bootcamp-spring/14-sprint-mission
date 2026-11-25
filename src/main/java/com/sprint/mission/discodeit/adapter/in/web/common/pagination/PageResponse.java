package com.sprint.mission.discodeit.adapter.in.web.common.pagination;

import com.sprint.mission.discodeit.core.common.pagination.PageResult;

import java.util.List;

public record PageResponse<T, S>(
        List<T> content,
        S nextCursor,
        int size,
        boolean hasNext,
        Long totalElement
) {
    public static <T, S> PageResponse<T, S> of(List<T> content, S nextCursor, int size, boolean hasNext) {
        return new PageResponse<>(content, nextCursor, size, hasNext, (long) content.size());
    }

    public static <T, S> PageResponse<T, S> from(PageResult<T, S> page) {
        return new PageResponse<>(page.content(), page.nextCursor(), page.size(), page.hasNext(),
                (long) page.content().size());
    }
}
