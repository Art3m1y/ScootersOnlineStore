package ru.Art3m1y.shop.services;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.Art3m1y.shop.models.Avatar;
import ru.Art3m1y.shop.models.Person;
import ru.Art3m1y.shop.repositories.AvatarRepository;
import ru.Art3m1y.shop.repositories.PersonRepository;
import ru.Art3m1y.shop.utils.enums.ContentType;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AvatarService {
    private final AvatarRepository avatarRepository;
    private final PersonRepository personRepository;

    @Transactional
    public void saveAvatar(Person person, MultipartFile avatar) {
        workWithAvatar(avatar, person);
    }

    @Transactional
    public void updateAvatar(Person person, MultipartFile avatar) {
        if (person.getAvatar() != null) {
            deleteAvatarByPerson(person);
        }

        workWithAvatar(avatar, person);
    }

    @Transactional(readOnly = true)
    public Avatar getRepresentationOfAvatarByPersonId(long id) {
        return avatarRepository.findByPersonId(id).orElseThrow(() -> new RuntimeException("У пользователя с таким идентификатором нет аватарки"));
    }

    @Transactional(readOnly = true)
    public MediaType getMediaTypeByPersonId(long id) {
        isPersonExistById(id);

        Avatar avatar = getRepresentationOfAvatarByPersonId(id);

        String contentType = avatar.getContentType().toString();

        if (contentType.equals("jpg")) {
            return MediaType.parseMediaType("image/jpeg");
        }

        return MediaType.parseMediaType("image/" + contentType);
    }

    @Transactional
    public File getImageByPersonId(long id) {
        isPersonExistById(id);

        Avatar avatar = getRepresentationOfAvatarByPersonId(id);

        String directoryPath = "avatars";

        File dir = new File(directoryPath);

        File file = new File(dir.getAbsolutePath() + File.separator + avatar.getOriginalFileName() + "." + avatar.getContentType());

        return file;
    }

    @Transactional(readOnly = true)
    public void isPersonExistById(long id) {
        if (!personRepository.existsById(id)) {
            throw new RuntimeException("Пользователь с таким идентификатором не найден");
        }
    }

    @Transactional
    public void deleteAvatarByPerson(Person person) {
        Avatar avatar = getRepresentationOfAvatarByPersonId(person.getId());

        String directoryPath = "avatars";

        File dir = new File(directoryPath);

        File fileToDelete = new File(dir.getAbsolutePath() + File.separator + avatar.getOriginalFileName() + "." + avatar.getContentType().toString());

        fileToDelete.delete();

        avatarRepository.deleteByPerson(person);
    }

    private void workWithAvatar(MultipartFile image, Person person) {
        try {
            String name = UUID.randomUUID().toString();
            String extension = getFileExtension(image);

            saveLocally(image, name, extension, "avatars");

            Avatar avatar = new Avatar(ContentType.valueOf(extension), name);

            avatar.setCreatedAt(new Date());

            person.addAvatarToPerson(avatar);

            person.setUpdatedAt(new Date());

            personRepository.save(person);
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
