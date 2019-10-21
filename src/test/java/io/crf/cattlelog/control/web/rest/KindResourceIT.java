package io.crf.cattlelog.control.web.rest;

import io.crf.cattlelog.control.ControlApp;
import io.crf.cattlelog.control.domain.Kind;
import io.crf.cattlelog.control.repository.KindRepository;
import io.crf.cattlelog.control.service.KindService;
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
 * Integration tests for the {@Link KindResource} REST controller.
 */
@SpringBootTest(classes = ControlApp.class)
public class KindResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    @Autowired
    private KindRepository kindRepository;

    @Autowired
    private KindService kindService;

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

    private MockMvc restKindMockMvc;

    private Kind kind;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final KindResource kindResource = new KindResource(kindService);
        this.restKindMockMvc = MockMvcBuilders.standaloneSetup(kindResource)
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
    public static Kind createEntity(EntityManager em) {
        Kind kind = new Kind()
            .name(DEFAULT_NAME)
            .remark(DEFAULT_REMARK);
        return kind;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Kind createUpdatedEntity(EntityManager em) {
        Kind kind = new Kind()
            .name(UPDATED_NAME)
            .remark(UPDATED_REMARK);
        return kind;
    }

    @BeforeEach
    public void initTest() {
        kind = createEntity(em);
    }

    @Test
    @Transactional
    public void createKind() throws Exception {
        int databaseSizeBeforeCreate = kindRepository.findAll().size();

        // Create the Kind
        restKindMockMvc.perform(post("/api/kinds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(kind)))
            .andExpect(status().isCreated());

        // Validate the Kind in the database
        List<Kind> kindList = kindRepository.findAll();
        assertThat(kindList).hasSize(databaseSizeBeforeCreate + 1);
        Kind testKind = kindList.get(kindList.size() - 1);
        assertThat(testKind.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testKind.getRemark()).isEqualTo(DEFAULT_REMARK);
    }

    @Test
    @Transactional
    public void createKindWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = kindRepository.findAll().size();

        // Create the Kind with an existing ID
        kind.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restKindMockMvc.perform(post("/api/kinds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(kind)))
            .andExpect(status().isBadRequest());

        // Validate the Kind in the database
        List<Kind> kindList = kindRepository.findAll();
        assertThat(kindList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = kindRepository.findAll().size();
        // set the field null
        kind.setName(null);

        // Create the Kind, which fails.

        restKindMockMvc.perform(post("/api/kinds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(kind)))
            .andExpect(status().isBadRequest());

        List<Kind> kindList = kindRepository.findAll();
        assertThat(kindList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllKinds() throws Exception {
        // Initialize the database
        kindRepository.saveAndFlush(kind);

        // Get all the kindList
        restKindMockMvc.perform(get("/api/kinds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kind.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())));
    }
    
    @Test
    @Transactional
    public void getKind() throws Exception {
        // Initialize the database
        kindRepository.saveAndFlush(kind);

        // Get the kind
        restKindMockMvc.perform(get("/api/kinds/{id}", kind.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(kind.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingKind() throws Exception {
        // Get the kind
        restKindMockMvc.perform(get("/api/kinds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateKind() throws Exception {
        // Initialize the database
        kindService.save(kind);

        int databaseSizeBeforeUpdate = kindRepository.findAll().size();

        // Update the kind
        Kind updatedKind = kindRepository.findById(kind.getId()).get();
        // Disconnect from session so that the updates on updatedKind are not directly saved in db
        em.detach(updatedKind);
        updatedKind
            .name(UPDATED_NAME)
            .remark(UPDATED_REMARK);

        restKindMockMvc.perform(put("/api/kinds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedKind)))
            .andExpect(status().isOk());

        // Validate the Kind in the database
        List<Kind> kindList = kindRepository.findAll();
        assertThat(kindList).hasSize(databaseSizeBeforeUpdate);
        Kind testKind = kindList.get(kindList.size() - 1);
        assertThat(testKind.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testKind.getRemark()).isEqualTo(UPDATED_REMARK);
    }

    @Test
    @Transactional
    public void updateNonExistingKind() throws Exception {
        int databaseSizeBeforeUpdate = kindRepository.findAll().size();

        // Create the Kind

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKindMockMvc.perform(put("/api/kinds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(kind)))
            .andExpect(status().isBadRequest());

        // Validate the Kind in the database
        List<Kind> kindList = kindRepository.findAll();
        assertThat(kindList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteKind() throws Exception {
        // Initialize the database
        kindService.save(kind);

        int databaseSizeBeforeDelete = kindRepository.findAll().size();

        // Delete the kind
        restKindMockMvc.perform(delete("/api/kinds/{id}", kind.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Kind> kindList = kindRepository.findAll();
        assertThat(kindList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Kind.class);
        Kind kind1 = new Kind();
        kind1.setId(1L);
        Kind kind2 = new Kind();
        kind2.setId(kind1.getId());
        assertThat(kind1).isEqualTo(kind2);
        kind2.setId(2L);
        assertThat(kind1).isNotEqualTo(kind2);
        kind1.setId(null);
        assertThat(kind1).isNotEqualTo(kind2);
    }
}
