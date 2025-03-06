package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.dto.CareerPathDTO;
import com.CareerPathway.CareerPathway.mapper.CareerPathMapper;
import com.CareerPathway.CareerPathway.mapper.CareerPathStepMapper;
import com.CareerPathway.CareerPathway.model.CareerPath;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.service.CareerPathService;
import com.CareerPathway.CareerPathway.service.UserService;
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
}
