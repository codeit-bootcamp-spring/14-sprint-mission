package com.sprint.mission.discodeit.adapter.out.persistence.common.pagination;

import com.sprint.mission.discodeit.core.common.pagination.PageQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PageRequestMapper {

    public static PageRequest from(PageQuery query) {
        List<Sort.Order> orders = query.sort().stream()
                .map(order -> new Sort.Order(
                        Sort.Direction.valueOf(order.direction().name()),
                        order.field()
                ))
                .toList();
        return PageRequest.of(query.page(), query.size(), Sort.by(orders));
    }
}
