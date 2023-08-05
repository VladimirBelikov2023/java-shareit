package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {
    ItemDto createItem(Item item);

    ItemDto patchItem(Item item, int ownerId);

    ItemDto getItem(int id);

    List<ItemDto> getItems(int id);

    List<ItemDto> search(String text);
}
