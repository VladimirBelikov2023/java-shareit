package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Comment;

import java.util.List;

public interface CommentRepo extends JpaRepository<Comment, Integer> {

    @Query("select new ru.practicum.shareit.item.model.Comment(b.id, b.text, b.authorName ,  b.created, b.item) " +
            "from Comment as b  where b.item = ?1")
    List<Comment> findCommentsByItem(int id);
}
