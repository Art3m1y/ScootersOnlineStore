package ru.Art3m1y.shop.controllers;

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

import static ru.Art3m1y.shop.controllers.Helpers.checkConvertFromStringToInteger;
import static ru.Art3m1y.shop.controllers.Helpers.validateRequestBody;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class CommentController {
    private final CommentService commentService;
    private final ModelMapper modelMapper;

    @PostMapping("/add")
    public ResponseEntity<?> addComment(@Valid @RequestBody AddCommentDTO addCommentDTO, BindingResult bindingResult) {
        validateRequestBody(bindingResult);

        Person person = ((PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();

        Comment comment = new Comment(new Product(addCommentDTO.getId()), addCommentDTO.getMark(), addCommentDTO.getText());

        commentService.saveComment(comment, person);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteComment(@RequestBody String id) {
        checkConvertFromStringToInteger(id);

        long id_converted = Long.parseLong(id);

        Person person = ((PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();

        commentService.deleteCommentById(id_converted, person);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/update")
    public ResponseEntity<?> updateComment(@RequestBody UpdateCommentDTO updateCommentDTO, BindingResult bindingResult) {
        validateRequestBody(bindingResult);

        Person person = ((PersonDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPerson();

        commentService.updateCommentById(modelMapper.map(updateCommentDTO, Comment.class), person);

        return  ResponseEntity.ok().build();
    }
}
