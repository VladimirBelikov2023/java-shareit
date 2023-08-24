package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.exception.NoObjectException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.LastBooking;
import ru.practicum.shareit.item.model.NextBooking;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@AllArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepo userRepository;
    private final CommentRepo commentRepo;
    private final BookingRepository bookingRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, int id) {
        User user = userRepository.findUserById(id);
        Item item = ItemMapper.toItem(itemDto, user);
        check(item, id);
        item.setOwner(userRepository.findUserById(id));
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto patchItem(ItemDto itemDto, int id, int ownerId) {
        Item itemBase = itemRepository.findItemById(id);
        if (itemBase == null) {
            throw new NoObjectException("Item не найден в базе");
        }
        if (itemBase.getOwner().getId() != ownerId) {
            throw new NoObjectException("Это не ваша вещь");
        }
        Item item = update(itemBase, itemDto);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto getItem(int id, int ownerId) {
        try {
            Item item = itemRepository.findItemById(id);
            User user = userRepository.findUserById(ownerId);
            if (item == null || user == null) {
                throw new NoObjectException("Неверные данные");
            }
            List<Comment> comments = commentRepo.findCommentsByItem(id);

            List<Booking> bookings = itemRepository.getPastOwner(item.getId());
            if (bookings != null && bookings.size() > 0 && item.getOwner().getId() == ownerId) {
                item.setLastBooking(new LastBooking(bookings.get(0).getId(), bookings.get(0).getBooker().getId()));
            }
            bookings = itemRepository.getFutureOwner(item.getId());
            if (bookings != null && bookings.size() > 0 && item.getOwner().getId() == ownerId) {
                item.setNextBooking(new NextBooking(bookings.get(0).getId(), bookings.get(0).getBooker().getId()));
            }

            item.setComments(comments);
            return ItemMapper.toItemDto(item);
        } catch (Exception e) {
            throw new NoObjectException("Item не найден");
        }
    }

    @Override
    public List<ItemDto> getItems(int ownerId, int itemId) {
        List<ItemDto> answer = new ArrayList<>();
        List<Item> ls = itemRepository.findAll();
        ls.sort(Comparator.comparingInt(Item::getId));
        for (Item o : ls) {
            if (o.getOwner().getId() == ownerId) {
                List<Booking> bookings = itemRepository.getPastOwner(o.getId());
                if (bookings != null && bookings.size() > 0) {
                    o.setLastBooking(new LastBooking(bookings.get(0).getId(), bookings.get(0).getBooker().getId()));
                }
                bookings = itemRepository.getFutureOwner(o.getId());
                if (bookings != null && bookings.size() > 0) {
                    o.setNextBooking(new NextBooking(bookings.get(0).getId(), bookings.get(0).getBooker().getId()));
                }
                answer.add(ItemMapper.toItemDto(o));
            }
        }
        return answer;
    }

    @Override
    public List<ItemDto> search(String text) {
        List<ItemDto> itemList = new ArrayList<>();
        if (text == null || text.isEmpty() || text.isBlank()) {
            return itemList;
        }
        for (Item o : itemRepository.findAll()) {
            if ((o.getName().toLowerCase().contains(text.toLowerCase()) || o.getDescription().toLowerCase().contains(text.toLowerCase())) && o.getAvailable()) {
                ItemDto item = ItemMapper.toItemDto(o);
                itemList.add(item);
            }
        }
        return itemList;
    }


    private Item check(Item item, int id) {
        if (userRepository.findUserById(id) == null) {
            throw new NoObjectException("User не найден");
        } else if (item.getAvailable() == null) {
            throw new ValidationException("Available пустой");
        } else if (item.getName() == null || item.getName().isEmpty()) {
            throw new ValidationException("Name пустое");
        } else if (item.getDescription() == null || item.getDescription().isEmpty()) {
            throw new ValidationException("Description пустое");
        }
        return item;
    }

    private Item update(Item item, ItemDto itemDto) {
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        return item;
    }

    public Comment createComment(int ownerId, int idItem, Comment comment) {
        User user = userRepository.findUserById(ownerId);
        Item item = itemRepository.findItemById(idItem);
        List<Booking> lsBooking = bookingRepository.findAllApprovedBooking();
        Booking booking = null;
        if (user == null || item == null || comment.getText().isBlank()) {
            throw new ValidationException("Не верные данные");
        }
        for (Booking o : lsBooking) {
            if (o.getBooker().getId() == user.getId() && o.getItem().getId() == idItem) {
                booking = o;
            }
        }
        if (booking == null || booking.getBooker().getId() != ownerId) {
            throw new ValidationException("Пользователь не оставлял запрос");
        }
        comment.setAuthorName(user.getName());
        comment.setCreated(LocalDateTime.now());
        comment.setItem(idItem);
        commentRepo.save(comment);
        return comment;
    }

}
