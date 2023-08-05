package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NoObjectException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ItemStorageImpl implements ItemStorage {
    private Map<Integer, Item> items = new HashMap<>();
    private int id = 1;

    @Override
    public ItemDto createItem(Item item) {
        item.setId(id);
        id++;
        items.put(item.getId(), item);
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    @Override
    public ItemDto patchItem(Item item, int ownerId) {
        if (items.get(item.getId()).getOwner().getId() != ownerId) {
            throw new NoObjectException("Это не ваша вещь");
        }
        Item originItem = update(item);
        items.put(originItem.getId(), originItem);
        return new ItemDto(
                originItem.getId(),
                originItem.getName(),
                originItem.getDescription(),
                originItem.getAvailable()
        );
    }

    @Override
    public ItemDto getItem(int id) {
        Item item = items.get(id);
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    @Override
    public List<ItemDto> getItems(int id) {
        List<ItemDto> itemList = new ArrayList<>();
        for (Item o : items.values()) {
            if (o.getOwner().getId() == id) {
                ItemDto item = new ItemDto(
                        o.getId(),
                        o.getName(),
                        o.getDescription(),
                        o.getAvailable()
                );
                itemList.add(item);
            }
        }
        return itemList;
    }

    @Override
    public List<ItemDto> search(String text) {
        List<ItemDto> itemList = new ArrayList<>();
        if (text == null || text.isEmpty() || text.isBlank()) {
            return itemList;
        }
        for (Item o : items.values()) {
            if ((o.getName().toLowerCase().contains(text.toLowerCase()) || o.getDescription().toLowerCase().contains(text.toLowerCase())) && o.getAvailable()) {
                ItemDto item = new ItemDto(
                        o.getId(),
                        o.getName(),
                        o.getDescription(),
                        o.getAvailable()
                );
                itemList.add(item);
            }
        }
        return itemList;
    }

    private Item update(Item item) {
        Item originItem = items.get(item.getId());
        if (item.getName() != null) {
            originItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            originItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            originItem.setAvailable(item.getAvailable());
        }
        return originItem;
    }
}
