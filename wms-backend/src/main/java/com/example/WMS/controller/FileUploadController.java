package com.example.WMS.controller;

import com.example.WMS.service.FileProcessorService;
import com.example.WMS.dto.UploadSummaryDto;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin("http://localhost:3000")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileProcessorService processor;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // @PostMapping
    public ResponseEntity<UploadSummaryDto> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            UploadSummaryDto summary = processor.processFile(file);
            return ResponseEntity.ok(summary);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                  .body(new UploadSummaryDto(
                      file.getOriginalFilename(),
                      0, 0,
                      ex.getMessage()
                  ));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(500)
                  .body(new UploadSummaryDto(
                      file.getOriginalFilename(),
                      0, 0,
                      "Server error: " + ex.getMessage()
                  ));
        }
    }
}
