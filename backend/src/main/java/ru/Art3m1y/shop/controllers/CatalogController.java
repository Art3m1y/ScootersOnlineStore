package ru.Art3m1y.shop.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.Art3m1y.shop.dtoes.GetProductDTO;
import ru.Art3m1y.shop.dtoes.GetProductsDTO;
import ru.Art3m1y.shop.services.ProductService;

import java.util.ArrayList;
import java.util.Optional;

import static ru.Art3m1y.shop.controllers.Helpers.checkConvertFromStringToInteger;

@Tag(name = "Каталог продуктов")
@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class CatalogController {
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Operation(summary = "Получение продукта по его идентификатору")
    @GetMapping(value = "/{id}")
    public ResponseEntity<GetProductDTO> getProductById(@PathVariable String id) {
        checkConvertFromStringToInteger(id);

        return ResponseEntity.ok().body(modelMapper.map(productService.getProductById(Long.parseLong(id)), GetProductDTO.class));
    }

    @Operation(summary = "Получение списка продуктов", description = "Возможно использование пагинации в случае использование аргументов page и itemsPerPage")
    @GetMapping
    public ResponseEntity<GetProductsDTO> getProducts(@RequestParam(required = false) Optional<String> page, @RequestParam(required = false) Optional<String> itemsPerPage, @RequestParam(required = false) Optional<String> search) {
        GetProductsDTO products = new GetProductsDTO(new ArrayList<>());

        if (search.isPresent() && !search.get().isBlank()) {
            if (page.isPresent() && itemsPerPage.isPresent()) {
                checkConvertFromStringToInteger(page.get());
                checkConvertFromStringToInteger(itemsPerPage.get());

                int page_converted = Integer.parseInt(page.get()) - 1;
                int itemsPerPage_converted = Integer.parseInt(itemsPerPage.get());


                productService.getProducts(page_converted, itemsPerPage_converted, search.get()).forEach(product -> products.getProducts().add(modelMapper.map(product, GetProductDTO.class)));
            } else {
                productService.getProducts(search.get()).forEach(product -> products.getProducts().add(modelMapper.map(product, GetProductDTO.class)));
            }

            products.setAmount(productService.countProducts(search.get()));;

            return ResponseEntity.ok().body(products);
        }

        if (page.isPresent() && itemsPerPage.isPresent()) {
            checkConvertFromStringToInteger(page.get());
            checkConvertFromStringToInteger(itemsPerPage.get());

            int page_converted = Integer.parseInt(page.get()) - 1;
            int itemsPerPage_converted = Integer.parseInt(itemsPerPage.get());


            productService.getProducts(page_converted, itemsPerPage_converted).forEach(product -> products.getProducts().add(modelMapper.map(product, GetProductDTO.class)));
        } else {
            productService.getProducts().forEach(product -> products.getProducts().add(modelMapper.map(product, GetProductDTO.class)));
        }

        products.setAmount(productService.countProducts());

        return ResponseEntity.ok().body(products);
    }
}
