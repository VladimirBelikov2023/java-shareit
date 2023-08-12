package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ItemRequest {
    private Integer id;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    private User requester;
    @NotNull
    private LocalDate created;
}
