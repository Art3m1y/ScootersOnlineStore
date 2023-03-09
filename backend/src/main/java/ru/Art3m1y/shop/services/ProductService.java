package ru.Art3m1y.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.Art3m1y.shop.models.Image;
import ru.Art3m1y.shop.models.Product;
import ru.Art3m1y.shop.repositories.ImageRepository;
import ru.Art3m1y.shop.repositories.ProductRepository;
import ru.Art3m1y.shop.utils.enums.ContentType;
import java.io.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class    ProductService {
    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    public void productExistsById(long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Продукт с таким идентификатором не найден");
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveProduct(Product product, List<MultipartFile> images) {
        product.setCreatedAt(new Date());
        product.setUpdatedAt(new Date());

        productRepository.save(product);

        final boolean[] flag = {false};

        if (images.size() != 0) {
            images.forEach(image -> {
                if ((image != null) && image.getSize() != 0) {
                    imageService.saveImage(image, product);
                }
            });
        }
    }

    @Transactional(readOnly = true)
    public Product getProductById(long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Продукт с таким идентификатором не найден"));
    }

    @Transactional(readOnly = true)
    public long countProducts() {
        return productRepository.count();
    }

    @Transactional(readOnly = true)
    public long countProducts(String search) {
        return productRepository.countAllByNameContainsIgnoreCase(search);
    }

    @Transactional(readOnly = true)
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Product> getProducts(int page, int itemsPerPage) {
        return productRepository.findAll(PageRequest.of(page, itemsPerPage)).getContent();
    }

    @Transactional(readOnly = true)
    public List<Product> getProducts(int page, int itemsPerPage, String search) {
        return productRepository.findAllByNameContainsIgnoreCase(PageRequest.of(page, itemsPerPage), search).getContent();

    }

    @Transactional(readOnly = true)
    public List<Product> getProducts(String search) {
        return productRepository.findAllByNameContainsIgnoreCase(search);
    }

    @Transactional
    public void deleteProductById(long id) {
        List<Image> images = imageRepository.findAllByProduct(getProductById(id));

        images.forEach(image -> {
            String directoryPath = "images";

            File dir = new File(directoryPath);

            File fileToDelete = new File(dir.getAbsolutePath() + File.separator + image.getOriginalFileName() + "." + image.getContentType());

            fileToDelete.delete();
        });

        productRepository.deleteById(id);
    }

    @Transactional
    public void addImageToProductById(long id, MultipartFile image) {
        if ((image == null) || image.getSize() == 0) {
            throw new RuntimeException("Вы не добавили изображение к продукту, либо добавили его неправильно");
        }

        Product product = getProductById(id);

        imageService.saveImage(image, product);
    }

    @Transactional
    public void updateProduct(Product product) {
        Product productToUpdate = getProductById(product.getId());

        productToUpdate.setName(product.getName());
        productToUpdate.setCost(product.getCost());
        productToUpdate.setDescription(product.getDescription());
        productToUpdate.setUpdatedAt(new Date());
        productToUpdate.setPower(product.getPower());
        productToUpdate.setBatteryCapacity(product.getBatteryCapacity());
        productToUpdate.setTime(product.getTime());
        productToUpdate.setSpeed(product.getSpeed());

        productRepository.save(productToUpdate);
    }
}
