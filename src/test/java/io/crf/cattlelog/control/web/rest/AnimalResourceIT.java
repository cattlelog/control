package io.crf.cattlelog.control.web.rest;

import io.crf.cattlelog.control.ControlApp;
import io.crf.cattlelog.control.domain.Animal;
import io.crf.cattlelog.control.repository.AnimalRepository;
import io.crf.cattlelog.control.service.AnimalService;
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

import io.crf.cattlelog.control.domain.enumeration.Origin;
import io.crf.cattlelog.control.domain.enumeration.Gender;
import io.crf.cattlelog.control.domain.enumeration.Conception;
/**
 * Integration tests for the {@Link AnimalResource} REST controller.
 */
@SpringBootTest(classes = ControlApp.class)
public class AnimalResourceIT {

    private static final String DEFAULT_EARRING = "AAAAAAAAAA";
    private static final String UPDATED_EARRING = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_IDENTIFICATION = "AAAAAAAAAA";
    private static final String UPDATED_IDENTIFICATION = "BBBBBBBBBB";

    private static final String DEFAULT_REMARK = "AAAAAAAAAA";
    private static final String UPDATED_REMARK = "BBBBBBBBBB";

    private static final String DEFAULT_BREEDER = "AAAAAAAAAA";
    private static final String UPDATED_BREEDER = "BBBBBBBBBB";

    private static final Origin DEFAULT_ORIGIN = Origin.BUY;
    private static final Origin UPDATED_ORIGIN = Origin.BORN;

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final Conception DEFAULT_CONCEPTION = Conception.RIDE;
    private static final Conception UPDATED_CONCEPTION = Conception.INSEMINATION;

    private static final LocalDate DEFAULT_DATE_OF_BIRTH = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_BIRTH = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_OF_PURCHASE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_PURCHASE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_OF_WEANING = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_OF_WEANING = LocalDate.now(ZoneId.systemDefault());

    private static final Float DEFAULT_WEIGHT_AT_BIRTH = 1F;
    private static final Float UPDATED_WEIGHT_AT_BIRTH = 2F;

    private static final Float DEFAULT_WEIGHT_AT_WEANING = 1F;
    private static final Float UPDATED_WEIGHT_AT_WEANING = 2F;

    private static final Float DEFAULT_WEIGHT_205 = 1F;
    private static final Float UPDATED_WEIGHT_205 = 2F;

    private static final Float DEFAULT_WEIGHT_365 = 1F;
    private static final Float UPDATED_WEIGHT_365 = 2F;

    private static final Float DEFAULT_SCROTUM = 1F;
    private static final Float UPDATED_SCROTUM = 2F;

    private static final Integer DEFAULT_RANCH = 1;
    private static final Integer UPDATED_RANCH = 2;

    private static final Float DEFAULT_MPHP = 1F;
    private static final Float UPDATED_MPHP = 2F;

    @Autowired
    private AnimalRepository animalRepository;

    @Autowired
    private AnimalService animalService;

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

    private MockMvc restAnimalMockMvc;

    private Animal animal;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final AnimalResource animalResource = new AnimalResource(animalService);
        this.restAnimalMockMvc = MockMvcBuilders.standaloneSetup(animalResource)
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
    public static Animal createEntity(EntityManager em) {
        Animal animal = new Animal()
            .earring(DEFAULT_EARRING)
            .code(DEFAULT_CODE)
            .identification(DEFAULT_IDENTIFICATION)
            .remark(DEFAULT_REMARK)
            .breeder(DEFAULT_BREEDER)
            .origin(DEFAULT_ORIGIN)
            .gender(DEFAULT_GENDER)
            .conception(DEFAULT_CONCEPTION)
            .dateOfBirth(DEFAULT_DATE_OF_BIRTH)
            .dateOfPurchase(DEFAULT_DATE_OF_PURCHASE)
            .dateOfWeaning(DEFAULT_DATE_OF_WEANING)
            .weightAtBirth(DEFAULT_WEIGHT_AT_BIRTH)
            .weightAtWeaning(DEFAULT_WEIGHT_AT_WEANING)
            .weight205(DEFAULT_WEIGHT_205)
            .weight365(DEFAULT_WEIGHT_365)
            .scrotum(DEFAULT_SCROTUM)
            .ranch(DEFAULT_RANCH)
            .mphp(DEFAULT_MPHP);
        return animal;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Animal createUpdatedEntity(EntityManager em) {
        Animal animal = new Animal()
            .earring(UPDATED_EARRING)
            .code(UPDATED_CODE)
            .identification(UPDATED_IDENTIFICATION)
            .remark(UPDATED_REMARK)
            .breeder(UPDATED_BREEDER)
            .origin(UPDATED_ORIGIN)
            .gender(UPDATED_GENDER)
            .conception(UPDATED_CONCEPTION)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .dateOfPurchase(UPDATED_DATE_OF_PURCHASE)
            .dateOfWeaning(UPDATED_DATE_OF_WEANING)
            .weightAtBirth(UPDATED_WEIGHT_AT_BIRTH)
            .weightAtWeaning(UPDATED_WEIGHT_AT_WEANING)
            .weight205(UPDATED_WEIGHT_205)
            .weight365(UPDATED_WEIGHT_365)
            .scrotum(UPDATED_SCROTUM)
            .ranch(UPDATED_RANCH)
            .mphp(UPDATED_MPHP);
        return animal;
    }

    @BeforeEach
    public void initTest() {
        animal = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnimal() throws Exception {
        int databaseSizeBeforeCreate = animalRepository.findAll().size();

        // Create the Animal
        restAnimalMockMvc.perform(post("/api/animals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(animal)))
            .andExpect(status().isCreated());

        // Validate the Animal in the database
        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeCreate + 1);
        Animal testAnimal = animalList.get(animalList.size() - 1);
        assertThat(testAnimal.getEarring()).isEqualTo(DEFAULT_EARRING);
        assertThat(testAnimal.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testAnimal.getIdentification()).isEqualTo(DEFAULT_IDENTIFICATION);
        assertThat(testAnimal.getRemark()).isEqualTo(DEFAULT_REMARK);
        assertThat(testAnimal.getBreeder()).isEqualTo(DEFAULT_BREEDER);
        assertThat(testAnimal.getOrigin()).isEqualTo(DEFAULT_ORIGIN);
        assertThat(testAnimal.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testAnimal.getConception()).isEqualTo(DEFAULT_CONCEPTION);
        assertThat(testAnimal.getDateOfBirth()).isEqualTo(DEFAULT_DATE_OF_BIRTH);
        assertThat(testAnimal.getDateOfPurchase()).isEqualTo(DEFAULT_DATE_OF_PURCHASE);
        assertThat(testAnimal.getDateOfWeaning()).isEqualTo(DEFAULT_DATE_OF_WEANING);
        assertThat(testAnimal.getWeightAtBirth()).isEqualTo(DEFAULT_WEIGHT_AT_BIRTH);
        assertThat(testAnimal.getWeightAtWeaning()).isEqualTo(DEFAULT_WEIGHT_AT_WEANING);
        assertThat(testAnimal.getWeight205()).isEqualTo(DEFAULT_WEIGHT_205);
        assertThat(testAnimal.getWeight365()).isEqualTo(DEFAULT_WEIGHT_365);
        assertThat(testAnimal.getScrotum()).isEqualTo(DEFAULT_SCROTUM);
        assertThat(testAnimal.getRanch()).isEqualTo(DEFAULT_RANCH);
        assertThat(testAnimal.getMphp()).isEqualTo(DEFAULT_MPHP);
    }

    @Test
    @Transactional
    public void createAnimalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = animalRepository.findAll().size();

        // Create the Animal with an existing ID
        animal.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnimalMockMvc.perform(post("/api/animals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(animal)))
            .andExpect(status().isBadRequest());

        // Validate the Animal in the database
        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkEarringIsRequired() throws Exception {
        int databaseSizeBeforeTest = animalRepository.findAll().size();
        // set the field null
        animal.setEarring(null);

        // Create the Animal, which fails.

        restAnimalMockMvc.perform(post("/api/animals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(animal)))
            .andExpect(status().isBadRequest());

        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnimals() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get all the animalList
        restAnimalMockMvc.perform(get("/api/animals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(animal.getId().intValue())))
            .andExpect(jsonPath("$.[*].earring").value(hasItem(DEFAULT_EARRING.toString())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].identification").value(hasItem(DEFAULT_IDENTIFICATION.toString())))
            .andExpect(jsonPath("$.[*].remark").value(hasItem(DEFAULT_REMARK.toString())))
            .andExpect(jsonPath("$.[*].breeder").value(hasItem(DEFAULT_BREEDER.toString())))
            .andExpect(jsonPath("$.[*].origin").value(hasItem(DEFAULT_ORIGIN.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].conception").value(hasItem(DEFAULT_CONCEPTION.toString())))
            .andExpect(jsonPath("$.[*].dateOfBirth").value(hasItem(DEFAULT_DATE_OF_BIRTH.toString())))
            .andExpect(jsonPath("$.[*].dateOfPurchase").value(hasItem(DEFAULT_DATE_OF_PURCHASE.toString())))
            .andExpect(jsonPath("$.[*].dateOfWeaning").value(hasItem(DEFAULT_DATE_OF_WEANING.toString())))
            .andExpect(jsonPath("$.[*].weightAtBirth").value(hasItem(DEFAULT_WEIGHT_AT_BIRTH.doubleValue())))
            .andExpect(jsonPath("$.[*].weightAtWeaning").value(hasItem(DEFAULT_WEIGHT_AT_WEANING.doubleValue())))
            .andExpect(jsonPath("$.[*].weight205").value(hasItem(DEFAULT_WEIGHT_205.doubleValue())))
            .andExpect(jsonPath("$.[*].weight365").value(hasItem(DEFAULT_WEIGHT_365.doubleValue())))
            .andExpect(jsonPath("$.[*].scrotum").value(hasItem(DEFAULT_SCROTUM.doubleValue())))
            .andExpect(jsonPath("$.[*].ranch").value(hasItem(DEFAULT_RANCH)))
            .andExpect(jsonPath("$.[*].mphp").value(hasItem(DEFAULT_MPHP.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getAnimal() throws Exception {
        // Initialize the database
        animalRepository.saveAndFlush(animal);

        // Get the animal
        restAnimalMockMvc.perform(get("/api/animals/{id}", animal.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(animal.getId().intValue()))
            .andExpect(jsonPath("$.earring").value(DEFAULT_EARRING.toString()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.identification").value(DEFAULT_IDENTIFICATION.toString()))
            .andExpect(jsonPath("$.remark").value(DEFAULT_REMARK.toString()))
            .andExpect(jsonPath("$.breeder").value(DEFAULT_BREEDER.toString()))
            .andExpect(jsonPath("$.origin").value(DEFAULT_ORIGIN.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.conception").value(DEFAULT_CONCEPTION.toString()))
            .andExpect(jsonPath("$.dateOfBirth").value(DEFAULT_DATE_OF_BIRTH.toString()))
            .andExpect(jsonPath("$.dateOfPurchase").value(DEFAULT_DATE_OF_PURCHASE.toString()))
            .andExpect(jsonPath("$.dateOfWeaning").value(DEFAULT_DATE_OF_WEANING.toString()))
            .andExpect(jsonPath("$.weightAtBirth").value(DEFAULT_WEIGHT_AT_BIRTH.doubleValue()))
            .andExpect(jsonPath("$.weightAtWeaning").value(DEFAULT_WEIGHT_AT_WEANING.doubleValue()))
            .andExpect(jsonPath("$.weight205").value(DEFAULT_WEIGHT_205.doubleValue()))
            .andExpect(jsonPath("$.weight365").value(DEFAULT_WEIGHT_365.doubleValue()))
            .andExpect(jsonPath("$.scrotum").value(DEFAULT_SCROTUM.doubleValue()))
            .andExpect(jsonPath("$.ranch").value(DEFAULT_RANCH))
            .andExpect(jsonPath("$.mphp").value(DEFAULT_MPHP.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAnimal() throws Exception {
        // Get the animal
        restAnimalMockMvc.perform(get("/api/animals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnimal() throws Exception {
        // Initialize the database
        animalService.save(animal);

        int databaseSizeBeforeUpdate = animalRepository.findAll().size();

        // Update the animal
        Animal updatedAnimal = animalRepository.findById(animal.getId()).get();
        // Disconnect from session so that the updates on updatedAnimal are not directly saved in db
        em.detach(updatedAnimal);
        updatedAnimal
            .earring(UPDATED_EARRING)
            .code(UPDATED_CODE)
            .identification(UPDATED_IDENTIFICATION)
            .remark(UPDATED_REMARK)
            .breeder(UPDATED_BREEDER)
            .origin(UPDATED_ORIGIN)
            .gender(UPDATED_GENDER)
            .conception(UPDATED_CONCEPTION)
            .dateOfBirth(UPDATED_DATE_OF_BIRTH)
            .dateOfPurchase(UPDATED_DATE_OF_PURCHASE)
            .dateOfWeaning(UPDATED_DATE_OF_WEANING)
            .weightAtBirth(UPDATED_WEIGHT_AT_BIRTH)
            .weightAtWeaning(UPDATED_WEIGHT_AT_WEANING)
            .weight205(UPDATED_WEIGHT_205)
            .weight365(UPDATED_WEIGHT_365)
            .scrotum(UPDATED_SCROTUM)
            .ranch(UPDATED_RANCH)
            .mphp(UPDATED_MPHP);

        restAnimalMockMvc.perform(put("/api/animals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAnimal)))
            .andExpect(status().isOk());

        // Validate the Animal in the database
        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeUpdate);
        Animal testAnimal = animalList.get(animalList.size() - 1);
        assertThat(testAnimal.getEarring()).isEqualTo(UPDATED_EARRING);
        assertThat(testAnimal.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testAnimal.getIdentification()).isEqualTo(UPDATED_IDENTIFICATION);
        assertThat(testAnimal.getRemark()).isEqualTo(UPDATED_REMARK);
        assertThat(testAnimal.getBreeder()).isEqualTo(UPDATED_BREEDER);
        assertThat(testAnimal.getOrigin()).isEqualTo(UPDATED_ORIGIN);
        assertThat(testAnimal.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testAnimal.getConception()).isEqualTo(UPDATED_CONCEPTION);
        assertThat(testAnimal.getDateOfBirth()).isEqualTo(UPDATED_DATE_OF_BIRTH);
        assertThat(testAnimal.getDateOfPurchase()).isEqualTo(UPDATED_DATE_OF_PURCHASE);
        assertThat(testAnimal.getDateOfWeaning()).isEqualTo(UPDATED_DATE_OF_WEANING);
        assertThat(testAnimal.getWeightAtBirth()).isEqualTo(UPDATED_WEIGHT_AT_BIRTH);
        assertThat(testAnimal.getWeightAtWeaning()).isEqualTo(UPDATED_WEIGHT_AT_WEANING);
        assertThat(testAnimal.getWeight205()).isEqualTo(UPDATED_WEIGHT_205);
        assertThat(testAnimal.getWeight365()).isEqualTo(UPDATED_WEIGHT_365);
        assertThat(testAnimal.getScrotum()).isEqualTo(UPDATED_SCROTUM);
        assertThat(testAnimal.getRanch()).isEqualTo(UPDATED_RANCH);
        assertThat(testAnimal.getMphp()).isEqualTo(UPDATED_MPHP);
    }

    @Test
    @Transactional
    public void updateNonExistingAnimal() throws Exception {
        int databaseSizeBeforeUpdate = animalRepository.findAll().size();

        // Create the Animal

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnimalMockMvc.perform(put("/api/animals")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(animal)))
            .andExpect(status().isBadRequest());

        // Validate the Animal in the database
        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAnimal() throws Exception {
        // Initialize the database
        animalService.save(animal);

        int databaseSizeBeforeDelete = animalRepository.findAll().size();

        // Delete the animal
        restAnimalMockMvc.perform(delete("/api/animals/{id}", animal.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Animal> animalList = animalRepository.findAll();
        assertThat(animalList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Animal.class);
        Animal animal1 = new Animal();
        animal1.setId(1L);
        Animal animal2 = new Animal();
        animal2.setId(animal1.getId());
        assertThat(animal1).isEqualTo(animal2);
        animal2.setId(2L);
        assertThat(animal1).isNotEqualTo(animal2);
        animal1.setId(null);
        assertThat(animal1).isNotEqualTo(animal2);
    }
}
