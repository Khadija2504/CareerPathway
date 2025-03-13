package com.CareerPathway.CareerPathway.service;

import com.CareerPathway.CareerPathway.dto.ProgressMetricsDTO;

public interface ProgressService {
    ProgressMetricsDTO calculateProgressMetrics(Long userId);
}
