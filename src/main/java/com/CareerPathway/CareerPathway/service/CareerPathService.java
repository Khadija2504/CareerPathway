package com.CareerPathway.CareerPathway.service;

import com.CareerPathway.CareerPathway.model.CareerPath;
import com.CareerPathway.CareerPathway.model.CareerPathStep;
import com.CareerPathway.CareerPathway.model.User;

import java.util.List;

public interface CareerPathService {
    CareerPath createCareerPath(CareerPath careerPath);
    List<CareerPath> getCareerPathsByEmployee(User employee);
    List<CareerPath> getAllCareerPaths();
    CareerPath updateCareerPath(CareerPath careerPath, long careerPathId);
    CareerPath getCareerPathById(long id);
    boolean deleteCareerPathById(long id);
    CareerPathStep updateCareerPathStep(boolean done, long stepId);
}
