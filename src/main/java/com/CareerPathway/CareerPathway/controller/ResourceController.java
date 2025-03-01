package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.dto.ResourceDTO;
import com.CareerPathway.CareerPathway.mapper.ResourceMapper;
import com.CareerPathway.CareerPathway.model.Resource;
import com.CareerPathway.CareerPathway.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private ResourceMapper resourceMapper;

    @GetMapping("/getAllResources")
    public ResponseEntity<?> getAllResources() {
        List<Resource> resources = resourceService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(resources);
    }

    @PostMapping("/addResource")
    public ResponseEntity<?> addResource(@RequestBody ResourceDTO resourceDTO) {
        Resource resource = resourceMapper.toEntity(resourceDTO);
        Resource savedResource = resourceService.addResource(resource);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedResource);
    }
}
