package com.CareerPathway.CareerPathway.service;

import com.CareerPathway.CareerPathway.model.CareerPath;
import com.CareerPathway.CareerPathway.model.Certification;
import com.CareerPathway.CareerPathway.model.User;

public interface CertificationService {
    Certification generateCertification(CareerPath careerPath, User employee);
    Certification getCertification(CareerPath careerPath);
}
