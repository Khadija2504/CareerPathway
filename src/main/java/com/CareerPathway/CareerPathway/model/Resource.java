package com.CareerPathway.CareerPathway.model;

import com.CareerPathway.CareerPathway.model.enums.ResourceType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Resource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private ResourceType type;
    private String category;
    private String resourceUrl;
    private String image;
}
