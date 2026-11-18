package com.sprint.mission.discodeit.user.adapter.persistence;

import com.sprint.mission.discodeit.user.domain.Email;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmailConverter implements AttributeConverter<Email, String> {


  @Override
  public String convertToDatabaseColumn(Email attribute) {
    return attribute == null ? null : attribute.value();
  }

  @Override
  public Email convertToEntityAttribute(String dbData) {
    return dbData == null ? null : new Email(dbData);
  }
}
