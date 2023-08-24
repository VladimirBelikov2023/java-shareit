package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NoObjectException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo repository;

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        if (user.getEmail() == null) {
            throw new ValidationException("Почта пустая");
        }
        check(user);
        try {
            return UserMapper.toUserDto(repository.save(user));
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Дубликат электронного адреса пользователя");
        }
    }

    @Override
    public List<UserDto> getAllUsers() {
        return UserMapper.toLsUserDto(repository.findAll());
    }

    @Override
    public UserDto getUser(int id) {
        User user = repository.findUserById(id);
        if (user == null) {
            throw new NoObjectException("User не найден");
        }
        return UserMapper.toUserDto(user);
    }

    @Override
    public void deleteUser(int id) {
        repository.deleteById(id);
    }

    @Override
    public UserDto patchUser(int id, UserDto userDto) {
        UserDto user = getUser(id);
        if (user == null) {
            throw new NoObjectException("User не найден");
        }
        User originUser = UserMapper.toUser(user);
        if (userDto.getName() != null) {
            originUser.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            originUser.setEmail(userDto.getEmail());
        }
        originUser.setId(id);
        check(originUser);
        return UserMapper.toUserDto(repository.save(originUser));
    }

    private UserDto check(User user) {
        if (user == null) {
            throw new ValidationException("Передан пустой объект пользователя");
        } else if (user.getEmail() != null && !user.getEmail().contains("@")) {
            throw new ValidationException("Передан объект с некоректным email");
        }
        return UserMapper.toUserDto(user);
    }
}