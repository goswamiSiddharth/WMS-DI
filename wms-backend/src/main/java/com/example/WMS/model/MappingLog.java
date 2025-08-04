package com.example.WMS.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "MAPPING_LOG")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MappingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sku;
    private String product;
    private String message;

    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}