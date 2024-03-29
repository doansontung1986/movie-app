package vn.techmaster.movie.service;

import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.techmaster.movie.entity.Image;
import vn.techmaster.movie.entity.User;
import vn.techmaster.movie.exception.ResourceNotFoundException;
import vn.techmaster.movie.repository.ImageRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ImageService {
    private String uploadDir = "image_uploads";
    private final HttpSession session;
    private final ImageRepository imageRepository;

    public ImageService(HttpSession session, ImageRepository imageRepository) {
        this.session = session;
        this.imageRepository = imageRepository;
        createDirectory(uploadDir);
    }

    private void createDirectory(String name) {
        Path path = Paths.get(name);
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                log.error("Cannot create directory: " + path);
                log.error(e.getMessage());
                throw new RuntimeException("Cannot create directory: " + path);
            }
        }
    }

    public List<Image> getAllImagesByCurrentUser() {
        User user = (User) session.getAttribute("currentUser");
        return imageRepository.findByUser_IdOrderByCreatedAt(user.getId());
    }

    public Page<Image> getAllImagesByCurrentUser(Integer page, Integer limit) {
        User user = (User) session.getAttribute("currentUser");

        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
        return imageRepository.findByUser_IdOrderByCreatedAtDesc(user.getId(), pageable);
    }

    public Image uploadImage(MultipartFile file) {
        User user = (User) session.getAttribute("currentUser");

        // Upload file vào folder
        String imageId = UUID.randomUUID().toString();
        Path rootPath = Paths.get(uploadDir);
        Path filePath = rootPath.resolve(imageId);

        try {
            Files.copy(file.getInputStream(), filePath);
        } catch (IOException e) {
            log.error("Cannot upload file: " + filePath);
            log.error(e.getMessage());
            throw new RuntimeException("Cannot upload file: " + filePath);
        }

        // Lưu thông tin ảnh vào database
        Image image = Image.builder()
                .id(imageId)
                .name(file.getOriginalFilename())
                .type(file.getContentType())
                .size(file.getSize() / 1048576.0) // convert to MB
                .url("/" + uploadDir + "/" + imageId)
                .user(user)
                .build();

        imageRepository.save(image);

        return image;
    }

    public void deleteImage(String id) {
        Image image = imageRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Image not found"));

        // Xóa ảnh trong folder
        Path filePath = Paths.get(uploadDir).resolve(image.getId());

        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            log.error("Cannot delete file: " + filePath);
            log.error(e.getMessage());
            throw new RuntimeException("Cannot delete file: " + filePath);
        }

        // Xóa ảnh trong database
        imageRepository.deleteById(id);
    }
}
