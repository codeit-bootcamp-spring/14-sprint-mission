package com.sprint.mission.discodeit.user.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.user.domain.entity.User;
import com.sprint.mission.discodeit.user.domain.repository.UserRepository;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

class DiscodeitUserDetailsServiceTest {

    private final UserRepository repository = mock(UserRepository.class);
    private final DiscodeitUserDetailsService service =
        new DiscodeitUserDetailsService(repository);

    @Test
    void returnsUserDetailsForExistingUser() {
        UUID userId = UUID.randomUUID();
        User user = mock(User.class);
        when(user.getId()).thenReturn(userId);
        when(user.getName()).thenReturn("june");
        when(user.getUserPassword()).thenReturn("hashed-password");
        when(repository.findByName("june")).thenReturn(Optional.of(user));

        UserDetails details = service.loadUserByUsername("june");

        assertEquals("june", details.getUsername());
        assertEquals("hashed-password", details.getPassword());
        assertEquals(userId, ((DiscodeitUserDetails) details).getUser().userId());
    }

    @Test
    void throwsWhenUserDoesNotExist() {
        when(repository.findByName("missing")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> service.loadUserByUsername("missing"));
    }
}
