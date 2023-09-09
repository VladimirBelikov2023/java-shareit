package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NoObjectException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RequestServiceImpl implements RequestService {
    private final UserService userService;
    private final RequestRepository requestRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestDto createRequest(int ownerId, ItemRequestDto requestDto) {
        User user = UserMapper.toUser(userService.getUser(ownerId));
        requestDto.setRequester(user);
        if (requestDto.getDescription() == null || requestDto.getDescription().isBlank()) {
            throw new ValidationException("Не верное описание");
        }
        ItemRequest itemRequest = RequestMapper.toItemRequest(requestDto);
        return RequestMapper.toItemRequestDto(requestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getRequests(int id) {
        User user = UserMapper.toUser(userService.getUser(id));
        List<ItemRequest> itemRequests = requestRepository.getByRequesterId(id, Sort.by("created").descending());
        for (ItemRequest o : itemRequests) {
            List<ItemDto> items = ItemMapper.toLsItemDto(itemRepository.getByRequestId(o.getId()));
            o.setItems(items);
        }
        return itemRequests.stream().map(RequestMapper::toItemRequestDto).collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getRequestId(int ownerId, int id) {
        User user = UserMapper.toUser(userService.getUser(ownerId));
        ItemRequest itemRequest;
        Optional<ItemRequest> itemRequestsOpt = requestRepository.findById(id);
        if (itemRequestsOpt.isPresent()) {
            itemRequest = itemRequestsOpt.get();
        } else {
            throw new NoObjectException("Запрос не найден");
        }
        List<ItemDto> items = ItemMapper.toLsItemDto(itemRepository.getByRequestId(itemRequest.getId()));
        itemRequest.setItems(items);
        return RequestMapper.toItemRequestDto(itemRequest);
    }

    @Override
    public List<ItemRequestDto> getRequestsAnother(int id, int from, int size) {
        User user = UserMapper.toUser(userService.getUser(id));
        if (size == 0) {
            throw new ValidationException("Длина больше 0 должна быть");
        }
        List<ItemRequest> itemRequests = requestRepository.getAllByAnother(id, PageRequest.of(from, size, Sort.by("created").descending()));
        for (ItemRequest o : itemRequests) {
            List<ItemDto> items = ItemMapper.toLsItemDto(itemRepository.getByRequestId(o.getId()));
            o.setItems(items);
        }
        return itemRequests.stream().map(RequestMapper::toItemRequestDto).collect(Collectors.toList());
    }
}
