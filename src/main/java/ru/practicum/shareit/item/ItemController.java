package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.Update;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") int id, @RequestBody @Validated(Create.class) ItemDto item) {
        return itemService.createItem(item, id);
    }

    @PatchMapping("{id}")
    public ItemDto patchItem(@RequestHeader("X-Sharer-User-Id") int ownerId, @RequestBody @Validated(Update.class) ItemDto item, @PathVariable int id) {
        return itemService.patchItem(item, id, ownerId);
    }

    @GetMapping("{id}")
    public ItemDto getItem(@PathVariable int id) {
        return itemService.getItem(id);
    }

    @GetMapping()
    public List<ItemDto> getItems(@RequestHeader("X-Sharer-User-Id") int id) {
        return itemService.getItems(id);
    }

    @GetMapping("/search")
    public List<ItemDto> search(@RequestParam String text) {
        return itemService.search(text);
    }
}
