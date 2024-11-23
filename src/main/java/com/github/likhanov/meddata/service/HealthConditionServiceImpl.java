package com.github.likhanov.meddata.service;

import com.github.likhanov.meddata.domain.HealthCondition;
import com.github.likhanov.meddata.domain.Source;
import com.github.likhanov.meddata.repository.HealthConditionRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class HealthConditionServiceImpl implements HealthConditionService {

    private static final int BATCH_SIZE = 1000;

    private final HealthConditionRepository healthConditionRepository;

    public HealthConditionServiceImpl(HealthConditionRepository healthConditionRepository) {
        this.healthConditionRepository = healthConditionRepository;
    }

    @Override
    @Transactional
    public void loadCsvData(MultipartFile file) throws IOException, CsvValidationException {
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            // Skip the header
            String[] nextLine = csvReader.readNext();

            List<HealthCondition> healthConditions = new ArrayList<>();
            while ((nextLine = csvReader.readNext()) != null) {
                HealthCondition healthCondition = new HealthCondition();
                healthCondition.setSource(Source.valueOf(nextLine[0]));
                healthCondition.setListCode(nextLine[1]);
                healthCondition.setCode(nextLine[2]);
                healthCondition.setDisplayValue(nextLine[3]);
                healthCondition.setLongDescription(nextLine[4]);
                healthCondition.setFromDate(parseDate(nextLine[5]));
                healthCondition.setToDate(parseDate(nextLine[6]));
                healthCondition.setPriority(parsePriority(nextLine[7]));

                healthConditions.add(healthCondition);

                if (healthConditions.size() >= BATCH_SIZE) {
                    healthConditionRepository.saveAll(healthConditions);
                    healthConditions.clear();
                }
            }

            if (!healthConditions.isEmpty()) {
                healthConditionRepository.saveAll(healthConditions);
            }
        }
    }

    @Override
    public Page<HealthCondition> getAllHealthConditions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return healthConditionRepository.findAll(pageable);
    }

    @Override
    public Optional<HealthCondition> getHealthConditionByCode(String code) {
        return healthConditionRepository.findByCode(code);
    }

    @Override
    public void deleteAllHealthConditions() {
        healthConditionRepository.deleteAll();
    }

    private Instant parseDate(String date) {
        Instant result;
        if (date.isEmpty()) {
            result = Instant.EPOCH;
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate localDate = LocalDate.parse(date, formatter);
            result = localDate.atStartOfDay(ZoneOffset.UTC).toInstant();
        }
        return result;
    }

    private Integer parsePriority(String priority) {
        Integer result;
        if (priority.isEmpty()) {
            result = 0;
        } else {
            result = Integer.parseInt(priority);
        }
        return result;
    }
}
