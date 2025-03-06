package com.CareerPathway.CareerPathway.repository;

import com.CareerPathway.CareerPathway.model.CareerPath;
import com.CareerPathway.CareerPathway.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CareerPathRepository extends JpaRepository<CareerPath, Long> {
    List<CareerPath> findCareerPathByEmployee(User employee);
}
