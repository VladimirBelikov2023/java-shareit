package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.ErrorStatusException;
import ru.practicum.shareit.exception.NoObjectException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepo;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @Mock
    private BookingRepository bookingRepository;

    private BookingService bookingService;
    @Mock
    private UserRepo userRepo;

    @Mock
    private ItemRepository itemRepository;

    @BeforeEach
    public void serUp() {
        UserService userService = new UserServiceImpl(userRepo);
        bookingService = new BookingServiceImpl(itemRepository, bookingRepository, userRepo, userService);

    }

    @Test
    void createBooking() {

        User user = User.builder().name("fgfg").email("sd@mail.ru").id(1).build();
        User user2 = User.builder().name("fgfg").email("sdsd@mail.ru").id(2).build();

        Item item = Item.builder().id(1).name("Sd").description("sds").owner(user).available(true).build();

        Booking booking = new Booking(1, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), item, user2, Status.WAITING);
        when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user2));
        when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));
        when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(booking);

        BookingDto booking1 = bookingService.createBooking(BookingMapper.toBookingDto(booking), 2);
        assertEquals(booking.getId(), booking1.getId());
    }

    @Test
    void createBookingBadUser() {

        User user = User.builder().name("fgfg").email("sd@mail.ru").id(1).build();
        User user2 = User.builder().name("fgfg").email("sdsd@mail.ru").id(2).build();

        Item item = Item.builder().id(1).name("Sd").description("sds").owner(user).available(true).build();

        Booking booking = new Booking(1, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), item, user2, Status.WAITING);
        when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        Exception e = assertThrows(NoObjectException.class, () -> bookingService.createBooking(BookingMapper.toBookingDto(booking), 2));
        assertEquals("Неверные входные данные", e.getMessage());
    }

    @Test
    void createBookingBadItemId() {

        User user = User.builder().name("fgfg").email("sd@mail.ru").id(1).build();
        User user2 = User.builder().name("fgfg").email("sdsd@mail.ru").id(2).build();

        Item item = Item.builder().id(1).name("Sd").description("sds").owner(user).available(true).build();

        Booking booking = new Booking(1, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), item, user2, Status.WAITING);
        when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.empty());

        Exception e = assertThrows(NoObjectException.class, () -> bookingService.createBooking(BookingMapper.toBookingDto(booking), 2));
        assertEquals("Неверные входные данные", e.getMessage());
    }

    @Test
    void createBookingNotOwner() {

        User user = User.builder().name("fgfg").email("sd@mail.ru").id(1).build();

        Item item = Item.builder().id(1).name("Sd").description("sds").owner(user).available(true).build();

        Booking booking = new Booking(1, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), item, user, Status.WAITING);
        when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));


        Exception e = assertThrows(NoObjectException.class, () -> bookingService.createBooking(BookingMapper.toBookingDto(booking), 1));
        assertEquals("Вы владелец вещи", e.getMessage());
    }

    @Test
    void createBookingBadStart() {

        User user = User.builder().name("fgfg").email("sd@mail.ru").id(1).build();
        User user2 = User.builder().name("fgfg").email("sdsd@mail.ru").id(2).build();

        Item item = Item.builder().id(1).name("Sd").description("sds").owner(user).available(true).build();

        Booking booking = new Booking(1, LocalDateTime.now(), LocalDateTime.now().plusSeconds(2), item, user2, Status.WAITING);
        when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user2));
        when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));


        Exception e = assertThrows(ValidationException.class, () -> bookingService.createBooking(BookingMapper.toBookingDto(booking), 2));
        assertEquals("Не верное время бронирования", e.getMessage());
    }

    @Test
    void createBookingBadEnd() {

        User user = User.builder().name("fgfg").email("sd@mail.ru").id(1).build();
        User user2 = User.builder().name("fgfg").email("sdsd@mail.ru").id(2).build();

        Item item = Item.builder().id(1).name("Sd").description("sds").owner(user).available(true).build();

        Booking booking = new Booking(1, LocalDateTime.now().plusSeconds(1), LocalDateTime.now(), item, user2, Status.WAITING);
        when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user2));
        when(itemRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(item));


        Exception e = assertThrows(ValidationException.class, () -> bookingService.createBooking(BookingMapper.toBookingDto(booking), 2));
        assertEquals("Не верное время бронирования", e.getMessage());
    }

    @Test
    void approveBooking() {
        User user = User.builder().name("fgfg").email("sd@mail.ru").id(1).build();
        User user2 = User.builder().name("fgfg").email("sdsd@mail.ru").id(2).build();

        Item item = Item.builder().id(1).name("Sd").description("sds").owner(user).available(true).build();

        Booking booking = new Booking(1, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), item, user2, Status.WAITING);
        Booking booking1 = new Booking(1, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), item, user2, Status.APPROVED);
        when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(booking));
        when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(booking1);

        BookingDto booking2 = bookingService.approveBooking(1, 1, true);
        assertEquals(booking2.getStatus(), Status.APPROVED);
    }

    @Test
    void approveBookingBadUser() {
        User user = User.builder().name("fgfg").email("sd@mail.ru").id(1).build();
        User user2 = User.builder().name("fgfg").email("sdsd@mail.ru").id(2).build();

        Item item = Item.builder().id(1).name("Sd").description("sds").owner(user).available(true).build();

        Booking booking = new Booking(1, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), item, user2, Status.WAITING);

        Exception e = assertThrows(NoObjectException.class, () -> bookingService.approveBooking(1, 1, true));
        assertEquals("Неверные входные данные", e.getMessage());
    }

    @Test
    void approveBookingBadBookingId() {
        User user = User.builder().name("fgfg").email("sd@mail.ru").id(1).build();
        User user2 = User.builder().name("fgfg").email("sdsd@mail.ru").id(2).build();

        Item item = Item.builder().id(1).name("Sd").description("sds").owner(user).available(true).build();

        Booking booking = new Booking(1, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), item, user2, Status.WAITING);
        when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));

        Exception e = assertThrows(NoObjectException.class, () -> bookingService.approveBooking(1, 1, true));
        assertEquals("Неверные входные данные", e.getMessage());
    }

    @Test
    void approveBookingNotOwner() {
        User user = User.builder().name("fgfg").email("sd@mail.ru").id(1).build();
        User user2 = User.builder().name("fgfg").email("sdsd@mail.ru").id(2).build();

        Item item = Item.builder().id(1).name("Sd").description("sds").owner(user).available(true).build();

        Booking booking = new Booking(1, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), item, user2, Status.WAITING);
        Booking booking1 = new Booking(1, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), item, user2, Status.APPROVED);
        when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user2));
        when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(booking));

        Exception e = assertThrows(NoObjectException.class, () -> bookingService.approveBooking(1, 1, true));
        assertEquals("Вы не владелец", e.getMessage());
    }

    @Test
    void approveBookingAlreadyDone() {
        User user = User.builder().name("fgfg").email("sd@mail.ru").id(1).build();
        User user2 = User.builder().name("fgfg").email("sdsd@mail.ru").id(2).build();

        Item item = Item.builder().id(1).name("Sd").description("sds").owner(user).available(true).build();

        Booking booking = new Booking(1, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), item, user2, Status.APPROVED);
        Booking booking1 = new Booking(1, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), item, user2, Status.APPROVED);
        when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));
        when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(booking));

        Exception e = assertThrows(ValidationException.class, () -> bookingService.approveBooking(1, 1, true));
        assertEquals("Запрос уже рассмотрен", e.getMessage());
    }

    @Test
    void getBooking() {
        User user = User.builder().name("fgfg").email("sd@mail.ru").id(1).build();
        User user2 = User.builder().name("fgfg").email("sdsd@mail.ru").id(2).build();

        Item item = Item.builder().id(1).name("Sd").description("sds").owner(user).available(true).build();

        Booking booking = new Booking(1, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), item, user2, Status.WAITING);
        when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user2));
        when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(booking));

        BookingDto booking1 = bookingService.getBooking(1, 1);
        assertEquals(booking.getId(), booking1.getId());
    }

    @Test
    void getBookingBadUserId() {
        User user = User.builder().name("fgfg").email("sd@mail.ru").id(1).build();
        User user2 = User.builder().name("fgfg").email("sdsd@mail.ru").id(2).build();

        Item item = Item.builder().id(1).name("Sd").description("sds").owner(user).available(true).build();

        Booking booking = new Booking(1, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), item, user2, Status.WAITING);
        when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(booking));

        Exception e = assertThrows(NoObjectException.class, () -> bookingService.getBooking(1, 1));
        assertEquals("Неверные входные данные", e.getMessage());
    }

    @Test
    void getBookingBadBookingId() {
        User user = User.builder().name("fgfg").email("sd@mail.ru").id(1).build();
        User user2 = User.builder().name("fgfg").email("sdsd@mail.ru").id(2).build();

        Item item = Item.builder().id(1).name("Sd").description("sds").owner(user).available(true).build();

        Booking booking = new Booking(1, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), item, user2, Status.WAITING);
        when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));

        Exception e = assertThrows(NoObjectException.class, () -> bookingService.getBooking(1, 1));
        assertEquals("Неверные входные данные", e.getMessage());
    }

    @Test
    void getBookingNotAllowed() {
        User user = User.builder().name("fgfg").email("sd@mail.ru").id(1).build();
        User user2 = User.builder().name("fgfg").email("sdsd@mail.ru").id(2).build();

        Item item = Item.builder().id(1).name("Sd").description("sds").owner(user).available(true).build();

        Booking booking = new Booking(1, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), item, user, Status.WAITING);
        when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user2));
        when(bookingRepository.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(booking));

        Exception e = assertThrows(NoObjectException.class, () -> bookingService.getBooking(1, 1));
        assertEquals("Вы не можете посмотреть запрос", e.getMessage());
    }

    @Test
    void getBookingLsBadUser() {
        Exception e = assertThrows(NoObjectException.class, () -> bookingService.getBookingLs(1, "WAITING"));
        assertEquals("User не найден", e.getMessage());
    }

    @Test
    void getBookingLsBadState() {
        User user = User.builder().name("fgfg").email("sd@mail.ru").id(1).build();
        when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));

        Exception e = assertThrows(ErrorStatusException.class, () -> bookingService.getBookingLs(1, "sds"));
        assertNull(e.getMessage());
    }

    @Test
    void getBookingLsOwner() {
        Exception e = assertThrows(NoObjectException.class, () -> bookingService.getBookingLsOwner(1, "WAITING"));
        assertEquals("User не найден", e.getMessage());
    }

    @Test
    void getBookingLsOwnerBadState() {
        User user = User.builder().name("fgfg").email("sd@mail.ru").id(1).build();
        when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user));

        Exception e = assertThrows(ErrorStatusException.class, () -> bookingService.getBookingLsOwner(1, "sds"));
        assertNull(e.getMessage());
    }

    @Test
    void getBookingAllBadSize() {
        User user = User.builder().name("fgfg").email("sd@mail.ru").id(1).build();
        User user2 = User.builder().name("fgfg").email("sdsd@mail.ru").id(2).build();

        Item item = Item.builder().id(1).name("Sd").description("sds").owner(user).available(true).build();

        Booking booking = new Booking(1, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), item, user2, Status.WAITING);
        when(userRepo.findById(Mockito.anyInt())).thenReturn(Optional.ofNullable(user2));

        Exception e = assertThrows(ValidationException.class, () -> bookingService.getBookingAll(1, 0, 0, false));
        assertEquals("Не верные входные данные", e.getMessage());
    }

    @Test
    void getBookingBadUser() {
        User user = User.builder().name("fgfg").email("sd@mail.ru").id(1).build();
        User user2 = User.builder().name("fgfg").email("sdsd@mail.ru").id(2).build();

        Item item = Item.builder().id(1).name("Sd").description("sds").owner(user).available(true).build();

        Booking booking = new Booking(1, LocalDateTime.now().plusSeconds(1), LocalDateTime.now().plusSeconds(2), item, user2, Status.WAITING);

        Exception e = assertThrows(NoObjectException.class, () -> bookingService.getBookingAll(1, 0, 0, false));
        assertEquals("User не найден", e.getMessage());
    }
}