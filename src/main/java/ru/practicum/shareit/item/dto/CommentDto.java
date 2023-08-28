package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class CommentDto {
    private int id;
    @NotNull
    private String text;

    private String authorName;
    @NotNull
    private final LocalDateTime created = LocalDateTime.now();
    @NotNull
    private int itemId;
}
