package io.crf.cattlelog.control.web.rest;

import io.crf.cattlelog.control.ControlApp;
import io.crf.cattlelog.control.domain.Breed;
import io.crf.cattlelog.control.domain.Kind;
import io.crf.cattlelog.control.repository.BreedRepository;
import io.crf.cattlelog.control.service.BreedService;
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
 * Integration tests for the {@Link BreedResource} REST controller.
 */
@SpringBootTest(classes = ControlApp.class)
public class BreedResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    @Autowired
    private BreedRepository breedRepository;

    @Autowired
    private BreedService breedService;

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

    private MockMvc restBreedMockMvc;

    private Breed breed;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BreedResource breedResource = new BreedResource(breedService);
        this.restBreedMockMvc = MockMvcBuilders.standaloneSetup(breedResource)
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
    public static Breed createEntity(EntityManager em) {
        Breed breed = new Breed()
            .name(DEFAULT_NAME)
            .remark(DEFAULT_REMARK);
        // Add required entity
        Kind kind;
        if (TestUtil.findAll(em, Kind.class).isEmpty()) {
            kind = KindResourceIT.createEntity(em);
            em.persist(kind);
            em.flush();
        } else {
            kind = TestUtil.findAll(em, Kind.class).get(0);
        }
        breed.setKind(kind);
        return breed;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Breed createUpdatedEntity(EntityManager em) {
        Breed breed = new Breed()
            .name(UPDATED_NAME)
            .remark(UPDATED_REMARK);
        // Add required entity
        Kind kind;
        if (TestUtil.findAll(em, Kind.class).isEmpty()) {
            kind = KindResourceIT.createUpdatedEntity(em);
            em.persist(kind);
            em.flush();
        } else {
            kind = TestUtil.findAll(em, Kind.class).get(0);
        }
        breed.setKind(kind);
        return breed;
    }

    @BeforeEach
    public void initTest() {
        breed = createEntity(em);
    }

    @Test
    @Transactional
    public void createBreed() throws Exception {
        int databaseSizeBeforeCreate = breedRepository.findAll().size();

        // Create the Breed
        restBreedMockMvc.perform(post("/api/breeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(breed)))
            .andExpect(status().isCreated());

        // Validate the Breed in the database
        List<Breed> breedList = breedRepository.findAll();
        assertThat(breedList).hasSize(databaseSizeBeforeCreate + 1);
        Breed testBreed = breedList.get(breedList.size() - 1);
        assertThat(testBreed.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBreed.getRemark()).isEqualTo(DEFAULT_REMARK);
    }

    @Test
    @Transactional
    public void createBreedWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = breedRepository.findAll().size();

        // Create the Breed with an existing ID
        breed.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBreedMockMvc.perform(post("/api/breeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(breed)))
            .andExpect(status().isBadRequest());

        // Validate the Breed in the database
        List<Breed> breedList = breedRepository.findAll();
        assertThat(breedList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = breedRepository.findAll().size();
        // set the field null
        breed.setName(null);

        // Create the Breed, which fails.

        restBreedMockMvc.perform(post("/api/breeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(breed)))
            .andExpect(status().isBadRequest());

        List<Breed> breedList = breedRepository.findAll();
        assertThat(breedList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBreeds() throws Exception {
        // Initialize the database
        breedRepository.saveAndFlush(breed);

        // Get all the breedList
        restBreedMockMvc.perform(get("/api/breeds?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(breed.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())));
    }
    
    @Test
    @Transactional
    public void getBreed() throws Exception {
        // Initialize the database
        breedRepository.saveAndFlush(breed);

        // Get the breed
        restBreedMockMvc.perform(get("/api/breeds/{id}", breed.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(breed.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBreed() throws Exception {
        // Get the breed
        restBreedMockMvc.perform(get("/api/breeds/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBreed() throws Exception {
        // Initialize the database
        breedService.save(breed);

        int databaseSizeBeforeUpdate = breedRepository.findAll().size();

        // Update the breed
        Breed updatedBreed = breedRepository.findById(breed.getId()).get();
        // Disconnect from session so that the updates on updatedBreed are not directly saved in db
        em.detach(updatedBreed);
        updatedBreed
            .name(UPDATED_NAME)
            .remark(UPDATED_REMARK);

        restBreedMockMvc.perform(put("/api/breeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedBreed)))
            .andExpect(status().isOk());

        // Validate the Breed in the database
        List<Breed> breedList = breedRepository.findAll();
        assertThat(breedList).hasSize(databaseSizeBeforeUpdate);
        Breed testBreed = breedList.get(breedList.size() - 1);
        assertThat(testBreed.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBreed.getRemark()).isEqualTo(UPDATED_REMARK);
    }

    @Test
    @Transactional
    public void updateNonExistingBreed() throws Exception {
        int databaseSizeBeforeUpdate = breedRepository.findAll().size();

        // Create the Breed

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBreedMockMvc.perform(put("/api/breeds")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(breed)))
            .andExpect(status().isBadRequest());

        // Validate the Breed in the database
        List<Breed> breedList = breedRepository.findAll();
        assertThat(breedList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBreed() throws Exception {
        // Initialize the database
        breedService.save(breed);

        int databaseSizeBeforeDelete = breedRepository.findAll().size();

        // Delete the breed
        restBreedMockMvc.perform(delete("/api/breeds/{id}", breed.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Breed> breedList = breedRepository.findAll();
        assertThat(breedList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Breed.class);
        Breed breed1 = new Breed();
        breed1.setId(1L);
        Breed breed2 = new Breed();
        breed2.setId(breed1.getId());
        assertThat(breed1).isEqualTo(breed2);
        breed2.setId(2L);
        assertThat(breed1).isNotEqualTo(breed2);
        breed1.setId(null);
        assertThat(breed1).isNotEqualTo(breed2);
    }
}
