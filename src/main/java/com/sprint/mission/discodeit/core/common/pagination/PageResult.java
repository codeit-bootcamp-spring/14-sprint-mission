package com.sprint.mission.discodeit.core.common.pagination;

import java.util.List;
import java.util.function.Function;

public record PageResult<T, C>(List<T> content, C nextCursor, int size, boolean hasNext) {
    public PageResult {
        content = List.copyOf(content);
    }

    public static <T, C> PageResult<T, C> from(
            List<T> content,
            int size,
            boolean hasNext,
            Function<? super T, ? extends C> cursorExtractor
    ) {
        C nextCursor = content.isEmpty()
                ? null
                : cursorExtractor.apply(content.getLast());
        return new PageResult<>(content, nextCursor, size, hasNext);
    }

    public <R> PageResult<R, C> map(Function<? super T, ? extends R> mapper) {
        return new PageResult<>(content.stream().<R>map(mapper).toList(), nextCursor, size, hasNext);
    }
}
