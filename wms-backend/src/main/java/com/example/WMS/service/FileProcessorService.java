package com.example.WMS.service;

import com.example.WMS.utils.CsvParser;
import com.example.WMS.utils.ExcelParser;
import com.example.WMS.dto.UploadSummaryDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FileProcessorService {

    private final ExcelParser excelParser;
    private final CsvParser csvParser;

    public UploadSummaryDto processFile(MultipartFile file) throws Exception {
        String fn = file.getOriginalFilename();
        if (fn == null) {
            throw new IllegalArgumentException("Filename must not be null");
        }
        String lower = fn.toLowerCase();

        int mappedCount = 0;
        int newCount = 0;

        if (lower.endsWith(".xlsx") || lower.endsWith(".xls")) {
            var result = excelParser.parseAndMap(file);
            mappedCount = result.getMappedCount();
            newCount = result.getNewCount();
        } else if (lower.endsWith(".csv")) {
            var result = csvParser.parseAndMap(file);
            mappedCount = result.getMappedCount();
            newCount = result.getNewCount();
        } else {
            throw new IllegalArgumentException("Unsupported file type: " + fn);
        }

        return new UploadSummaryDto(fn, mappedCount, newCount, "Processed OK");
    }
}
