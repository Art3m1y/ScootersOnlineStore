package ru.Art3m1y.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.Art3m1y.shop.models.Comment;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.models.Product;
import ru.Art3m1y.shop.repositories.CommentRepository;
import ru.Art3m1y.shop.repositories.ProductRepository;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ProductService productService;

    public void existsById(long id) {
        if (!commentRepository.existsById(id)) {
            throw new RuntimeException("Комментарий с таким идентификатором не найден");
        }
    }

    public void saveComment(Comment comment, Person person) {
        productService.productExistsById(comment.getProduct().getId());

        comment.setPerson(person);
        comment.setCreatedAt(new Date());
        comment.setUpdatedAt(new Date());

        commentRepository.save(comment);
    }

    public Comment findById(long id) {
        return commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Комментарий с таким идентификатором не найден"));
    }

    public void deleteCommentById(long id, Person person) {
        Comment comment = findById(id);

        if (!person.getRole().equals("ROLE_ADMIN")) {
            if (!comment.getPerson().equals(person)) {
                throw new RuntimeException("Пользователь, который пытается удалить комментарий не является его автором, а также администратором");
            }
        }

        commentRepository.deleteById(id);
     }

    public void updateCommentById(Comment comment, Person person) {
        long id = comment.getId();

        Comment commentToUpdate = findById(id);
        commentToUpdate.setText(comment.getText());
        commentToUpdate.setMark(comment.getMark());

        if (!person.getRole().equals("ROLE_ADMIN")) {
            if (!commentToUpdate.getPerson().equals(person)) {
                throw new RuntimeException("Пользователь, который пытается обновить комментарий не является его автором, а также администратором");
            }
        }

        commentRepository.save(commentToUpdate);
    }
}
