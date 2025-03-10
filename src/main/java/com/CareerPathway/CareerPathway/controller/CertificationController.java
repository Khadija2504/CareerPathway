package com.CareerPathway.CareerPathway.controller;

import com.CareerPathway.CareerPathway.model.CareerPath;
import com.CareerPathway.CareerPathway.model.Certification;
import com.CareerPathway.CareerPathway.service.CareerPathService;
import com.CareerPathway.CareerPathway.service.CertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/certifications")
public class CertificationController {
    @Autowired
    private CertificationService certificationService;
    @Autowired
    private CareerPathService careerPathService;
    @GetMapping("/{careerPathId}")
    public ResponseEntity<?> getCertificationCareerPath(@PathVariable long careerPathId) {
        CareerPath careerPath =  careerPathService.getCareerPathById(careerPathId);
        Certification certification = certificationService.getCertification(careerPath);
        return ResponseEntity.status(HttpStatus.OK).body(certification);
    }
}
