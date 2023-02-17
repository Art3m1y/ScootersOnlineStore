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

    @Transactional
    public void productExistsById(long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Продукт с таким идентификатором не найден");
        }
    }

    @Transactional
    public void saveProduct(Product product, List<MultipartFile> images) {
        product.setCreatedAt(new Date());
        product.setUpdatedAt(new Date());

        productRepository.save(product);

        final boolean[] flag = {false};

        if (images.size() != 0) {
            images.forEach(image -> {
                if (!(image == null) && image.getSize() != 0) {
                    saveImageLocalAndToDB(image, product);
                }
            });
        }
    }

    @Transactional
    public Product getProductById(long id) {
        return productRepository.findById(id).orElseThrow(() -> new RuntimeException("Продукт с таким идентификатором не найден"));
    }

    @Transactional
    public long countProducts() {
        return productRepository.count();
    }

    @Transactional
    public long countProducts(String search) {
        return productRepository.countAllByNameContainsIgnoreCase(search);
    }

    @Transactional
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Transactional
    public List<Product> getProducts(int page, int itemsPerPage) {
        return productRepository.findAll(PageRequest.of(page, itemsPerPage)).getContent();
    }

    @Transactional
    public List<Product> getProducts(int page, int itemsPerPage, String search) {
        return productRepository.findAllByNameContainsIgnoreCase(PageRequest.of(page, itemsPerPage), search).getContent();

    }

    @Transactional
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

        saveImageLocalAndToDB(image, product);
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

    private void saveImage(MultipartFile image, String name, String extension) throws IOException {
        byte[] bytes = image.getBytes();

        String directoryPath = "images";

        File dir = new File(directoryPath);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File uploadedFile = new File(dir.getAbsolutePath() + File.separator + name + "." + extension);

        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile));

        stream.write(bytes);
        stream.flush();
        stream.close();
    }

    private static String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }

    private void saveImageLocalAndToDB(MultipartFile image, Product product) {
        try {
            String name = UUID.randomUUID().toString();
            String extension = getFileExtension(image);

            saveImage(image, name, extension);

            Image img = new Image(ContentType.valueOf(extension), name);

            img.setCreatedAt(new Date());

            product.addImageToProduct(img);

            product.setUpdatedAt(new Date());

            productRepository.save(product);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
