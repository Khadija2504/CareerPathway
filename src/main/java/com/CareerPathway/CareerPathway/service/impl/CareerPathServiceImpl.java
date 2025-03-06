package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.CareerPath;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.repository.CareerPathRepository;
import com.CareerPathway.CareerPathway.service.CareerPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CareerPathServiceImpl implements CareerPathService {
    @Autowired
    private CareerPathRepository careerPathRepository;
    @Override
    public CareerPath createCareerPath(CareerPath careerPath) {
        return careerPathRepository.save(careerPath);
    }

    @Override
    public List<CareerPath> getCareerPathsByEmployee(User employee) {
        return careerPathRepository.findCareerPathByEmployee(employee);
    }

    @Override
    public List<CareerPath> getAllCareerPaths() {
        return careerPathRepository.findAll();
    }
}
