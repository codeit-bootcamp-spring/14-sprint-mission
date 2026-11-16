package com.sprint.mission.discodeit.adapter.in.controller.common;

import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.function.Function;

public record PageResponse<T, S>(
        List<T> content,
        S nextCursor,
        int size,
        boolean hasNext,
        Long totalElement
) {
    public static <T, S> PageResponse<T, S> fromSlice(
            Slice<T> slice,
            Function<T, S> nextCursorExtractor
    ) {
        S nextCursor = slice.hasContent()
                ? nextCursorExtractor.apply(
                        slice.getContent().get(slice.getNumberOfElements() - 1))
                : null;

        return new PageResponse<>(
                slice.getContent(),
                nextCursor,
                slice.getSize(),
                slice.hasNext(),
                slice.stream().count()
        );
    }
}
