package com.CareerPathway.CareerPathway.repository;

import com.CareerPathway.CareerPathway.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByCategoryIn(List<String> categories);
}
