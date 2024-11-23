package com.github.likhanov.meddata.controller;

import com.github.likhanov.meddata.domain.HealthCondition;
import com.github.likhanov.meddata.service.HealthConditionService;
import com.opencsv.exceptions.CsvException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/health-condition")
public class HealthConditionController {

    private final HealthConditionService healthConditionService;

    public HealthConditionController(HealthConditionService healthConditionService) {
        this.healthConditionService = healthConditionService;
    }

    @PostMapping("/upload")
    public String uploadCsv(@RequestParam("file") MultipartFile file) {
        try {
            healthConditionService.loadCsvData(file);
            return "CSV data successfully loaded.";
        } catch (IOException e) {
            return "Failed to load CSV data: " + e.getMessage();
        } catch (CsvException e) {
            return "Failed to parse CSV data: " + e.getMessage();
        }
    }

    @GetMapping
    public Page<HealthCondition> getAllHealthConditions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        return healthConditionService.getAllHealthConditions(page, size);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<HealthCondition> getHealthConditionByCode(@PathVariable String code) {
        return healthConditionService.getHealthConditionByCode(code)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public void deleteAllHealthConditions() {
        healthConditionService.deleteAllHealthConditions();
    }
}
