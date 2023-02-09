package ru.Art3m1y.shop.modelMappers;

import lombok.RequiredArgsConstructor;
import org.hibernate.sql.Update;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.Art3m1y.shop.dtoes.AddCommentDTO;
import ru.Art3m1y.shop.dtoes.AuthenticationPersonDTO;
import ru.Art3m1y.shop.dtoes.ProductDTO;
import ru.Art3m1y.shop.dtoes.UpdateCommentDTO;
import ru.Art3m1y.shop.models.Comment;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.models.Product;

@RequiredArgsConstructor
@Component
public class CommentModelMapper {
    private final ModelMapper modelMapper;

    public Product MapToProduct(ProductDTO productDTO) {
        return modelMapper.map(productDTO, Product.class);
    }

    public Comment MapToComment(AddCommentDTO addCommentDTO) {
        return modelMapper.map(addCommentDTO, Comment.class);
    }
    public Comment MapToComment(UpdateCommentDTO updateCommentDTO) {
        return modelMapper.map(updateCommentDTO, Comment.class);
    }
}
