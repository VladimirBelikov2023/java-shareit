package ru.practicum.shareit.booking;

import lombok.experimental.UtilityClass;
import ru.practicum.shareit.booking.dto.BookingDto;

@UtilityClass
public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                0,
                booking.getStarting(),
                booking.getEnding(),
                booking.getItem(),
                booking.getBooker(),
                booking.getStatus()
        );
    }


    public static Booking toBooking(BookingDto bookingDto) {
        return new Booking(
                0,
                bookingDto.getStart(),
                bookingDto.getEnd(),
                bookingDto.getItem(),
                bookingDto.getBooker(),
                bookingDto.getStatus()
        );
    }
}
