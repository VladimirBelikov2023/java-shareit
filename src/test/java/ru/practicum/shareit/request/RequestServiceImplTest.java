package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.exception.NoObjectException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {
    private RequestService service;
    @Mock
    private RequestRepository requestRepository;

    @Mock
    private UserRepo userRepo;

    @Mock
    private ItemRepository itemRepository;

    @BeforeEach
    public void setUp() {
        UserService userService = new UserServiceImpl(userRepo);
        service = new RequestServiceImpl(userService, requestRepository, itemRepository);
    }

    @DirtiesContext
    @Test
    void createRequest() {
        User user = User.builder().name("sds").email("sd@mail.ru").id(1).build();
        ItemRequest request = new ItemRequest(1, "sdfd", user, LocalDateTime.now(), new ArrayList<>());

        when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        when(requestRepository.save(Mockito.any(ItemRequest.class))).thenReturn(request);

        ItemRequestDto requestDto = RequestMapper.toItemRequestDto(request);
        ItemRequestDto requestDto2 = service.createRequest(1, requestDto);
        assertEquals(1, requestDto2.getId());
    }

    @DirtiesContext
    @Test
    void createRequestBadOwnerId() {
        User user = User.builder().name("sds").email("sd@mail.ru").id(1).build();
        ItemRequest request = new ItemRequest(1, "sdfd", user, LocalDateTime.now(), new ArrayList<>());

        when(userRepo.findById(Mockito.anyInt())).thenReturn(null);

        ItemRequestDto requestDto = RequestMapper.toItemRequestDto(request);
        Exception e = assertThrows(NoObjectException.class, () -> service.createRequest(1, requestDto));
        assertEquals("User не найден", e.getMessage());
    }

    @DirtiesContext
    @Test
    void createRequestBadDescription() {
        User user = User.builder().name("sds").email("sd@mail.ru").id(1).build();
        ItemRequest request = new ItemRequest(1, "  ", user, LocalDateTime.now(), new ArrayList<>());

        when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));

        ItemRequestDto requestDto = RequestMapper.toItemRequestDto(request);
        Exception e = assertThrows(ValidationException.class, () -> service.createRequest(1, requestDto));
        assertEquals("Не верное описание", e.getMessage());
    }

    @DirtiesContext
    @Test
    void getRequestsBadOwnerId() {

        when(userRepo.findById(Mockito.anyInt())).thenReturn(null);

        Exception e = assertThrows(NoObjectException.class, () -> service.getRequests(1));
        assertEquals("User не найден", e.getMessage());
    }

    @DirtiesContext
    @Test
    void getRequestIdBadOwnerId() {
        when(userRepo.findById(Mockito.anyInt())).thenReturn(null);

        Exception e = assertThrows(NoObjectException.class, () -> service.getRequestId(1, 1));
        assertEquals("User не найден", e.getMessage());
    }

    @DirtiesContext
    @Test
    void getRequestIdBadItemId() {
        User user = User.builder().name("sds").email("sd@mail.ru").id(1).build();
        when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        when(requestRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        Exception e = assertThrows(NoObjectException.class, () -> service.getRequestId(1, 1));
        assertEquals("Запрос не найден", e.getMessage());
    }

    @DirtiesContext
    @Test
    void getRequestsAnotheBadOwnerId() {
        User user = User.builder().name("sds").email("sd@mail.ru").id(1).build();
        when(userRepo.findById(Mockito.anyInt())).thenReturn(null);

        Exception e = assertThrows(NoObjectException.class, () -> service.getRequestsAnother(1, 0, 1));
        assertEquals("User не найден", e.getMessage());
    }

    @DirtiesContext
    @Test
    void getRequestsAnotheBadSize() {
        User user = User.builder().name("sds").email("sd@mail.ru").id(1).build();
        when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));

        Exception e = assertThrows(ValidationException.class, () -> service.getRequestsAnother(1, 0, 0));
        assertEquals("Длина больше 0 должна быть", e.getMessage());
    }

}