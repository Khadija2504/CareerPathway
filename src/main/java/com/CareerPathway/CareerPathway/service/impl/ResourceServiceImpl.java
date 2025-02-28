package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.Resource;
import com.CareerPathway.CareerPathway.repository.ResourceRepository;
import com.CareerPathway.CareerPathway.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceServiceImpl implements ResourceService {

    @Autowired
    private ResourceRepository resourceRepository;

    @Override
    public List<Resource> findAll() {
        return resourceRepository.findAll();
    }
}
