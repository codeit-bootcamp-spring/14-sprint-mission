package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

public class FileUserRepository extends AbstractFileRepository<User> implements UserRepository {
    private static final String FILE_PATH = "users.ser";

    private static FileUserRepository instance;

    private FileUserRepository() {
        super(FILE_PATH);
    }

    public static FileUserRepository getInstance(){
        if (instance == null){
            instance = new FileUserRepository();
        }
        return instance;
    }
}

