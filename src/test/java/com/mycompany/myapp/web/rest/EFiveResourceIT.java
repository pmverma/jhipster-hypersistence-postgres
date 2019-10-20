package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;
import com.mycompany.myapp.domain.EFive;
import com.mycompany.myapp.repository.EFiveRepository;
import com.mycompany.myapp.service.EFiveService;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

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

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EFiveResource} REST controller.
 */
@SpringBootTest(classes = JhipsterApp.class)
public class EFiveResourceIT {

    private static final String DEFAULT_FIVE = "AAAAAAAAAA";
    private static final String UPDATED_FIVE = "BBBBBBBBBB";

    @Autowired
    private EFiveRepository eFiveRepository;

    @Autowired
    private EFiveService eFiveService;

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

    private MockMvc restEFiveMockMvc;

    private EFive eFive;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EFiveResource eFiveResource = new EFiveResource(eFiveService);
        this.restEFiveMockMvc = MockMvcBuilders.standaloneSetup(eFiveResource)
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
    public static EFive createEntity(EntityManager em) {
        EFive eFive = new EFive()
            .five(DEFAULT_FIVE);
        return eFive;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EFive createUpdatedEntity(EntityManager em) {
        EFive eFive = new EFive()
            .five(UPDATED_FIVE);
        return eFive;
    }

    @BeforeEach
    public void initTest() {
        eFive = createEntity(em);
    }

    @Test
    @Transactional
    public void createEFive() throws Exception {
        int databaseSizeBeforeCreate = eFiveRepository.findAll().size();

        // Create the EFive
        restEFiveMockMvc.perform(post("/api/e-fives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eFive)))
            .andExpect(status().isCreated());

        // Validate the EFive in the database
        List<EFive> eFiveList = eFiveRepository.findAll();
        assertThat(eFiveList).hasSize(databaseSizeBeforeCreate + 1);
        EFive testEFive = eFiveList.get(eFiveList.size() - 1);
        assertThat(testEFive.getFive()).isEqualTo(DEFAULT_FIVE);
    }

    @Test
    @Transactional
    public void createEFiveWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eFiveRepository.findAll().size();

        // Create the EFive with an existing ID
        eFive.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEFiveMockMvc.perform(post("/api/e-fives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eFive)))
            .andExpect(status().isBadRequest());

        // Validate the EFive in the database
        List<EFive> eFiveList = eFiveRepository.findAll();
        assertThat(eFiveList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = eFiveRepository.findAll().size();
        // set the field null
        eFive.setFive(null);

        // Create the EFive, which fails.

        restEFiveMockMvc.perform(post("/api/e-fives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eFive)))
            .andExpect(status().isBadRequest());

        List<EFive> eFiveList = eFiveRepository.findAll();
        assertThat(eFiveList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEFives() throws Exception {
        // Initialize the database
        eFiveRepository.saveAndFlush(eFive);

        // Get all the eFiveList
        restEFiveMockMvc.perform(get("/api/e-fives?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eFive.getId().intValue())))
            .andExpect(jsonPath("$.[*].five").value(hasItem(DEFAULT_FIVE)));
    }
    
    @Test
    @Transactional
    public void getEFive() throws Exception {
        // Initialize the database
        eFiveRepository.saveAndFlush(eFive);

        // Get the eFive
        restEFiveMockMvc.perform(get("/api/e-fives/{id}", eFive.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(eFive.getId().intValue()))
            .andExpect(jsonPath("$.five").value(DEFAULT_FIVE));
    }

    @Test
    @Transactional
    public void getNonExistingEFive() throws Exception {
        // Get the eFive
        restEFiveMockMvc.perform(get("/api/e-fives/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEFive() throws Exception {
        // Initialize the database
        eFiveService.save(eFive);

        int databaseSizeBeforeUpdate = eFiveRepository.findAll().size();

        // Update the eFive
        EFive updatedEFive = eFiveRepository.findById(eFive.getId()).get();
        // Disconnect from session so that the updates on updatedEFive are not directly saved in db
        em.detach(updatedEFive);
        updatedEFive
            .five(UPDATED_FIVE);

        restEFiveMockMvc.perform(put("/api/e-fives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEFive)))
            .andExpect(status().isOk());

        // Validate the EFive in the database
        List<EFive> eFiveList = eFiveRepository.findAll();
        assertThat(eFiveList).hasSize(databaseSizeBeforeUpdate);
        EFive testEFive = eFiveList.get(eFiveList.size() - 1);
        assertThat(testEFive.getFive()).isEqualTo(UPDATED_FIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingEFive() throws Exception {
        int databaseSizeBeforeUpdate = eFiveRepository.findAll().size();

        // Create the EFive

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEFiveMockMvc.perform(put("/api/e-fives")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eFive)))
            .andExpect(status().isBadRequest());

        // Validate the EFive in the database
        List<EFive> eFiveList = eFiveRepository.findAll();
        assertThat(eFiveList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEFive() throws Exception {
        // Initialize the database
        eFiveService.save(eFive);

        int databaseSizeBeforeDelete = eFiveRepository.findAll().size();

        // Delete the eFive
        restEFiveMockMvc.perform(delete("/api/e-fives/{id}", eFive.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EFive> eFiveList = eFiveRepository.findAll();
        assertThat(eFiveList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EFive.class);
        EFive eFive1 = new EFive();
        eFive1.setId(1L);
        EFive eFive2 = new EFive();
        eFive2.setId(eFive1.getId());
        assertThat(eFive1).isEqualTo(eFive2);
        eFive2.setId(2L);
        assertThat(eFive1).isNotEqualTo(eFive2);
        eFive1.setId(null);
        assertThat(eFive1).isNotEqualTo(eFive2);
    }
}
