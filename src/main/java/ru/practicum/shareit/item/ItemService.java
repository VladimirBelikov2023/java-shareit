package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Item item, int id);

    ItemDto patchItem(Item item, int id, int ownerId);

    ItemDto getItem(int id);

    List<ItemDto> getItems(int id);

    List<ItemDto> search(String text);
}
