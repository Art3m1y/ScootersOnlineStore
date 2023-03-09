package ru.Art3m1y.shop.unit_tests.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.Art3m1y.shop.models.Comment;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.models.Product;
import ru.Art3m1y.shop.repositories.CommentRepository;
import ru.Art3m1y.shop.services.CommentService;
import ru.Art3m1y.shop.services.ProductService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ProductService productService;
    @InjectMocks
    private CommentService commentService;

    @Test
    void existsById_shouldThrowExceptionIfCommentDoesNotExist_whenCalled_commentExists() {
        long id = 1;
        when(commentRepository.existsById(id)).thenReturn(true);

        commentService.existsById(id);

        verify(commentRepository, times(1)).existsById(id);
    }

    @Test
    void existsById_shouldThrowExceptionIfCommentDoesNotExist_whenCalled_commentDoesNotExist() {
        long id = 1;
        when(commentRepository.existsById(id)).thenReturn(false);

        try {
            commentService.existsById(id);
            fail("Ожидалось выбрасывание RuntimeException");
        } catch (RuntimeException ignored) {
        }

        verify(commentRepository, times(1)).existsById(id);
    }

    @Test
    void saveComment_shouldSaveComment_whenUserWriteComment() {
        Comment comment = mock(Comment.class);
        Product product = mock(Product.class);
        Person person = new Person();
        when(comment.getProduct()).thenReturn(product);
        when(product.getId()).thenReturn(1L);

        commentService.saveComment(comment, person);

        verify(commentRepository, times(1)).save(comment);
        verify(comment, times(1)).setPerson(person);
        verify(comment, times(1)).setCreatedAt(any());
        verify(comment, times(1)).setUpdatedAt(any());
    }

    @Test
    void findById_shouldFindCommentById_whenCalledOnPageWithProduct_commentExists() {
        long id = 1;
        Comment comment = new Comment();
        comment.setId(1);
        when(commentRepository.findById(id)).thenReturn(Optional.of(comment));

        Comment commentGot = commentService.findById(id);

        verify(commentRepository, times(1)).findById(id);
        assertNotNull(commentGot);
        assertEquals(comment, commentGot);
        assertEquals(id, commentGot.getId());
    }

    @Test
    void findById_shouldFindCommentById_whenCalledOnPageWithProduct_commentDoesNotExist() {
        long id = 1;
        when(commentRepository.findById(id)).thenReturn(Optional.empty());
        try {
            Comment commentGot = commentService.findById(id);
            fail("Ожидалось выбрасывание RuntimeException");
        } catch (RuntimeException ignored) {}

        verify(commentRepository, times(1)).findById(id);
    }

    @Test
    void deleteCommentById_shouldDeleteCommentById_whenUserCallThis_userHasRoleAdmin() {
        long id = 1;
        Comment comment = mock(Comment.class);
        Person person = mock(Person.class);
        when(person.getRole()).thenReturn("ROLE_ADMIN");
        when(commentRepository.findById(id)).thenReturn(Optional.ofNullable(comment));

        commentService.deleteCommentById(id, person);

        verify(commentRepository, times(1)).findById(id);
        verify(commentRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteCommentById_shouldDeleteCommentById_whenUserCallThis_userHaveNotRoleAdminButCommentWasWrittenByHim() {
        long id = 1;
        Comment comment = mock(Comment.class);
        Person person = mock(Person.class);
        when(person.getRole()).thenReturn("ROLE_USER");
        when(commentRepository.findById(id)).thenReturn(Optional.ofNullable(comment));
        when(comment.getPerson()).thenReturn(person);

        commentService.deleteCommentById(id, person);

        verify(commentRepository, times(1)).findById(id);
        verify(commentRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteCommentById_shouldDeleteCommentById_whenUserCallThis_userHaveNotRoleAdminButCommentWasNotWrittenByHim() {
        long id = 1;
        Comment comment = mock(Comment.class);
        Person person = mock(Person.class);
        when(person.getRole()).thenReturn("ROLE_USER");
        when(commentRepository.findById(id)).thenReturn(Optional.ofNullable(comment));
        when(comment.getPerson()).thenReturn(new Person());

        try {
            commentService.deleteCommentById(id, person);
            fail("Ожидалось выбрасывание RuntimeException");
        } catch (RuntimeException ignored) {
        }

        verify(commentRepository, times(1)).findById(id);
    }

    @Test
    void updateCommentById_shouldUpdateCommentById_WhenUserUpdateComment_userHasRoleAdmin() {
        long id = 1;
        Comment comment = mock(Comment.class);
        Person person = mock(Person.class);
        Comment commentToUpdate = mock(Comment.class);
        when(commentRepository.findById(id)).thenReturn(Optional.ofNullable(commentToUpdate));
        when(person.getRole()).thenReturn("ROLE_ADMIN");
        when(comment.getId()).thenReturn(id);
        when(comment.getText()).thenReturn("text");
        when(comment.getMark()).thenReturn((short) 5);

        commentService.updateCommentById(comment, person);

        verify(commentRepository, times(1)).findById(id);
        verify(commentToUpdate, times(1)).setUpdatedAt(any());
        verify(commentToUpdate, times(1)).setMark(any(short.class));
        verify(commentToUpdate, times(1)).setText(any());
    }

    void updateCommentById_shouldUpdateCommentById_WhenUserUpdateComment_userHaveNotRoleAdminButCommentWasWrittenByHim() {
        long id = 1;
        Comment comment = mock(Comment.class);
        Person person = mock(Person.class);
        Comment commentToUpdate = mock(Comment.class);
        when(commentRepository.findById(id)).thenReturn(Optional.ofNullable(commentToUpdate));
        when(person.getRole()).thenReturn("ROLE_USER");
        when(comment.getId()).thenReturn(id);
        when(comment.getText()).thenReturn("text");
        when(comment.getMark()).thenReturn((short) 5);
        when(comment.getPerson()).thenReturn(person);

        commentService.updateCommentById(comment, person);

        verify(commentRepository, times(1)).findById(id);
        verify(commentRepository, times(1)).save(commentToUpdate);
        verify(commentToUpdate, times(1)).setUpdatedAt(any());
        verify(commentToUpdate, times(1)).setMark(any(short.class));
        verify(commentToUpdate, times(1)).setText(any());
    }

    void updateCommentById_shouldUpdateCommentById_WhenUserUpdateComment_userHaveNotRoleAdminButCommentWasNotWrittenByHim() {
        long id = 1;
        Comment comment = mock(Comment.class);
        Person person = mock(Person.class);
        Comment commentToUpdate = mock(Comment.class);
        when(commentRepository.findById(id)).thenReturn(Optional.ofNullable(commentToUpdate));
        when(person.getRole()).thenReturn("ROLE_USER");
        when(comment.getId()).thenReturn(id);
        when(comment.getText()).thenReturn("text");
        when(comment.getMark()).thenReturn((short) 5);
        when(comment.getPerson()).thenReturn(new Person());

        try {
            commentService.updateCommentById(comment, person);
            fail("Ожидалось выбрасывание RuntimeException");
        } catch (RuntimeException ignored) {
        }

        verify(commentRepository, times(1)).findById(id);
        verify(commentRepository, times(1)).save(commentToUpdate);
    }
}