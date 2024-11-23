package com.github.likhanov.meddata;

import com.github.likhanov.meddata.controller.HealthConditionController;
import com.github.likhanov.meddata.domain.HealthCondition;
import com.github.likhanov.meddata.domain.Source;
import com.github.likhanov.meddata.repository.HealthConditionRepository;
import com.github.likhanov.meddata.service.HealthConditionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class HealthConditionControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private HealthConditionRepository healthConditionRepository;

    @Autowired
    private HealthConditionController healthConditionController;

    @Autowired
    private HealthConditionService healthConditionService;

    private HealthCondition healthCondition;

    @BeforeEach
    void setUp() {
        healthConditionRepository.deleteAll();
        healthCondition = new HealthCondition();
        healthCondition.setSource(Source.ZIB);
        healthCondition.setListCode("ZIB001");
        healthCondition.setCode("271636001");
        healthCondition.setDisplayValue("Polsslag regelmatig");
        healthCondition.setLongDescription("The long description is necessary");
        healthCondition.setPriority(1);

        healthConditionRepository.save(healthCondition);
    }

    @Test
    void testGetHealthConditionByCode() {
        ResponseEntity<HealthCondition> response = restTemplate.getForEntity(
                "/health-condition/code/{code}", HealthCondition.class, "271636001");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getCode()).isEqualTo("271636001");
    }

    @Test
    void testGetHealthConditionByCodeNotFound() {
        ResponseEntity<HealthCondition> response = restTemplate.getForEntity(
                "/health-condition/code/{code}", HealthCondition.class, "000000001");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
