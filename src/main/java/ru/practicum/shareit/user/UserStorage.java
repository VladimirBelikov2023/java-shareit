package ru.practicum.shareit.user;

import java.util.List;

public interface UserStorage {
    User createUser(User user);

    List<User> getAllUsers();

    User getUser(int id);

    void deleteUser(int id);

    User patchUser(User user);
}
