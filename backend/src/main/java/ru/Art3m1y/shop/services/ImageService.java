package ru.Art3m1y.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.Art3m1y.shop.models.Image;
import ru.Art3m1y.shop.models.Product;
import ru.Art3m1y.shop.repositories.ImageRepository;
import ru.Art3m1y.shop.repositories.ProductRepository;

import java.io.File;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Image getRepresentationOfImageById(long id) {
        return imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Не найдено изображения с таким идентификатором"));
    }

    @Transactional
    public MediaType getMediaTypeByImageId(long id) {
        Image image = getRepresentationOfImageById(id);

        String contentType = image.getContentType().toString();

        if (contentType.equals("jpg")) {
            return MediaType.parseMediaType("image/jpeg");
        }

        return MediaType.parseMediaType("image/" + contentType);
    }

    @Transactional
    public File getImageById(long id) {
        Image image = getRepresentationOfImageById(id);

        String directoryPath = "images";

        File dir = new File(directoryPath);

        File file = new File(dir.getAbsolutePath() + File.separator + image.getOriginalFileName() + "." + image.getContentType());

        return file;
    }

    @Transactional
    public void deleteImageFromProductById(long id) {
        Image image = getRepresentationOfImageById(id);

        Product product = image.getProduct();

        product.setUpdatedAt(new Date());

        String directoryPath = "images";

        File dir = new File(directoryPath);

        File fileToDelete = new File(dir.getAbsolutePath() + File.separator + image.getOriginalFileName() + "." + image.getContentType().toString());

        if (fileToDelete.exists()) {
            fileToDelete.delete();
        }

        imageRepository.deleteById(id);

        productRepository.save(product);
    }
}
