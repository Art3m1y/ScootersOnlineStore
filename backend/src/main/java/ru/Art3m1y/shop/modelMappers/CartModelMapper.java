package ru.Art3m1y.shop.modelMappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.Art3m1y.shop.dtoes.AddProductToCartDTO;
import ru.Art3m1y.shop.dtoes.GetCartDTO;
import ru.Art3m1y.shop.dtoes.GetProductDTO;
import ru.Art3m1y.shop.models.Cart;

@Component
@RequiredArgsConstructor
public class CartModelMapper {
    private final ModelMapper modelMapper;

    public Cart MapToCart(AddProductToCartDTO addProductToCartDTO) {
        return modelMapper.map(addProductToCartDTO, Cart.class);
    }

    public GetCartDTO MapToGetCartDTO(Cart cart) {
        return modelMapper.map(cart, GetCartDTO.class);
    }
}
