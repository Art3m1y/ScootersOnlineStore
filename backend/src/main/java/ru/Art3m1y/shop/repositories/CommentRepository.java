package ru.Art3m1y.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.Art3m1y.shop.models.Comment;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
}
