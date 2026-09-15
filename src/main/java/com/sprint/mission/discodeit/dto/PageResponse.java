package com.sprint.mission.discodeit.dto;

import java.util.List;

public record PageResponse<T>(
    List<T> content,
    int number,
    int size,
    Long totalElements
){

}
