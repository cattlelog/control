package io.crf.cattlelog.control.web.rest;

import io.crf.cattlelog.control.ControlApp;
import io.crf.cattlelog.control.domain.Herd;
import io.crf.cattlelog.control.repository.HerdRepository;
import io.crf.cattlelog.control.service.HerdService;
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
 * Integration tests for the {@Link HerdResource} REST controller.
 */
@SpringBootTest(classes = ControlApp.class)
public class HerdResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NUTRITION = "AAAAAAAAAA";
    private static final String UPDATED_NUTRITION = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    @Autowired
    private HerdRepository herdRepository;

    @Autowired
    private HerdService herdService;

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

    private MockMvc restHerdMockMvc;

    private Herd herd;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HerdResource herdResource = new HerdResource(herdService);
        this.restHerdMockMvc = MockMvcBuilders.standaloneSetup(herdResource)
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
    public static Herd createEntity(EntityManager em) {
        Herd herd = new Herd()
            .name(DEFAULT_NAME)
            .nutrition(DEFAULT_NUTRITION)
            .code(DEFAULT_CODE)
            .remark(DEFAULT_REMARK);
        return herd;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Herd createUpdatedEntity(EntityManager em) {
        Herd herd = new Herd()
            .name(UPDATED_NAME)
            .nutrition(UPDATED_NUTRITION)
            .code(UPDATED_CODE)
            .remark(UPDATED_REMARK);
        return herd;
    }

    @BeforeEach
    public void initTest() {
        herd = createEntity(em);
    }

    @Test
    @Transactional
    public void createHerd() throws Exception {
        int databaseSizeBeforeCreate = herdRepository.findAll().size();

        // Create the Herd
        restHerdMockMvc.perform(post("/api/herds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(herd)))
            .andExpect(status().isCreated());

        // Validate the Herd in the database
        List<Herd> herdList = herdRepository.findAll();
        assertThat(herdList).hasSize(databaseSizeBeforeCreate + 1);
        Herd testHerd = herdList.get(herdList.size() - 1);
        assertThat(testHerd.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHerd.getNutrition()).isEqualTo(DEFAULT_NUTRITION);
        assertThat(testHerd.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testHerd.getRemark()).isEqualTo(DEFAULT_REMARK);
    }

    @Test
    @Transactional
    public void createHerdWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = herdRepository.findAll().size();

        // Create the Herd with an existing ID
        herd.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHerdMockMvc.perform(post("/api/herds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(herd)))
            .andExpect(status().isBadRequest());

        // Validate the Herd in the database
        List<Herd> herdList = herdRepository.findAll();
        assertThat(herdList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = herdRepository.findAll().size();
        // set the field null
        herd.setName(null);

        // Create the Herd, which fails.

        restHerdMockMvc.perform(post("/api/herds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(herd)))
            .andExpect(status().isBadRequest());

        List<Herd> herdList = herdRepository.findAll();
        assertThat(herdList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHerds() throws Exception {
        // Initialize the database
        herdRepository.saveAndFlush(herd);

        // Get all the herdList
        restHerdMockMvc.perform(get("/api/herds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(herd.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].nutrition").value(hasItem(DEFAULT_NUTRITION.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())));
    }
    
    @Test
    @Transactional
    public void getHerd() throws Exception {
        // Initialize the database
        herdRepository.saveAndFlush(herd);

        // Get the herd
        restHerdMockMvc.perform(get("/api/herds/{id}", herd.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(herd.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.nutrition").value(DEFAULT_NUTRITION.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHerd() throws Exception {
        // Get the herd
        restHerdMockMvc.perform(get("/api/herds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHerd() throws Exception {
        // Initialize the database
        herdService.save(herd);

        int databaseSizeBeforeUpdate = herdRepository.findAll().size();

        // Update the herd
        Herd updatedHerd = herdRepository.findById(herd.getId()).get();
        // Disconnect from session so that the updates on updatedHerd are not directly saved in db
        em.detach(updatedHerd);
        updatedHerd
            .name(UPDATED_NAME)
            .nutrition(UPDATED_NUTRITION)
            .code(UPDATED_CODE)
            .remark(UPDATED_REMARK);

        restHerdMockMvc.perform(put("/api/herds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHerd)))
            .andExpect(status().isOk());

        // Validate the Herd in the database
        List<Herd> herdList = herdRepository.findAll();
        assertThat(herdList).hasSize(databaseSizeBeforeUpdate);
        Herd testHerd = herdList.get(herdList.size() - 1);
        assertThat(testHerd.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHerd.getNutrition()).isEqualTo(UPDATED_NUTRITION);
        assertThat(testHerd.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testHerd.getRemark()).isEqualTo(UPDATED_REMARK);
    }

    @Test
    @Transactional
    public void updateNonExistingHerd() throws Exception {
        int databaseSizeBeforeUpdate = herdRepository.findAll().size();

        // Create the Herd

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHerdMockMvc.perform(put("/api/herds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(herd)))
            .andExpect(status().isBadRequest());

        // Validate the Herd in the database
        List<Herd> herdList = herdRepository.findAll();
        assertThat(herdList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHerd() throws Exception {
        // Initialize the database
        herdService.save(herd);

        int databaseSizeBeforeDelete = herdRepository.findAll().size();

        // Delete the herd
        restHerdMockMvc.perform(delete("/api/herds/{id}", herd.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Herd> herdList = herdRepository.findAll();
        assertThat(herdList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Herd.class);
        Herd herd1 = new Herd();
        herd1.setId(1L);
        Herd herd2 = new Herd();
        herd2.setId(herd1.getId());
        assertThat(herd1).isEqualTo(herd2);
        herd2.setId(2L);
        assertThat(herd1).isNotEqualTo(herd2);
        herd1.setId(null);
        assertThat(herd1).isNotEqualTo(herd2);
    }
}
