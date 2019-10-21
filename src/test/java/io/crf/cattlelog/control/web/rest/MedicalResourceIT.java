package io.crf.cattlelog.control.web.rest;

import io.crf.cattlelog.control.ControlApp;
import io.crf.cattlelog.control.domain.Medical;
import io.crf.cattlelog.control.repository.MedicalRepository;
import io.crf.cattlelog.control.service.MedicalService;
import io.crf.cattlelog.control.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static io.crf.cattlelog.control.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link MedicalResource} REST controller.
 */
@SpringBootTest(classes = ControlApp.class)
public class MedicalResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private MedicalRepository medicalRepository;

    @Autowired
    private MedicalService medicalService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restMedicalMockMvc;

    private Medical medical;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final MedicalResource medicalResource = new MedicalResource(medicalService);
        this.restMedicalMockMvc = MockMvcBuilders.standaloneSetup(medicalResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medical createEntity(EntityManager em) {
        Medical medical = new Medical()
            .name(DEFAULT_NAME)
            .date(DEFAULT_DATE);
        return medical;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medical createUpdatedEntity(EntityManager em) {
        Medical medical = new Medical()
            .name(UPDATED_NAME)
            .date(UPDATED_DATE);
        return medical;
    }

    @BeforeEach
    public void initTest() {
        medical = createEntity(em);
    }

    @Test
    @Transactional
    public void createMedical() throws Exception {
        int databaseSizeBeforeCreate = medicalRepository.findAll().size();

        // Create the Medical
        restMedicalMockMvc.perform(post("/api/medicals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medical)))
            .andExpect(status().isCreated());

        // Validate the Medical in the database
        List<Medical> medicalList = medicalRepository.findAll();
        assertThat(medicalList).hasSize(databaseSizeBeforeCreate + 1);
        Medical testMedical = medicalList.get(medicalList.size() - 1);
        assertThat(testMedical.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMedical.getDate()).isEqualTo(DEFAULT_DATE);
    }

    @Test
    @Transactional
    public void createMedicalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = medicalRepository.findAll().size();

        // Create the Medical with an existing ID
        medical.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicalMockMvc.perform(post("/api/medicals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medical)))
            .andExpect(status().isBadRequest());

        // Validate the Medical in the database
        List<Medical> medicalList = medicalRepository.findAll();
        assertThat(medicalList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicalRepository.findAll().size();
        // set the field null
        medical.setName(null);

        // Create the Medical, which fails.

        restMedicalMockMvc.perform(post("/api/medicals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medical)))
            .andExpect(status().isBadRequest());

        List<Medical> medicalList = medicalRepository.findAll();
        assertThat(medicalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMedicals() throws Exception {
        // Initialize the database
        medicalRepository.saveAndFlush(medical);

        // Get all the medicalList
        restMedicalMockMvc.perform(get("/api/medicals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medical.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getMedical() throws Exception {
        // Initialize the database
        medicalRepository.saveAndFlush(medical);

        // Get the medical
        restMedicalMockMvc.perform(get("/api/medicals/{id}", medical.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(medical.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMedical() throws Exception {
        // Get the medical
        restMedicalMockMvc.perform(get("/api/medicals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMedical() throws Exception {
        // Initialize the database
        medicalService.save(medical);

        int databaseSizeBeforeUpdate = medicalRepository.findAll().size();

        // Update the medical
        Medical updatedMedical = medicalRepository.findById(medical.getId()).get();
        // Disconnect from session so that the updates on updatedMedical are not directly saved in db
        em.detach(updatedMedical);
        updatedMedical
            .name(UPDATED_NAME)
            .date(UPDATED_DATE);

        restMedicalMockMvc.perform(put("/api/medicals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMedical)))
            .andExpect(status().isOk());

        // Validate the Medical in the database
        List<Medical> medicalList = medicalRepository.findAll();
        assertThat(medicalList).hasSize(databaseSizeBeforeUpdate);
        Medical testMedical = medicalList.get(medicalList.size() - 1);
        assertThat(testMedical.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMedical.getDate()).isEqualTo(UPDATED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingMedical() throws Exception {
        int databaseSizeBeforeUpdate = medicalRepository.findAll().size();

        // Create the Medical

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicalMockMvc.perform(put("/api/medicals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(medical)))
            .andExpect(status().isBadRequest());

        // Validate the Medical in the database
        List<Medical> medicalList = medicalRepository.findAll();
        assertThat(medicalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMedical() throws Exception {
        // Initialize the database
        medicalService.save(medical);

        int databaseSizeBeforeDelete = medicalRepository.findAll().size();

        // Delete the medical
        restMedicalMockMvc.perform(delete("/api/medicals/{id}", medical.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Medical> medicalList = medicalRepository.findAll();
        assertThat(medicalList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medical.class);
        Medical medical1 = new Medical();
        medical1.setId(1L);
        Medical medical2 = new Medical();
        medical2.setId(medical1.getId());
        assertThat(medical1).isEqualTo(medical2);
        medical2.setId(2L);
        assertThat(medical1).isNotEqualTo(medical2);
        medical1.setId(null);
        assertThat(medical1).isNotEqualTo(medical2);
    }
}
