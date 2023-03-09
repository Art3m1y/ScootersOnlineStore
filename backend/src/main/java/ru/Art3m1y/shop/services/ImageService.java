package ru.Art3m1y.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.Art3m1y.shop.models.Image;
import ru.Art3m1y.shop.models.Product;
import ru.Art3m1y.shop.repositories.ImageRepository;
import ru.Art3m1y.shop.repositories.ProductRepository;
import ru.Art3m1y.shop.utils.enums.ContentType;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void saveImage(MultipartFile image, Product product) {
        workWithImage(image, product);
    }

    @Transactional(readOnly = true)
    public Image getRepresentationOfImageById(long id) {
        return imageRepository.findById(id).orElseThrow(() -> new RuntimeException("Не найдено изображения с таким идентификатором"));
    }

    @Transactional(readOnly = true)
    public MediaType getMediaTypeByImageId(long id) {
        Image image = getRepresentationOfImageById(id);

        String contentType = image.getContentType().toString();

        if (contentType.equals("jpg")) {
            return MediaType.parseMediaType("image/jpeg");
        }

        return MediaType.parseMediaType("image/" + contentType);
    }

    @Transactional(readOnly = true)
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

    //Для скрипта для очистки не нужных изображений
    @Transactional(readOnly = true)
    public List<String> getAllImagesNames() {
        List<String> names = new ArrayList<>();

        imageRepository.findAll().forEach(image -> names.add(image.getOriginalFileName() + "." + image.getContentType()));

        return names;
    }

    private void workWithImage(MultipartFile image, Product product) {
        try {
            String name = UUID.randomUUID().toString();
            String extension = getFileExtension(image);

            saveLocally(image, name, extension, "images");

            Image img = new Image(ContentType.valueOf(extension), name);

            img.setCreatedAt(new Date());

            product.addImageToProduct(img);

            product.setUpdatedAt(new Date());

            productRepository.save(product);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void saveLocally(MultipartFile image, String name, String extension, String directoryPath) throws IOException {
        byte[] bytes = image.getBytes();

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

    private String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }
}
