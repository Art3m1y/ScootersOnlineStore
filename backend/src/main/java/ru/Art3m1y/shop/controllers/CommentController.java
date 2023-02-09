package ru.Art3m1y.shop.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.Art3m1y.shop.dtoes.AddCommentDTO;
import ru.Art3m1y.shop.dtoes.UpdateCommentDTO;
import ru.Art3m1y.shop.modelMappers.CommentModelMapper;
import ru.Art3m1y.shop.models.Comment;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.models.Product;
import ru.Art3m1y.shop.security.PersonDetails;
import ru.Art3m1y.shop.services.CommentService;
import ru.Art3m1y.shop.utils.exceptions.*;
import ru.Art3m1y.shop.utils.other.Helpers;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CommentController {
    private final CommentService commentService;
    private final CommentModelMapper commentModelMapper;
    private final Helpers helpers;

    @PostMapping("/add")
    public ResponseEntity<?> addComment(@Valid @RequestBody AddCommentDTO addCommentDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> errors.append(error.getField()).append(" ").append(error.getDefaultMessage()).append("; "));
            throw new AddCommentException(errors.toString());
        }

        Person person = ((PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();

        Comment comment = new Comment(new Product(addCommentDTO.getId()), addCommentDTO.getMark(), addCommentDTO.getText());

        commentService.saveComment(comment, person);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteComment(@RequestBody String id) {
        if (!helpers.checkConvertFromStringToLong(id)) {
            throw new DeleteCommentException("Не удалось преобразовать строковое значение идентификатора продукта в лонг-формат");
        }

        long id_converted = Long.parseLong(id);

        Person person = ((PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();

        commentService.deleteCommentById(id_converted, person);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateComment(@RequestBody UpdateCommentDTO updateCommentDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errors = new StringBuilder();
            bindingResult.getFieldErrors().forEach(error -> errors.append(error.getField()).append(" ").append(error.getDefaultMessage()).append("; "));
            throw new UpdateCommentException(errors.toString());
        }

        Person person = ((PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();

        commentService.updateCommentById(commentModelMapper.MapToComment(updateCommentDTO), person);

        return  ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({AddCommentException.class, ProductNotFoundException.class, CommentNotFoundException.class, DeleteCommentException.class, UpdateCommentException.class})
    private ResponseEntity<ErrorResponse> handlerException(RuntimeException e) {
        ErrorResponse response = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
