package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.CareerPath;
import com.CareerPathway.CareerPathway.model.Certification;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.repository.CertificationRepository;
import com.CareerPathway.CareerPathway.service.CertificationService;
import com.CareerPathway.CareerPathway.util.PdfCertificateGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificationServiceImpl implements CertificationService {
    @Autowired
    private CertificationRepository certificationRepository;

    @Override
    public Certification generateCertification(CareerPath careerPath, User employee) {
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
            throw new RuntimeException("Failed to generate certificate", e);
        }

        Certification certification = Certification.builder()
                .user(employee)
                .careerPath(careerPath)
                .certificateUrl("http://localhost:8800/" + certificatePath)
                .build();
        return certificationRepository.save(certification);
    }

    @Override
    public Certification getCertification(CareerPath careerPath) {
        return certificationRepository.findCertificationByCareerPath(careerPath);
    }
}