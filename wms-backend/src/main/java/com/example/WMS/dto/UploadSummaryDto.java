package com.example.WMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UploadSummaryDto {
    private String filename;
    private int mappedCount;
    private int newCount;
    private String status;
}
