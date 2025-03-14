package com.CareerPathway.CareerPathway.service;

import com.CareerPathway.CareerPathway.model.Resource;

import java.util.List;

public interface ResourceService {
    List<Resource> findAll();
    Resource addResource(Resource resource);
    boolean deleteResource(long resourceId);
}
