package com.github.likhanov.meddata.service;

import com.github.likhanov.meddata.domain.HealthCondition;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface HealthConditionService {

    void loadCsvData(MultipartFile file) throws IOException, CsvValidationException;

    Page<HealthCondition> getAllHealthConditions(int page, int size);

    Optional<HealthCondition> getHealthConditionByCode(String code);

    void deleteAllHealthConditions();
}
