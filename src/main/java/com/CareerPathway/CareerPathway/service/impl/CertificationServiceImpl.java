package com.CareerPathway.CareerPathway.service.impl;

import com.CareerPathway.CareerPathway.model.CareerPath;
import com.CareerPathway.CareerPathway.model.Certification;
import com.CareerPathway.CareerPathway.model.User;
import com.CareerPathway.CareerPathway.repository.CertificationRepository;
import com.CareerPathway.CareerPathway.service.CertificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CertificationServiceImpl implements CertificationService {
    @Autowired
    private CertificationRepository certificationRepository;
    @Override
    public Certification generateCertification(CareerPath careerPath, User employee) {
        Certification certification = Certification.builder()
                .user(employee)
                .careerPath(careerPath)
                .certificateUrl("/certificates/career.pdf")
                .build();
        return certificationRepository.save(certification);
    }

    @Override
    public Certification getCertification(CareerPath careerPath) {
        return certificationRepository.findCertificationByCareerPath(careerPath);
    }
}
