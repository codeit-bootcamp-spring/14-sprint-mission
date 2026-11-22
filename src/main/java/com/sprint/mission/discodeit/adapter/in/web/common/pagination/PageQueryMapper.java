package com.sprint.mission.discodeit.adapter.in.web.common.pagination;

import com.sprint.mission.discodeit.core.common.pagination.PageQuery;
import com.sprint.mission.discodeit.core.common.pagination.SortOrder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PageQueryMapper {

    public static PageQuery fromPageable(Pageable pageable) {
        List<SortOrder> sort = pageable.getSort().stream()
                .map(order -> new SortOrder(
                        order.getProperty(),
                        SortOrder.Direction.valueOf(order.getDirection().name())
                ))
                .toList();
        return new PageQuery(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }
}
