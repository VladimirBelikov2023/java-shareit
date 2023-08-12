package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NoObjectException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserStorage;

import java.util.List;

@AllArgsConstructor
@Service
public class ItemServiceImpl implements ItemService {
    private final UserStorage userStorage;
    private final ItemStorage itemStorage;

    @Override
    public ItemDto createItem(ItemDto itemDto, int id) {
        Item item = ItemMapper.toItem(itemDto);
        check(item, id);
        item.setOwner(userStorage.getUser(id));
        return itemStorage.createItem(item);
    }

    @Override
    public ItemDto patchItem(ItemDto itemDto, int id, int ownerId) {
        Item item = ItemMapper.toItem(itemDto);
        item.setId(id);
        return itemStorage.patchItem(item, ownerId);
    }

    @Override
    public ItemDto getItem(int id) {
        try {
            return itemStorage.getItem(id);
        } catch (Exception e) {
            throw new NoObjectException("Item не найден");
        }
    }

    @Override
    public List<ItemDto> getItems(int id) {
        try {
            return itemStorage.getItems(id);
        } catch (Exception e) {
            throw new NoObjectException("User не найден");
        }
    }

    @Override
    public List<ItemDto> search(String text) {
        return itemStorage.search(text);
    }

    private Item check(Item item, int id) {
        if (userStorage.getUser(id) == null) {
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

}
