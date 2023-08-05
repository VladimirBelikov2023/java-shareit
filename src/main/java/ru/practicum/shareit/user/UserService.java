package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    User createUser(User user);

    List<User> getAllUsers();

    User getUser(int id);

    void deleteUser(int id);

    User patchUser(int id, User user);
}
