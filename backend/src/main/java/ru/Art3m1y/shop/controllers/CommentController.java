package ru.Art3m1y.shop.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.Art3m1y.shop.dtoes.AddCommentDTO;
import ru.Art3m1y.shop.dtoes.UpdateCommentDTO;
import ru.Art3m1y.shop.models.Comment;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.models.Product;
import ru.Art3m1y.shop.security.PersonDetails;
import ru.Art3m1y.shop.services.CommentService;

import static ru.Art3m1y.shop.controllers.Helpers.checkConvertFromStringToLong;
import static ru.Art3m1y.shop.controllers.Helpers.validateRequestBody;

@Tag(name = "Контроллер комментариев")
@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CommentController {
    private final CommentService commentService;
    private final ModelMapper modelMapper;

    @Operation(summary = "Добавление комментария")
    @PostMapping("/add")
    public ResponseEntity<?> addComment(@Valid @RequestBody AddCommentDTO addCommentDTO, BindingResult bindingResult) {
        validateRequestBody(bindingResult);

        Person person = ((PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();

        Comment comment = new Comment(new Product(addCommentDTO.getId()), addCommentDTO.getMark(), addCommentDTO.getText());

        commentService.saveComment(comment, person);

        return ResponseEntity.ok().build();
    }

        @Operation(summary = "Удаление комментария по идентификатору комментария")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteComment(@RequestBody String id) {
        checkConvertFromStringToLong(id);

        long id_converted = Long.parseLong(id);

        Person person = ((PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();

        commentService.deleteCommentById(id_converted, person);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновление комментария по его идентификатору")
    @PatchMapping("/update")
    public ResponseEntity<?> updateComment(@RequestBody UpdateCommentDTO updateCommentDTO, BindingResult bindingResult) {
        validateRequestBody(bindingResult);

        Person person = ((PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();

        commentService.updateCommentById(modelMapper.map(updateCommentDTO, Comment.class), person);

        return  ResponseEntity.ok().build();
    }
}
