package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class UserStorageImpl implements UserStorage {

    private HashMap<Integer, User> users = new HashMap<>();
    private int id = 1;

    @Override
    public User createUser(User user) {
        user.setId(id);
        id++;
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(int id) {
        return users.get(id);
    }

    @Override
    public void deleteUser(int id) {
        users.remove(id);
    }

    @Override
    public User patchUser(User user) {
        users.put(user.getId(), user);
        return user;
    }
}
