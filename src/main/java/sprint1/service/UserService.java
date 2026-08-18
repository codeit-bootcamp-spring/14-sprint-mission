package sprint1.service;

import com.example.demo.levelTest1.entity.User;
import java.util.List;
import java.util.UUID;


public interface UserService {

    User create(String username, String email, String password);

    User find(UUID userId);

    List<User> findAll();

    User update(UUID userId, String newUsername);

    void delete(UUID userId);
}
