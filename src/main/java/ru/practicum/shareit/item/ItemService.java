package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface ItemService {
    ItemDto createItem(ItemDto item, int id);

    ItemDto patchItem(ItemDto item, int id, int ownerId);

    ItemDto getItem(int id, int ownerId);

    List<ItemDto> getItems(int ownerId, int itemId);

    List<ItemDto> search(String text);

    Comment createComment(int ownerId, int idItem, Comment comment);
}
