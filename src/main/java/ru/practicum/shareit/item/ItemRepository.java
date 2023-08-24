package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Item findItemById(int id);

    @Query("select new ru.practicum.shareit.booking.Booking(b.id, b.starting, b.ending, b.item, b.booker, b.status)" +
            " from Booking as b  where b.item.id = ?1 and b.starting < now()  and b.status like 'APPROVED' order by b.ending desc")
    List<Booking> getPastOwner(int id);

    @Query("select new ru.practicum.shareit.booking.Booking(b.id, b.starting, b.ending, b.item, b.booker, b.status)" +
            " from Booking as b  where b.item.id = ?1 and b.starting > now() and b.status like 'APPROVED' order by b.starting")
    List<Booking> getFutureOwner(int id);
}
