package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.Create;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.LastBooking;
import ru.practicum.shareit.item.model.NextBooking;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemDto {
    private int id;
    @NotBlank(groups = {Create.class})
    @NotNull(groups = {Create.class})
    private String name;
    @NotBlank(groups = {Create.class})
    @NotNull(groups = {Create.class})
    private String description;
    @NotNull(groups = {Create.class})
    private Boolean available;
    private LastBooking lastBooking;
    private NextBooking nextBooking;
    private List<Comment> comments;
}


