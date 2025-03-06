package com.CareerPathway.CareerPathway.service;

import com.CareerPathway.CareerPathway.model.CareerPath;
import com.CareerPathway.CareerPathway.model.User;

import java.util.List;

public interface CareerPathService {
    CareerPath createCareerPath(CareerPath careerPath);
    List<CareerPath> getCareerPathsByEmployee(User employee);
    List<CareerPath> getAllCareerPaths();
}
