package com.example.WMS.controller;

import com.example.WMS.repository.SKURepository;
import com.example.WMS.repository.MappingLogRepository;
// import com.example.WMS.model.MappingLog;
// import com.example.WMS.model.SKU;
import org.springframework.web.bind.annotation.*;
// import org.springframework.http.ResponseEntity;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000") // only if React runs on 3000
public class ReportsController {

    private final MappingLogRepository logRepo;
    private final SKURepository skuRepo;

    @GetMapping("/api/logs")
    public List<MappingLogDto> fetchLogs() {
        return logRepo.findAll().stream()
            .map(log -> new MappingLogDto(log.getId(), log.getSku(), log.getProduct(), log.getMessage(), log.getTimestamp()))
            .collect(Collectors.toList());
    }

    @GetMapping("/api/skus")
    public MskuSummaryDto fetchSkuSummary() {
        long total = skuRepo.count();
        long mapped = skuRepo.findAll().stream()
            .filter(s -> s.getMsku() != null).count();
        return new MskuSummaryDto(mapped, total - mapped);
    }

    // DTOs can be nested or in a separate 'dto' package
    public record MappingLogDto(Long id, String sku, String product, String message, java.time.LocalDateTime timestamp) {}
    public record MskuSummaryDto(long mappedCount, long newCount) {}
}
