package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NoObjectException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        if (user.getEmail() == null) {
            throw new ValidationException("Почта пустая");
        }
        check(user);
        return UserMapper.toUserDto(userStorage.createUser(user));
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public UserDto getUser(int id) {
        return isExists(id);
    }

    @Override
    public void deleteUser(int id) {
        isExists(id);
        userStorage.deleteUser(id);
    }

    @Override
    public UserDto patchUser(int id, UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        user.setId(id);
        check(user);
        isExists(id);
        return UserMapper.toUserDto(userStorage.patchUser(user));
    }

    private UserDto check(User user) {
        if (user == null) {
            throw new ValidationException("Передан пустой объект пользователя");
        } else if (user.getEmail() != null && !user.getEmail().contains("@")) {
            throw new ValidationException("Передан объект с некоректным email");
        }
        return UserMapper.toUserDto(user);
    }

    private UserDto isExists(int id) {
        User user = userStorage.getUser(id);
        if (user == null) {
            throw new NoObjectException("User не обнаружен");
        }
        return UserMapper.toUserDto(user);
    }

}