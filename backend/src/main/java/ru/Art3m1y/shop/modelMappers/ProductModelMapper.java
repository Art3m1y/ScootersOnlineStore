package ru.Art3m1y.shop.modelMappers;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.Art3m1y.shop.dtoes.GetProductDTO;
import ru.Art3m1y.shop.dtoes.SaveProductDTO;
import ru.Art3m1y.shop.dtoes.UpdateProductDTO;
import ru.Art3m1y.shop.models.Product;

@Component
@RequiredArgsConstructor
public class ProductModelMapper {
    private final ModelMapper modelMapper;

    public Product mapToProduct(SaveProductDTO saveProductDTO) {
        return modelMapper.map(saveProductDTO, Product.class);
    }

    public Product mapToProduct(UpdateProductDTO updateProductDTO) {
        return modelMapper.map(updateProductDTO, Product.class);
    }

    public GetProductDTO mapToGetProductDTO(Product product) {
        return modelMapper.map(product, GetProductDTO.class);
    }
}
