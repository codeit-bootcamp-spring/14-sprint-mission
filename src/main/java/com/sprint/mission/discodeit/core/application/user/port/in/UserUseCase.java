package com.sprint.mission.discodeit.core.application.user.port.in;

import com.sprint.mission.discodeit.core.application.binarycontent.port.in.dto.request.CreateBinaryContentCommand;
import com.sprint.mission.discodeit.core.application.user.port.in.dto.response.UserResult;
import com.sprint.mission.discodeit.core.domain.user.Role;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Validated
public interface UserUseCase {
    UserResult createAccount(@NotBlank @Size(max = 50) String username,
                             @NotBlank @Email @Size(max = 50) String email,
                             @NotBlank String password,
                             @Nullable CreateBinaryContentCommand command);

    UserResult getUser(@NotNull UUID userId);

    List<UserResult> getAllUsers();

    UserResult patchUserInfo(@NotNull UUID id,
                             @Nullable @Size(max = 50) String username,
                             @Nullable @Email @Size(max = 50) String email,
                             @Nullable String password,
                             @Nullable CreateBinaryContentCommand command);

    void deleteAccount(@NotNull UUID userId);

    UserResult updateUserRole(@NotNull UUID userId, @NotNull Role role);

    void createBootstrapAdmin(@NotBlank String username, @NotBlank @Email String email,
                              @NotBlank String password);
}
