package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.dto.CareerPathDTO;
import com.CareerPathway.CareerPathway.mapper.CareerPathMapper;
import com.CareerPathway.CareerPathway.mapper.CareerPathStepMapper;
import com.CareerPathway.CareerPathway.model.CareerPath;
import com.CareerPathway.CareerPathway.model.CareerPathStep;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.repository.CareerPathRepository;
import com.CareerPathway.CareerPathway.service.CareerPathService;
import com.CareerPathway.CareerPathway.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/careerPath")
public class CareerPathController {
    @Autowired
    private CareerPathService careerPathService;

    @Autowired
    private CareerPathMapper careerPathMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private CareerPathRepository careerPathRepository;

    @PostMapping("/admin/create-careerPath")
    public ResponseEntity<?> createCareerPath(@Valid @RequestBody CareerPathDTO careerPathDTO, BindingResult result) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }

        CareerPath careerPath = careerPathMapper.toEntity(careerPathDTO);
        User employee = userService.findById(careerPathDTO.getEmployeeId()).get();
        careerPath.setEmployee(employee);
        CareerPath savedCareer = careerPathService.createCareerPath(careerPath);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCareer);
    }

    @GetMapping("/admin/getAllCareerPaths")
    public ResponseEntity<?> getAllCareerPaths() {
        List<CareerPath> careerPaths = careerPathService.getAllCareerPaths();
        return ResponseEntity.status(HttpStatus.OK).body(careerPaths);
    }

    @GetMapping("/employee/getAllCareerPaths")
    public ResponseEntity<?> getAllCareerPathsByEmployee(HttpServletRequest request) {
        long employeeId = Integer.parseInt(request.getAttribute("userId").toString());
        User employee = userService.findById(employeeId).get();
        List<CareerPath> careerPaths = careerPathService.getCareerPathsByEmployee(employee);
        return ResponseEntity.status(HttpStatus.OK).body(careerPaths);
    }

    @PutMapping("/admin/updateCareerPath/{careerPathId}")
    public ResponseEntity<?> updateCareerPath(@Valid @RequestBody CareerPathDTO careerPathDTO, BindingResult result, @PathVariable long careerPathId) {
        if (result.hasErrors()) {
            List<String> errors = result.getFieldErrors().stream()
                    .map(error -> error.getField() + ": " + error.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        CareerPath careerPath = careerPathMapper.toEntity(careerPathDTO);
        User employee = userService.findById(careerPathDTO.getEmployeeId()).orElseThrow(() -> new RuntimeException("Employee not found"));
        careerPath.setEmployee(employee);

        CareerPath updatedCareerPath = careerPathService.updateCareerPath(careerPath, careerPathId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCareerPath);
    }

    @GetMapping("/admin/getCareerPath/{careerId}")
    public ResponseEntity<?> getCareerPath(@PathVariable long careerId) {
        CareerPath careerPath = careerPathService.getCareerPathById(careerId);
        return ResponseEntity.status(HttpStatus.OK).body(careerPath);
    }

    @GetMapping("/admin/deleteCareerPath/{careerId}")
    public ResponseEntity<?> deleteCareerPath(@PathVariable long careerId) {
        boolean deletedCareerPath = careerPathService.deleteCareerPathById(careerId);
        return ResponseEntity.status(HttpStatus.OK).body(deletedCareerPath);
    }

    @PostMapping("/employee/updateStepStatus/{stepId}")
    public ResponseEntity<?> updateStepStatus(@PathVariable long stepId, @RequestBody boolean done) {
        CareerPathStep updatedCareerPathStep = careerPathService.updateCareerPathStep(done, stepId);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCareerPathStep);
    }
}
