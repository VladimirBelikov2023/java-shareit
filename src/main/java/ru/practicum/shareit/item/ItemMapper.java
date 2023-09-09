package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.ArrayList;
import java.util.List;


@AllArgsConstructor
@Service
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        int requestId = 0;
        if (item.getRequest() != null) {
            requestId = item.getRequest().getId();
        }
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getLastBooking(),
                item.getNextBooking(),
                item.getComments(),
                requestId
        );
    }

    public static List<ItemDto> toLsItemDto(List<Item> ls) {
        List<ItemDto> itemDto = new ArrayList<>();
        for (Item o : ls) {
            itemDto.add(ItemMapper.toItemDto(o));
        }
        return itemDto;
    }


    public static Item toItem(ItemDto itemDto) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .lastBooking(itemDto.getLastBooking())
                .nextBooking(itemDto.getNextBooking())
                .comments(itemDto.getComments()).build();
    }

}
