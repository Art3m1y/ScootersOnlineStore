package ru.Art3m1y.shop.services;

import org.springframework.stereotype.Service;
import ru.Art3m1y.shop.models.Image;
import ru.Art3m1y.shop.models.Product;
import ru.Art3m1y.shop.repositories.ImageRepository;
import ru.Art3m1y.shop.repositories.ProductRepository;
import ru.Art3m1y.shop.utils.enums.ContentType;
import ru.Art3m1y.shop.utils.exceptions.GetImageException;

import java.io.File;
import java.util.Date;
import java.util.Optional;

@Service
public class ImageService {
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;

    public ImageService(ImageRepository imageRepository,
                        ProductRepository productRepository) {
        this.imageRepository = imageRepository;
        this.productRepository = productRepository;
    }

    public Image getRepresentationOfImageById(long id) {
        Optional<Image> optionalImage = imageRepository.findById(id);

        if (optionalImage.isEmpty()) {
            throw new GetImageException("Не найдено изображения с таким идентификатором");
        }

        Image image = optionalImage.get();

        return image;
    }

    public File getImageById(long id) {
        Image image = getRepresentationOfImageById(id);

        String directoryPath = "images";

        File dir = new File(directoryPath);

        File file = new File(dir.getAbsolutePath() + File.separator + image.getOriginalFileName() + "." + image.getContentType());

        return file;
    }

    public void deleteImageFromProductById(long id) {
        Image image = getRepresentationOfImageById(id);

        Product product = image.getProduct();

        product.setUpdatedAt(new Date());

        String directoryPath = "images";

        File dir = new File(directoryPath);

        File fileToDelete = new File(dir.getAbsolutePath() + File.separator + image.getOriginalFileName() + "." + image.getContentType());

        if (fileToDelete.exists()) {
            fileToDelete.delete();
        }

        imageRepository.deleteById(id);



        productRepository.save(product);
    }
}
