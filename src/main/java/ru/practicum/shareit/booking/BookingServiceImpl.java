package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.exception.ErrorStatus;
import ru.practicum.shareit.exception.NoObjectException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {

    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final UserRepo userRepo;

    @Override
    public BookingDto createBooking(BookingDto bookingDto, int ownerId) {
        Item item = itemRepository.findItemById(bookingDto.getItemId());
        User owner = userRepo.findUserById(ownerId);
        if (item == null || owner == null) {
            throw new NoObjectException("Неверные входные данные");
        }
        if (item.getOwner().getId() == ownerId) {
            throw new NoObjectException("Вы владелец вещи");
        }
        bookingDto.setStatus("WAITING");
        bookingDto.setBooker(owner);
        bookingDto.setItem(item);
        check(bookingDto);
        Booking booking = BookingMapper.toBooking(bookingDto);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }


    public BookingDto approveBooking(int ownerId, int id, boolean isApproved) {
        User owner = userRepo.findUserById(ownerId);
        Booking booking = bookingRepository.findBookingById(id);

        if (owner == null || booking == null) {
            throw new NoObjectException("Неверные входные данные");
        }
        if (booking.getItem().getOwner().getId() != owner.getId()) {
            throw new NoObjectException("Вы не владелец");
        }
        if (booking.getStatus().equals("APPROVED") || booking.getStatus().equals("REJECTED")) {
            throw new ValidationException("Запрос уже рассмотрен");
        }
        if (isApproved) {
            booking.setStatus("APPROVED");
        } else {
            booking.setStatus("REJECTED");
        }
        bookingRepository.save(booking);
        return BookingMapper.toBookingDto(booking);
    }


    public BookingDto getBooking(int ownerId, int id) {
        User owner = userRepo.findUserById(ownerId);
        Booking booking = bookingRepository.findBookingById(id);

        if (owner == null || booking == null) {
            throw new NoObjectException("Неверные входные данные");
        }
        if (booking.getBooker().getId() != owner.getId() && owner.getId() != booking.getItem().getOwner().getId()) {
            throw new NoObjectException("Вы не можете посмотреть запрос");
        }
        return BookingMapper.toBookingDto(booking);

    }


    public List<BookingDto> getBookingLs(int ownerId, String state) {
        User owner = userRepo.findUserById(ownerId);
        if (owner == null) {
            throw new NoObjectException("User не найден");
        }
        return getBooks(state, ownerId, false);
    }

    public List<BookingDto> getBookingLsOwner(int ownerId, String state) {
        User owner = userRepo.findUserById(ownerId);
        if (owner == null) {
            throw new NoObjectException("User не найден");
        }
        return getBooks(state, ownerId, true);
    }


    private List<BookingDto> getBooks(String state, int ownerId, boolean isTrue) {
        List<BookingDto> answer = new ArrayList<>();
        List<Booking> bookings;
        switch (state) {
            case "ALL":
                if (isTrue) {
                    bookings = bookingRepository.findAllOrderedBookingOwner(ownerId);
                } else {
                    bookings = bookingRepository.findAllOrderedBooking();
                }
                break;
            case "PAST":
                if (isTrue) {
                    bookings = bookingRepository.findAllLastBookingOwner(ownerId);
                } else {
                    bookings = bookingRepository.findAllLastBooking();
                }
                break;
            case "CURRENT":
                if (isTrue) {
                    bookings = bookingRepository.findAllCurrentBookingOwner(ownerId);
                } else {
                    bookings = bookingRepository.findAllCurrentBooking();
                }
                break;
            case "FUTURE":
                if (isTrue) {
                    bookings = bookingRepository.findAllFutureBookingOwner(ownerId);
                } else {
                    bookings = bookingRepository.findAllFutureBooking();
                }
                break;
            case "WAITING":
                if (isTrue) {
                    bookings = bookingRepository.findAllWaitingBookingOwner(ownerId);
                } else {
                    bookings = bookingRepository.findAllWaitingBooking();
                }
                break;
            case "REJECTED":
                if (isTrue) {
                    bookings = bookingRepository.findAllRejectedBookingOwner(ownerId);
                } else {
                    bookings = bookingRepository.findAllRejectedBooking(ownerId);
                }
                break;
            default:
                throw new ErrorStatus();
        }
        for (Booking o : bookings) {
            answer.add(BookingMapper.toBookingDto(o));
        }
        return answer;
    }


    private void check(BookingDto bookingDto) {
        if (!bookingDto.getItem().getAvailable()) {
            throw new ValidationException("Item не доступна");
        }
        if (bookingDto.getStart() == null || bookingDto.getEnd() == null) {
            throw new ValidationException("Не верное время бронирования");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart()) ||
                bookingDto.getEnd().isEqual(bookingDto.getStart())) {
            throw new ValidationException("Не верное время бронирования");
        }
        if (bookingDto.getEnd().isBefore(LocalDateTime.now()) || bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Не верное время бронирования");
        }
    }
}
