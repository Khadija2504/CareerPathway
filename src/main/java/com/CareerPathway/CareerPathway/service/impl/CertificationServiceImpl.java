package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.CareerPath;
import com.CareerPathway.CareerPathway.model.Certification;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.repository.CertificationRepository;
import com.CareerPathway.CareerPathway.service.CertificationService;
import com.CareerPathway.CareerPathway.util.PdfCertificateGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CertificationServiceImpl implements CertificationService {
    @Autowired
    private CertificationRepository certificationRepository;

    @Override
    public Certification generateCertification(CareerPath careerPath, User employee) {
        if (careerPath == null || employee == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Career path or employee cannot be null");
        }

        String certificateFileName = "certificate_" + careerPath.getName().replace(" ", "_") + "_" + careerPath.getId() + "_" + employee.getId() + ".pdf";
        String certificatePath = "uploads/" + certificateFileName;

        try {
            PdfCertificateGenerator.generateCertificate(
                    employee.getFirstName(),
                    employee.getLastName(),
                    careerPath.getName(),
                    certificatePath
            );

            System.out.println("Certification generated successfully: " + certificateFileName);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate certificate", e);
        }

        Certification certification = Certification.builder()
                .user(employee)
                .careerPath(careerPath)
                .certificateUrl("http://localhost:8800/" + certificatePath)
                .build();

        try {
            return certificationRepository.save(certification);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save certification", e);
        }
    }

    @Override
    public Certification getCertification(CareerPath careerPath) {
        if (careerPath == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Career path cannot be null");
        }

        try {
            Certification certification = certificationRepository.findCertificationByCareerPath(careerPath);
            if (certification == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Certification not found for the given career path");
            }
            return certification;
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error retrieving certification", e);
        }
    }
}