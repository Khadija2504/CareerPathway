package com.CareerPathway.CareerPathway.model;

import com.CareerPathway.CareerPathway.model.enums.CourseType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    private CourseType type;
    private String category;
    private String url;
    private final LocalDate createdDate = LocalDate.now();
}
