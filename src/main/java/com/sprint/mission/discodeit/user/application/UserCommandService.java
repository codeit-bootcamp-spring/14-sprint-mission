package com.sprint.mission.discodeit.user.application;

import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.user.application.provided.command.UserCommand;
import com.sprint.mission.discodeit.user.application.required.PasswordHasher;
import com.sprint.mission.discodeit.user.application.required.UserRepository;
import com.sprint.mission.discodeit.user.application.validation.UserValidator;
import com.sprint.mission.discodeit.user.domain.Email;
import com.sprint.mission.discodeit.user.domain.Password;
import com.sprint.mission.discodeit.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserCommandService implements UserCommand {

  private final UserRepository userRepository;
  private final UserValidator userValidator;
  private final PasswordHasher passwordHasher;


  @Override
  public User create(String username, String emailVal, String password, BinaryContent profile) {
    Email email = Email.to(emailVal);
    String encodedPassword = passwordHasher.hash(password);
    Password pwd = Password.to(encodedPassword);

    userValidator.validateCreate(username, email);
    User user = User.create(username, pwd, email, profile);
    return userRepository.save(user);
  }

  @Override
  public void delete(User user) {
    userRepository.delete(user);
  }

  @Override
  public User update(User user, String username, String emailVal, String password,
      BinaryContent profile) {
    Email email = emailVal == null ? null : new Email(emailVal);

    Password pwd = password == null ? null : Password.to(passwordHasher.hash(password));

    userValidator.validateUpdate(user, username, email);

    return user.update(username, pwd, email, profile);
  }
  
}
