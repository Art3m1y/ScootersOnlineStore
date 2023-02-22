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
import org.springframework.web.multipart.MultipartFile;
import ru.Art3m1y.shop.dtoes.SaveProductDTO;
import ru.Art3m1y.shop.dtoes.UpdatePersonFromAdminDTO;
import ru.Art3m1y.shop.dtoes.UpdatePersonFromPersonDTO;
import ru.Art3m1y.shop.dtoes.UpdateProductDTO;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.models.Product;
import ru.Art3m1y.shop.security.PersonDetails;
import ru.Art3m1y.shop.services.ImageService;
import ru.Art3m1y.shop.services.PersonService;
import ru.Art3m1y.shop.services.ProductService;
import ru.Art3m1y.shop.utils.validators.PersonValidator;

import java.util.Arrays;

import static ru.Art3m1y.shop.controllers.Helpers.*;

@Tag(name = "Админ-панель")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final ImageService imageService;
    private final PersonService personService;
    private final PersonValidator personValidator;

    @Operation(summary = "Добавление нового продукта и изображений к нему", description = "При добавлении максимум можно добавить 5 изображении, минимум - одно, обязательным является изображение с названием image1")
    @PostMapping(value = "/product/add", consumes = {"application/json", "multipart/form-data"})
    public ResponseEntity<?> addProduct(@RequestPart("product") @Valid SaveProductDTO saveProductDTO, BindingResult bindingResult, @RequestPart MultipartFile image1, @RequestPart(required = false) MultipartFile image2, @RequestPart(required = false) MultipartFile image3, @RequestPart(required = false) MultipartFile image4, @RequestPart(required = false) MultipartFile image5) {
        validateRequestBody(bindingResult);

        productService.saveProduct(modelMapper.map(saveProductDTO, Product.class), Arrays.asList(image1, image2, image3, image4, image5));

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаление продукта по его идентификатору")
    @DeleteMapping("/product/delete")
    public ResponseEntity<?> deleteProduct(@RequestBody String id) {
        checkConvertFromStringToLong(id);

        productService.deleteProductById(Long.parseLong(id));

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Добавление одной картинки к продукту по его идентификатору")
    @PatchMapping("/product/addimagetoproduct")
    public ResponseEntity<?> addImageToProduct(@RequestPart String id, @RequestPart MultipartFile image) {
        checkConvertFromStringToLong(id);

        productService.addImageToProductById(Long.parseLong(id), image);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаление одной картинки из продукта по его идентификатору")
    @DeleteMapping("/product/deleteimagefromproduct")
    public ResponseEntity<?> deleteImageFromProduct(@RequestBody String id) {
        checkConvertFromStringToLong(id);

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

    @Operation(summary = "Обновление информации о пользователе (включая аватар)")
    @PatchMapping("/person/update")
    public ResponseEntity<?> updatePerson(@RequestPart("person") @Valid UpdatePersonFromAdminDTO updatePersonFromAdminDTO, BindingResult bindingResult, @RequestPart(required = false) MultipartFile avatar) {

        Person person = modelMapper.map(updatePersonFromAdminDTO, Person.class);

        if (person.getId() == 0) {
            throw new RuntimeException("Значение идентификатора должно быть больше 0");
        }

        Person personToUpdate = personService.findById(person.getId());

        if ((person.getEmail() != null) && (!person.getEmail().equals(personToUpdate.getEmail()))) {
            personValidator.validate(person, bindingResult);
        }

        validateRequestBody(bindingResult);

        personService.updatePerson(person, avatar);

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удаление профиля пользователя с помощью его идентификатора")
    @DeleteMapping("/person/delete")
    public ResponseEntity<?> deletePerson(@RequestBody String id) {
        checkConvertFromStringToLong(id);

        personService.deletePersonById(Long.parseLong(id));

        return ResponseEntity.ok().build();
    }
}
