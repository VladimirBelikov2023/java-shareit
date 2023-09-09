package ru.practicum.shareit.user;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class UserServiceInregrationTest {

    @Autowired
    private UserService userService;


    @Test
    @Order(1)
    void createUser() {
        User user = new User(1, "dssd", "dsd@mail.ru");

        UserDto user1 = userService.createUser(UserMapper.toUserDto(user));

        assertEquals(1, user1.getId());

    }

    @Test
    @Order(3)
    void getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        assertEquals(1, users.size());
        assertEquals(1, users.get(0).getId());
    }

    @Test
    @Order(4)
    void getUser() {
        UserDto userDto = userService.getUser(1);
        assertEquals(1, userDto.getId());
        assertEquals("dssd", userDto.getName());
        assertEquals("dsd@mail.ru", userDto.getEmail());
    }

    @Test
    @Order(5)
    void patchUser() {
        UserDto userDto1 = new UserDto(1, "sssss", "wwwww@mail.ru");
        UserDto userDto = userService.patchUser(1, userDto1);
        assertEquals(userDto.getName(), userDto1.getName());
        assertEquals(userDto.getEmail(), userDto1.getEmail());
    }

    @Test
    @Transactional
    @Order(6)
    void deleteUser() {
        userService.deleteUser(1);
        List<UserDto> users = userService.getAllUsers();
        assertEquals(0, users.size());
    }
}