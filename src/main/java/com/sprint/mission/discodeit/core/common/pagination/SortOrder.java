package com.sprint.mission.discodeit.core.common.pagination;

public record SortOrder(String field, Direction direction) {
    public enum Direction {
        ASC,
        DESC
    }
}
