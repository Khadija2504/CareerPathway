package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.CareerPath;
import com.CareerPathway.CareerPathway.model.CareerPathStep;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.repository.CareerPathRepository;
import com.CareerPathway.CareerPathway.repository.CareerPathStepRepository;
import com.CareerPathway.CareerPathway.service.CareerPathService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CareerPathServiceImpl implements CareerPathService {
    @Autowired
    private CareerPathRepository careerPathRepository;
    @Autowired
    private CareerPathStepRepository careerPathStepRepository;

    @Override
    public CareerPath createCareerPath(CareerPath careerPath) {
        try {
            return careerPathRepository.save(careerPath);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating career path", e);
        }
    }

    @Override
    public List<CareerPath> getCareerPathsByEmployee(User employee) {
        try {
            return careerPathRepository.findCareerPathByEmployee(employee);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving career paths by employee", e);
        }
    }

    @Override
    public List<CareerPath> getAllCareerPaths() {
        try {
            return careerPathRepository.findAll();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving all career paths", e);
        }
    }

    @Override
    public CareerPath updateCareerPath(CareerPath careerPath, long careerPathId) {
        try {
            CareerPath oldCareerPath = getCareerPathById(careerPathId);
            if (oldCareerPath == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Career path not found");
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
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating career path", e);
        }
    }

    @Override
    public CareerPath getCareerPathById(long id) {
        try {
            Optional<CareerPath> careerPath = careerPathRepository.findById(id);
            return careerPath.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Career path not found"));
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving career path by ID", e);
        }
    }

    @Override
    public boolean deleteCareerPathById(long id) {
        try {
            Optional<CareerPath> careerPath = careerPathRepository.findById(id);
            if (careerPath.isPresent()) {
                careerPathRepository.delete(careerPath.get());
                return true;
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Career path not found");
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting career path", e);
        }
    }

    @Override
    public CareerPathStep updateCareerPathStep(boolean done, long stepId) {
        try {
            CareerPathStep careerPathStep = careerPathStepRepository.findById(stepId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Career path step not found"));
            careerPathStep.setDone(done);
            return careerPathStepRepository.save(careerPathStep);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating career path step", e);
        }
    }

    @Override
    public CareerPath updateCareerPathStatus(long careerPathId) {
        try {
            CareerPath careerPath = getCareerPathById(careerPathId);
            careerPath.setDone(true);
            return careerPathRepository.save(careerPath);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating career path status", e);
        }
    }
}
