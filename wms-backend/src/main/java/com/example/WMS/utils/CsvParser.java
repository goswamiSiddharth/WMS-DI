package com.example.WMS.utils;


import com.example.WMS.model.MappingLog;
import com.example.WMS.dto.UploadSummaryDto;
import com.example.WMS.model.MSKU;
import com.example.WMS.model.SKU;
import com.example.WMS.repository.MappingLogRepository;
import com.example.WMS.repository.MSKURepository;
import com.example.WMS.repository.SKURepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.logging.Logger;

@Component
public class CsvParser {

    private static final Logger logger = Logger.getLogger(CsvParser.class.getName());

    private final SKURepository skuRepo;
    private final MSKURepository mskuRepo;
    private final MappingLogRepository logRepo;

    public CsvParser(SKURepository skuRepo, MSKURepository mskuRepo, MappingLogRepository logRepo) {
        this.skuRepo = skuRepo;
        this.mskuRepo = mskuRepo;
        this.logRepo = logRepo;
    }

    public UploadSummaryDto  parseAndMap(MultipartFile file) throws Exception {
        int mappedCount = 0;
        int newCount = 0;
        // Build CSVFormat using non-deprecated builder API
        CSVFormat format = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(true)
                .setIgnoreHeaderCase(true)
                .setTrim(true)
                .build();

        try (
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
            CSVParser parser = CSVParser.parse(reader, format)
        ) {
            for (CSVRecord record : parser) {
                try {
                    // String skuCode = record.get("SKU");
                    // String productName = record.get("Product");

                    String skuCode = record.isMapped("SKU") ? record.get("SKU") : record.get("sku_code");
                    String productName = record.isMapped("Product") ? record.get("Product") : record.get("product_name");



                    Optional<SKU> existing = skuRepo.findByCode(skuCode);
                    if (existing.isPresent()) {
                        mappedCount++;
                        SKU sku = existing.get();
                        logger.info("Found in DB: SKU=" + skuCode + ", MSKU=" + sku.getMsku().getName());
                    } else {
                        MSKU msku = mskuRepo.findByName(productName)
                                .orElseGet(() -> mskuRepo.save(MSKU.builder().name(productName).build()));
                        SKU newSku = SKU.builder().code(skuCode).msku(msku).build();
                        skuRepo.save(newSku);
                        newCount++;
                        logger.info("Mapped CSV -> SKU=" + skuCode + ", MSKU=" + msku.getName());

                        MappingLog log = MappingLog.builder()
                                .sku(skuCode)
                                .product(productName)
                                .message("New mapping created")
                                .build();
                        logRepo.save(log);
                    }
                } catch (Exception ex) {
                    // MappingLog log = MappingLog.builder()
                    //         .sku(record.get("SKU"))
                    //         .product(record.get("Product"))
                    //         .message("CSV Mapping Error: " + ex.getMessage())
                    //         .build();
                    // logRepo.save(log);
                    // logger.warning("Error processing CSV row: " + ex.getMessage());

                    String fallbackSKU = record.isMapped("SKU") ? record.get("SKU") :
                         record.isMapped("sku_code") ? record.get("sku_code") : "N/A";

                    String fallbackProduct = record.isMapped("Product") ? record.get("Product") :
                                            record.isMapped("product_name") ? record.get("product_name") : "N/A";

                    MappingLog log = MappingLog.builder()
                            .sku(fallbackSKU)
                            .product(fallbackProduct)
                            .message("CSV Mapping Error: " + ex.getMessage())
                            .build();

                    logRepo.save(log);
                    logger.warning("Error processing CSV row: " + ex.getMessage());
                }
            }
        } catch (Exception e) {
            throw new Exception("Failed to parse CSV file.", e);
        }
            return new UploadSummaryDto(file.getOriginalFilename(), mappedCount, newCount, "CSV processed");

    }
}
