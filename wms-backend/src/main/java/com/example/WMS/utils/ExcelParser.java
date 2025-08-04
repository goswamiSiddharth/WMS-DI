package com.example.WMS.utils;

import com.example.WMS.dto.UploadSummaryDto;
import com.example.WMS.model.MSKU;
import com.example.WMS.model.SKU;
import com.example.WMS.model.MappingLog;
import com.example.WMS.repository.MSKURepository;
import com.example.WMS.repository.SKURepository;
import com.example.WMS.repository.MappingLogRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class ExcelParser {

    private static final Logger logger = Logger.getLogger(ExcelParser.class.getName());

    private final SKURepository skuRepo;
    private final MSKURepository mskuRepo;
    private final MappingLogRepository logRepo;

    public ExcelParser(SKURepository skuRepo, MSKURepository mskuRepo, MappingLogRepository logRepo) {
        this.skuRepo = skuRepo;
        this.mskuRepo = mskuRepo;
        this.logRepo = logRepo;
    }

    public UploadSummaryDto  parseAndMap(MultipartFile file) throws Exception {
        int mappedCount = 0;
        int newCount = 0;
        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header

                try {
                    String skuCode = getCellValue(row.getCell(0));
                    String productName = getCellValue(row.getCell(1));

                    Optional<SKU> skuOpt = skuRepo.findByCode(skuCode);
                    if (skuOpt.isPresent()) {
                         mappedCount++;
                        SKU sku = skuOpt.get();
                        logger.info("Found in DB: SKU=" + skuCode + ", Mapped MSKU=" + sku.getMsku().getName());
                    } else {
                        // Check or create MSKU
                        MSKU msku = mskuRepo.findByName(productName)
                                .orElseGet(() -> mskuRepo.save(MSKU.builder().name(productName).build()));

                        // Create and save SKU
                        SKU newSku = SKU.builder().code(skuCode).msku(msku).build();
                        skuRepo.save(newSku);
                        newCount++;
                        logger.info("Mapped and saved new SKU=" + skuCode + ", MSKU=" + msku.getName());
                    }

                } catch (Exception ex) {
                    logger.warning("Skipping row " + row.getRowNum() + ": " + ex.getMessage());

                    MappingLog log = MappingLog.builder()
                            .sku(getCellValue(row.getCell(0)))
                            .product(getCellValue(row.getCell(1)))
                            .message("Error during mapping: " + ex.getMessage())
                            .build();

                    logRepo.save(log);
                }
            }

        } catch (IOException e) {
            throw new Exception("Failed to parse Excel file.", e);
        }
        return new UploadSummaryDto(file.getOriginalFilename(), mappedCount, newCount, "Excel processed");
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            default -> "";
        };
    }
    
}
