package com.sprint.mission.discodeit.common;

import lombok.Getter;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

public final class DeletionEvent<T> implements ResolvableTypeProvider {

  @Getter
  private final T value;
  private final ResolvableType valueType;

  public DeletionEvent(T value, ResolvableType valueType) {
    this.value = value;
    this.valueType = valueType;
  }


  @Override
  public ResolvableType getResolvableType() {
    return ResolvableType.forClassWithGenerics(
        DeletionEvent.class,
        valueType
    );
  }
}
