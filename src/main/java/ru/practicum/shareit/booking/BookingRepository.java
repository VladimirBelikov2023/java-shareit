package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking findBookingById(int id);

    @Query("select new ru.practicum.shareit.booking.Booking(b.id, b.starting, b.ending, b.item, b.booker, b.status) from Booking as b  where b.ending < now() order by ending desc ")
    List<Booking> findAllLastBooking();

    @Query("select new ru.practicum.shareit.booking.Booking(b.id, b.starting, b.ending, b.item, b.booker, b.status) from Booking as b  where b.item.id in (select i.id from Item i where i.owner.id = ?1) and b.ending < now() order by ending desc ")
    List<Booking> findAllLastBookingOwner(int id);

    @Query("select new ru.practicum.shareit.booking.Booking(b.id, b.starting, b.ending, b.item, b.booker, b.status) from Booking as b  where b.starting > now() order by ending desc ")
    List<Booking> findAllFutureBooking();

    @Query("select new ru.practicum.shareit.booking.Booking(b.id, b.starting, b.ending, b.item, b.booker, b.status) from Booking as b  where b.item.id in (select i.id from Item i where i.owner.id = ?1)  and b.starting > now() order by ending desc ")
    List<Booking> findAllFutureBookingOwner(int id);

    @Query("select new ru.practicum.shareit.booking.Booking(b.id, b.starting, b.ending, b.item, b.booker, b.status) from Booking as b  where b.starting <= now() and b.ending > now() order by ending desc ")
    List<Booking> findAllCurrentBooking();

    @Query("select new ru.practicum.shareit.booking.Booking(b.id, b.starting, b.ending, b.item, b.booker, b.status) from Booking as b  where b.status like 'APPROVED' and b.ending < now() order by ending desc ")
    List<Booking> findAllApprovedBooking();

    @Query("select new ru.practicum.shareit.booking.Booking(b.id, b.starting, b.ending, b.item, b.booker, b.status) from Booking as b  where b.item.id in (select i.id from Item i where i.owner.id = ?1) and b.starting <= now() and b.ending > now() order by ending desc ")
    List<Booking> findAllCurrentBookingOwner(int id);

    @Query("select new ru.practicum.shareit.booking.Booking(b.id, b.starting, b.ending, b.item, b.booker, b.status) from Booking as b  where   b.status like 'WAITING' order by ending desc ")
    List<Booking> findAllWaitingBooking();

    @Query("select new ru.practicum.shareit.booking.Booking(b.id, b.starting, b.ending, b.item, b.booker, b.status) from Booking as b  where b.item.id in (select i.id from Item i where i.owner.id = ?1) and b.status like 'WAITING' order by ending desc ")
    List<Booking> findAllWaitingBookingOwner(int id);

    @Query("select new ru.practicum.shareit.booking.Booking(b.id, b.starting, b.ending, b.item, b.booker, b.status) from Booking as b  where b.booker.id = ?1 and b.status like 'REJECTED' order by ending desc ")
    List<Booking> findAllRejectedBooking(int id);

    @Query("select new ru.practicum.shareit.booking.Booking(b.id, b.starting, b.ending, b.item, b.booker, b.status) from Booking as b  where b.item.id in (select i.id from Item i where i.owner.id = ?1) and b.status like 'REJECTED' order by ending desc ")
    List<Booking> findAllRejectedBookingOwner(int id);

    @Query("select new ru.practicum.shareit.booking.Booking(b.id, b.starting, b.ending, b.item, b.booker, b.status) from Booking as b  order by ending desc ")
    List<Booking> findAllOrderedBooking();

    @Query("select new ru.practicum.shareit.booking.Booking(b.id, b.starting, b.ending, b.item, b.booker, b.status) from Booking as b where b.item.id in (select i.id from Item i where i.owner.id = ?1)  order by ending desc ")
    List<Booking> findAllOrderedBookingOwner(int id);
}
