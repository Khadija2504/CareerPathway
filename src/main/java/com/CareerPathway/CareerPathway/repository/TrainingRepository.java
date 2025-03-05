package com.CareerPathway.CareerPathway.repository;

import com.CareerPathway.CareerPathway.model.Training;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<Training, Long> {
    @Query("SELECT DISTINCT t FROM Training t JOIN t.skillsCovered s WHERE s IN :skills")
    List<Training> findByUserId(long userId, Pageable pageable);
    Page<Training> findByUser_Id(Long userId, Pageable pageable);
    List<Training> findTop1ByUserIdOrderByCreatedDateDesc(Long userId);
}
