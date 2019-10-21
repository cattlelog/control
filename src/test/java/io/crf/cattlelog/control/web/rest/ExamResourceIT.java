package io.crf.cattlelog.control.web.rest;

import io.crf.cattlelog.control.ControlApp;
import io.crf.cattlelog.control.domain.Exam;
import io.crf.cattlelog.control.domain.Medical;
import io.crf.cattlelog.control.domain.Animal;
import io.crf.cattlelog.control.repository.ExamRepository;
import io.crf.cattlelog.control.service.ExamService;
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
import java.util.List;

import static io.crf.cattlelog.control.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link ExamResource} REST controller.
 */
@SpringBootTest(classes = ControlApp.class)
public class ExamResourceIT {

    private static final String DEFAULT_RESULT = "AAAAAAAAAA";
    private static final String UPDATED_RESULT = "BBBBBBBBBB";

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private ExamService examService;

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

    private MockMvc restExamMockMvc;

    private Exam exam;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ExamResource examResource = new ExamResource(examService);
        this.restExamMockMvc = MockMvcBuilders.standaloneSetup(examResource)
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
    public static Exam createEntity(EntityManager em) {
        Exam exam = new Exam()
            .result(DEFAULT_RESULT);
        // Add required entity
        Medical medical;
        if (TestUtil.findAll(em, Medical.class).isEmpty()) {
            medical = MedicalResourceIT.createEntity(em);
            em.persist(medical);
            em.flush();
        } else {
            medical = TestUtil.findAll(em, Medical.class).get(0);
        }
        exam.setMedical(medical);
        // Add required entity
        Animal animal;
        if (TestUtil.findAll(em, Animal.class).isEmpty()) {
            animal = AnimalResourceIT.createEntity(em);
            em.persist(animal);
            em.flush();
        } else {
            animal = TestUtil.findAll(em, Animal.class).get(0);
        }
        exam.setAnimal(animal);
        return exam;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Exam createUpdatedEntity(EntityManager em) {
        Exam exam = new Exam()
            .result(UPDATED_RESULT);
        // Add required entity
        Medical medical;
        if (TestUtil.findAll(em, Medical.class).isEmpty()) {
            medical = MedicalResourceIT.createUpdatedEntity(em);
            em.persist(medical);
            em.flush();
        } else {
            medical = TestUtil.findAll(em, Medical.class).get(0);
        }
        exam.setMedical(medical);
        // Add required entity
        Animal animal;
        if (TestUtil.findAll(em, Animal.class).isEmpty()) {
            animal = AnimalResourceIT.createUpdatedEntity(em);
            em.persist(animal);
            em.flush();
        } else {
            animal = TestUtil.findAll(em, Animal.class).get(0);
        }
        exam.setAnimal(animal);
        return exam;
    }

    @BeforeEach
    public void initTest() {
        exam = createEntity(em);
    }

    @Test
    @Transactional
    public void createExam() throws Exception {
        int databaseSizeBeforeCreate = examRepository.findAll().size();

        // Create the Exam
        restExamMockMvc.perform(post("/api/exams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exam)))
            .andExpect(status().isCreated());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeCreate + 1);
        Exam testExam = examList.get(examList.size() - 1);
        assertThat(testExam.getResult()).isEqualTo(DEFAULT_RESULT);
    }

    @Test
    @Transactional
    public void createExamWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = examRepository.findAll().size();

        // Create the Exam with an existing ID
        exam.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restExamMockMvc.perform(post("/api/exams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exam)))
            .andExpect(status().isBadRequest());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkResultIsRequired() throws Exception {
        int databaseSizeBeforeTest = examRepository.findAll().size();
        // set the field null
        exam.setResult(null);

        // Create the Exam, which fails.

        restExamMockMvc.perform(post("/api/exams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exam)))
            .andExpect(status().isBadRequest());

        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExams() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get all the examList
        restExamMockMvc.perform(get("/api/exams?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exam.getId().intValue())))
            .andExpect(jsonPath("$.[*].result").value(hasItem(DEFAULT_RESULT.toString())));
    }
    
    @Test
    @Transactional
    public void getExam() throws Exception {
        // Initialize the database
        examRepository.saveAndFlush(exam);

        // Get the exam
        restExamMockMvc.perform(get("/api/exams/{id}", exam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(exam.getId().intValue()))
            .andExpect(jsonPath("$.result").value(DEFAULT_RESULT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExam() throws Exception {
        // Get the exam
        restExamMockMvc.perform(get("/api/exams/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExam() throws Exception {
        // Initialize the database
        examService.save(exam);

        int databaseSizeBeforeUpdate = examRepository.findAll().size();

        // Update the exam
        Exam updatedExam = examRepository.findById(exam.getId()).get();
        // Disconnect from session so that the updates on updatedExam are not directly saved in db
        em.detach(updatedExam);
        updatedExam
            .result(UPDATED_RESULT);

        restExamMockMvc.perform(put("/api/exams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedExam)))
            .andExpect(status().isOk());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
        Exam testExam = examList.get(examList.size() - 1);
        assertThat(testExam.getResult()).isEqualTo(UPDATED_RESULT);
    }

    @Test
    @Transactional
    public void updateNonExistingExam() throws Exception {
        int databaseSizeBeforeUpdate = examRepository.findAll().size();

        // Create the Exam

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExamMockMvc.perform(put("/api/exams")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(exam)))
            .andExpect(status().isBadRequest());

        // Validate the Exam in the database
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteExam() throws Exception {
        // Initialize the database
        examService.save(exam);

        int databaseSizeBeforeDelete = examRepository.findAll().size();

        // Delete the exam
        restExamMockMvc.perform(delete("/api/exams/{id}", exam.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Exam> examList = examRepository.findAll();
        assertThat(examList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Exam.class);
        Exam exam1 = new Exam();
        exam1.setId(1L);
        Exam exam2 = new Exam();
        exam2.setId(exam1.getId());
        assertThat(exam1).isEqualTo(exam2);
        exam2.setId(2L);
        assertThat(exam1).isNotEqualTo(exam2);
        exam1.setId(null);
        assertThat(exam1).isNotEqualTo(exam2);
    }
}
