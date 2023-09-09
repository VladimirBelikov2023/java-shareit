package ru.practicum.shareit.request;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @NotBlank
    @Column(name = "description", nullable = false, length = 4000)
    private String description;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User requester;
    @NotNull
    @Column(name = "created", nullable = false)
    private LocalDateTime created;

    @Transient
    private List<ItemDto> items;
}
