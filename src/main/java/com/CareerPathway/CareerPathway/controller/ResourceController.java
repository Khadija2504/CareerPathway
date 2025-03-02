package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.dto.ResourceDTO;
import com.CareerPathway.CareerPathway.mapper.ResourceMapper;
import com.CareerPathway.CareerPathway.model.Resource;
import com.CareerPathway.CareerPathway.model.enums.ResourceType;
import com.CareerPathway.CareerPathway.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.nio.file.*;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ResourceMapper resourceMapper;
    private final String UPLOAD_DIR = "uploads/";

    @GetMapping("/user/getAllResources")
    public ResponseEntity<?> getAllResources() {
        List<Resource> resources = resourceService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(resources);
    }

    @PostMapping("/admin/addResource")
    public ResponseEntity<?> addResource(
            @RequestParam("title") String title,
            @RequestParam("type") String type,
            @RequestParam("category") String category,
            @RequestParam("resourceUrl") String resourceUrl,
            @RequestParam("image") MultipartFile image) {

        try {
            String imageFileName = StringUtils.cleanPath(image.getOriginalFilename());
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path imageFilePath = uploadPath.resolve(imageFileName);
            Files.copy(image.getInputStream(), imageFilePath, StandardCopyOption.REPLACE_EXISTING);

            Resource resource = new Resource();
            resource.setTitle(title);
            resource.setType(ResourceType.valueOf(type));
            resource.setCategory(category);
            resource.setResourceUrl(resourceUrl);
            resource.setImage("http://localhost:8080/uploads/" + imageFileName);

            Resource savedResource = resourceService.addResource(resource);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedResource);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image");
        }
    }
}
