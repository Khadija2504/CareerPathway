package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.CareerPath;
import com.CareerPathway.CareerPathway.model.CareerPathStep;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.repository.CareerPathRepository;
import com.CareerPathway.CareerPathway.service.CareerPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Optional;

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

    @Override
    public CareerPath updateCareerPath(CareerPath careerPath, long careerPathId) {
        CareerPath oldCareerPath = getCareerPathById(careerPathId);
        if (oldCareerPath == null) {
            throw new RuntimeException("Career path not found");
        }

        oldCareerPath.setName(careerPath.getName());
        oldCareerPath.setDescription(careerPath.getDescription());
        oldCareerPath.setEmployee(careerPath.getEmployee());

        oldCareerPath.getSteps().clear();
        for (CareerPathStep step : careerPath.getSteps()) {
            step.setCareerPath(oldCareerPath);
            oldCareerPath.getSteps().add(step);
        }

        return careerPathRepository.save(oldCareerPath);
    }

    @Override
    public CareerPath getCareerPathById(long id) {
        Optional<CareerPath> careerPath = careerPathRepository.findById(id);
        return careerPath.orElse(null);
    }
}
