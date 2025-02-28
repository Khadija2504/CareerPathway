package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.model.Resource;
import com.CareerPathway.CareerPathway.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @GetMapping("/getAllResources")
    public ResponseEntity<?> getAllResources() {
        List<Resource> resources = resourceService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(resources);
    }
}
