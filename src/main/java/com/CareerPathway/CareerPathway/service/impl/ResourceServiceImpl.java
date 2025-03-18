package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.Resource;
import com.CareerPathway.CareerPathway.repository.ResourceRepository;
import com.CareerPathway.CareerPathway.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public List<Resource> findAll() {
        List<Resource> resources = resourceRepository.findAll();
        if (resources.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "No resources found.");
        }
        return resources;
    }

    @Override
    public Resource addResource(Resource resource) {
        try {
            return resourceRepository.save(resource);
        } catch (DataIntegrityViolationException e) {
            throw new RuntimeException("Error saving resource: Data integrity violation.", e);
        } catch (Exception e) {
            throw new RuntimeException("Error saving resource.", e);
        }
    }

    @Override
    public boolean deleteResource(long resourceId) {
        if (!resourceRepository.existsById(resourceId)) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Resource with ID " + resourceId + " not found.");
        }
        try {
            resourceRepository.deleteById(resourceId);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting resource.", e);
        }
    }
}
