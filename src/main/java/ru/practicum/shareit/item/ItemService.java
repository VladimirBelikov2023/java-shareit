package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto item, int id);

    ItemDto patchItem(ItemDto item, int id, int ownerId);

    ItemDto getItem(int id);

    List<ItemDto> getItems(int id);

    List<ItemDto> search(String text);
}
