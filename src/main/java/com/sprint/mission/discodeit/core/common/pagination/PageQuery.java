package com.sprint.mission.discodeit.core.common.pagination;

import jakarta.validation.constraints.Min;

import java.util.List;

public record PageQuery(@Min(0) int page,
                        @Min(1) int size,
                        List<SortOrder> sort) {
}
