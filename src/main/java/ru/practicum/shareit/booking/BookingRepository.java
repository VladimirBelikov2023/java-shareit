package ru.practicum.shareit.booking;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends PagingAndSortingRepository<Booking, Integer> {


    List<Booking> getByItemIdAndStartingIsBeforeAndStatus(int id, LocalDateTime time, Status status, Sort sort);


    List<Booking> getByItemIdAndStartingIsAfterAndStatus(int id, LocalDateTime time, Status status, Sort sort);


    List<Booking> findByEndingIsBeforeAndBookerId(LocalDateTime end, int id, Sort sort);

    @Query("select b from Booking as b  where b.item.owner.id=?1 and b.ending < now()")
    List<Booking> findAllLastBookingOwner(int id, Sort sort);

    List<Booking> findByStartingIsAfterAndBookerIdOrderByEndingDesc(LocalDateTime end, int id);


    @Query("select b from Booking as b  where b.item.owner.id=?1 and b.starting > now()")
    List<Booking> findAllFutureBookingOwner(int id, Sort sort);


    List<Booking> findByStartingIsBeforeAndEndingIsAfterAndBookerId(LocalDateTime time, LocalDateTime time2, int id, Sort sort);

    List<Booking> findByStatusAndEndingIsBefore(Status status, LocalDateTime time, Sort sort);

    @Query("select b from Booking as b  where b.item.owner.id=?1 and b.starting <= now() and b.ending > now()")
    List<Booking> findAllCurrentBookingOwner(int id, Sort sort);

    List<Booking> findByStatusAndBookerId(Status status, int id, Sort sort);

    @Query("select b from Booking as b  where b.item.owner.id=?1 and b.status like 'WAITING'")
    List<Booking> findAllWaitingBookingOwner(int id, Sort sort);


    List<Booking> findByBookerIdAndStatus(int id, Status status, Sort sort);

    @Query("select b from Booking as b  where b.item.owner.id=?1 and b.status like 'REJECTED'")
    List<Booking> findAllRejectedBookingOwner(int id, Sort sort);

    List<Booking> findByBookerId(int id, Sort sort);

    @Query("select b from Booking as b where b.item.owner.id=?1")
    List<Booking> findAllOrderedBookingOwnerPag(int id, Pageable pageable);

    @Query("select b from Booking as b where b.item.owner.id=?1")
    List<Booking> findAllOrderedBookingOwner(int id, Sort sort);

    List<Booking> findByBookerId(int id, Pageable pageable);
}
