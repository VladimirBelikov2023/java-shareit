package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NoObjectException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserStorage userStorage;

    @Override
    public User createUser(User user) {
        check(user);
        return userStorage.createUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User getUser(int id) {
        return isExists(id);
    }

    @Override
    public void deleteUser(int id) {
        isExists(id);
        userStorage.deleteUser(id);
    }

    @Override
    public User patchUser(int id, User user) {
        user.setId(id);
        checkPatch(user);
        isExists(id);
        User orUser = getUser(id);
        if (user.getName() == null) {
            user.setName(orUser.getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(orUser.getEmail());
        }
        return userStorage.patchUser(user);
    }

    private User check(User user) {
        List<User> users = getAllUsers();
        if (user == null) {
            throw new ValidationException("Передан пустой объект пользователя");
        } else if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Передан объект с некоректным email");
        } else if (users.stream().anyMatch(x -> x.getEmail().equals(user.getEmail()))) {
            throw new RuntimeException("Email должен быть уникальным");
        }
        return user;
    }

    private User checkPatch(User user) {
        boolean isValid = false;
        for (User o : userStorage.getAllUsers()) {
            if (!Objects.equals(o.getId(), user.getId()) && o.getEmail().equals(user.getEmail())) {
                isValid = true;
            }
        }
        if (user == null) {
            throw new ValidationException("Передан пустой объект пользователя");
        } else if (user.getEmail() != null && !user.getEmail().contains("@")) {
            throw new ValidationException("Передан объект с некоректным email");
        } else if (isValid) {
            throw new RuntimeException("Email должен быть уникальным");
        }
        return user;
    }

    private User isExists(int id) {
        if (userStorage.getUser(id) == null) {
            throw new NoObjectException("User не обнаружен");
        }
        return userStorage.getUser(id);
    }

}