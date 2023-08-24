package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Booking {
    private Integer id;
    @NotNull
    private LocalDate start;
    @NotNull
    private LocalDate end;
    @NotNull
    private Item item;
    @NotNull
    private Item booker;
    @NotNull
    private Status status;
}
