package ru.Art3m1y.shop.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.Art3m1y.shop.dtoes.SaveProductDTO;
import ru.Art3m1y.shop.dtoes.UpdateProductDTO;
import ru.Art3m1y.shop.models.Product;
import ru.Art3m1y.shop.services.ImageService;
import ru.Art3m1y.shop.services.ProductService;

import java.util.Arrays;

import static ru.Art3m1y.shop.controllers.Helpers.checkConvertFromStringToInteger;
import static ru.Art3m1y.shop.controllers.Helpers.validateRequestBody;

@Tag(name = "Админ-панель")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final ImageService imageService;

    @Operation(summary = "Добавление нового продукта и изображений к нему", description = "При добавлении максимум можно добавить 5 изображении, минимум - одно, обязательным является изображение с названием image1")
    @PostMapping(value = "/product/add", consumes = {"application/json", "multipart/form-data"})
    public ResponseEntity<?> addProduct(@Valid @RequestPart SaveProductDTO product, BindingResult bindingResult, @RequestPart MultipartFile image1, @RequestPart(required = false) MultipartFile image2, @RequestPart(required = false) MultipartFile image3, @RequestPart(required = false) MultipartFile image4, @RequestPart(required = false) MultipartFile image5) {
        validateRequestBody(bindingResult);

        productService.saveProduct(modelMapper.map(product, Product.class), Arrays.asList(image1, image2, image3, image4, image5));

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаление продукта по его идентификатору")
    @DeleteMapping("/product/delete")
    public ResponseEntity<?> deleteProduct(@RequestBody String id) {
        checkConvertFromStringToInteger(id);

        productService.deleteProductById(Long.parseLong(id));

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Добавление одной картинки к продукту по его идентификатору")
    @PatchMapping("/product/addimagetoproduct")
    public ResponseEntity<?> addImageToProduct(@RequestPart String id, @RequestPart MultipartFile image) {
        checkConvertFromStringToInteger(id);

        productService.addImageToProductById(Long.parseLong(id), image);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаление одной картинки из продукта по его идентификатору")
    @DeleteMapping("/product/deleteimagefromproduct")
    public ResponseEntity<?> deleteImageFromProduct(@RequestBody String id) {
        checkConvertFromStringToInteger(id);

        imageService.deleteImageFromProductById(Long.parseLong(id));

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновления данных о продукте (не включая изображений продукта)")
    @PatchMapping("/product/update")
    public ResponseEntity<?> updateProduct(@Valid @RequestBody UpdateProductDTO updateProductDTO, BindingResult bindingResult) {
        validateRequestBody(bindingResult);

        productService.updateProduct(modelMapper.map(updateProductDTO, Product.class));

        return ResponseEntity.ok().build();
    }
}
