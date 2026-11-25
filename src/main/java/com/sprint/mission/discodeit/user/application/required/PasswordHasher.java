package com.sprint.mission.discodeit.user.application.required;

public interface PasswordHasher {

  String hash(String rawPwd);

  boolean matches(String rawPwd, String hashPwd);
}
