package ru.Art3m1y.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.Art3m1y.shop.models.Comment;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.repositories.CommentRepository;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ProductService productService;

    @Transactional(readOnly = true)
    public void existsById(long id) {
        if (!commentRepository.existsById(id)) {
            throw new RuntimeException("Комментарий с таким идентификатором не найден");
        }
    }

    @Transactional
    public void saveComment(Comment comment, Person person) {
        productService.productExistsById(comment.getProduct().getId());

        comment.setPerson(person);
        comment.setCreatedAt(new Date());
        comment.setUpdatedAt(new Date());

        commentRepository.save(comment);
    }

    @Transactional(readOnly = true)
    public Comment findById(long id) {
        return commentRepository.findById(id).orElseThrow(() -> new RuntimeException("Комментарий с таким идентификатором не найден"));
    }

    @Transactional
    public void deleteCommentById(long id, Person person) {
        Comment comment = findById(id);

        if (!person.getRole().equals("ROLE_ADMIN")) {
            if (!comment.getPerson().equals(person)) {
                throw new RuntimeException("Пользователь, который пытается удалить комментарий не является его автором, а также администратором");
            }
        }

        commentRepository.deleteById(id);
     }

    @Transactional
    public void updateCommentById(Comment comment, Person person) {
        long id = comment.getId();

        Comment commentToUpdate = findById(id);
        commentToUpdate.setText(comment.getText());
        commentToUpdate.setMark(comment.getMark());
        commentToUpdate.setUpdatedAt(new Date());

        if (!person.getRole().equals("ROLE_ADMIN")) {
            if (!commentToUpdate.getPerson().equals(person)) {
                throw new RuntimeException("Пользователь, который пытается обновить комментарий не является его автором, а также администратором");
            }
        }

        commentRepository.save(commentToUpdate);
    }
}
