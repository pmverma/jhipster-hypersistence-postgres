package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;
import com.mycompany.myapp.domain.EOne;
import com.mycompany.myapp.domain.ETwo;
import com.mycompany.myapp.repository.EOneRepository;
import com.mycompany.myapp.service.EOneService;
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
 * Integration tests for the {@link EOneResource} REST controller.
 */
@SpringBootTest(classes = JhipsterApp.class)
public class EOneResourceIT {

    private static final String DEFAULT_ONE = "AAAAAAAAAA";
    private static final String UPDATED_ONE = "BBBBBBBBBB";

    @Autowired
    private EOneRepository eOneRepository;

    @Autowired
    private EOneService eOneService;

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

    private MockMvc restEOneMockMvc;

    private EOne eOne;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EOneResource eOneResource = new EOneResource(eOneService);
        this.restEOneMockMvc = MockMvcBuilders.standaloneSetup(eOneResource)
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
    public static EOne createEntity(EntityManager em) {
        EOne eOne = new EOne()
            .one(DEFAULT_ONE);
        // Add required entity
        ETwo eTwo;
        if (TestUtil.findAll(em, ETwo.class).isEmpty()) {
            eTwo = ETwoResourceIT.createEntity(em);
            em.persist(eTwo);
            em.flush();
        } else {
            eTwo = TestUtil.findAll(em, ETwo.class).get(0);
        }
        eOne.getETwoOS().add(eTwo);
        return eOne;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EOne createUpdatedEntity(EntityManager em) {
        EOne eOne = new EOne()
            .one(UPDATED_ONE);
        // Add required entity
        ETwo eTwo;
        if (TestUtil.findAll(em, ETwo.class).isEmpty()) {
            eTwo = ETwoResourceIT.createUpdatedEntity(em);
            em.persist(eTwo);
            em.flush();
        } else {
            eTwo = TestUtil.findAll(em, ETwo.class).get(0);
        }
        eOne.getETwoOS().add(eTwo);
        return eOne;
    }

    @BeforeEach
    public void initTest() {
        eOne = createEntity(em);
    }

    @Test
    @Transactional
    public void createEOne() throws Exception {
        int databaseSizeBeforeCreate = eOneRepository.findAll().size();

        // Create the EOne
        restEOneMockMvc.perform(post("/api/e-ones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eOne)))
            .andExpect(status().isCreated());

        // Validate the EOne in the database
        List<EOne> eOneList = eOneRepository.findAll();
        assertThat(eOneList).hasSize(databaseSizeBeforeCreate + 1);
        EOne testEOne = eOneList.get(eOneList.size() - 1);
        assertThat(testEOne.getOne()).isEqualTo(DEFAULT_ONE);
    }

    @Test
    @Transactional
    public void createEOneWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eOneRepository.findAll().size();

        // Create the EOne with an existing ID
        eOne.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEOneMockMvc.perform(post("/api/e-ones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eOne)))
            .andExpect(status().isBadRequest());

        // Validate the EOne in the database
        List<EOne> eOneList = eOneRepository.findAll();
        assertThat(eOneList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkOneIsRequired() throws Exception {
        int databaseSizeBeforeTest = eOneRepository.findAll().size();
        // set the field null
        eOne.setOne(null);

        // Create the EOne, which fails.

        restEOneMockMvc.perform(post("/api/e-ones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eOne)))
            .andExpect(status().isBadRequest());

        List<EOne> eOneList = eOneRepository.findAll();
        assertThat(eOneList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEOnes() throws Exception {
        // Initialize the database
        eOneRepository.saveAndFlush(eOne);

        // Get all the eOneList
        restEOneMockMvc.perform(get("/api/e-ones?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eOne.getId().intValue())))
            .andExpect(jsonPath("$.[*].one").value(hasItem(DEFAULT_ONE)));
    }
    
    @Test
    @Transactional
    public void getEOne() throws Exception {
        // Initialize the database
        eOneRepository.saveAndFlush(eOne);

        // Get the eOne
        restEOneMockMvc.perform(get("/api/e-ones/{id}", eOne.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(eOne.getId().intValue()))
            .andExpect(jsonPath("$.one").value(DEFAULT_ONE));
    }

    @Test
    @Transactional
    public void getNonExistingEOne() throws Exception {
        // Get the eOne
        restEOneMockMvc.perform(get("/api/e-ones/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEOne() throws Exception {
        // Initialize the database
        eOneService.save(eOne);

        int databaseSizeBeforeUpdate = eOneRepository.findAll().size();

        // Update the eOne
        EOne updatedEOne = eOneRepository.findById(eOne.getId()).get();
        // Disconnect from session so that the updates on updatedEOne are not directly saved in db
        em.detach(updatedEOne);
        updatedEOne
            .one(UPDATED_ONE);

        restEOneMockMvc.perform(put("/api/e-ones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEOne)))
            .andExpect(status().isOk());

        // Validate the EOne in the database
        List<EOne> eOneList = eOneRepository.findAll();
        assertThat(eOneList).hasSize(databaseSizeBeforeUpdate);
        EOne testEOne = eOneList.get(eOneList.size() - 1);
        assertThat(testEOne.getOne()).isEqualTo(UPDATED_ONE);
    }

    @Test
    @Transactional
    public void updateNonExistingEOne() throws Exception {
        int databaseSizeBeforeUpdate = eOneRepository.findAll().size();

        // Create the EOne

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEOneMockMvc.perform(put("/api/e-ones")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eOne)))
            .andExpect(status().isBadRequest());

        // Validate the EOne in the database
        List<EOne> eOneList = eOneRepository.findAll();
        assertThat(eOneList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEOne() throws Exception {
        // Initialize the database
        eOneService.save(eOne);

        int databaseSizeBeforeDelete = eOneRepository.findAll().size();

        // Delete the eOne
        restEOneMockMvc.perform(delete("/api/e-ones/{id}", eOne.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EOne> eOneList = eOneRepository.findAll();
        assertThat(eOneList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EOne.class);
        EOne eOne1 = new EOne();
        eOne1.setId(1L);
        EOne eOne2 = new EOne();
        eOne2.setId(eOne1.getId());
        assertThat(eOne1).isEqualTo(eOne2);
        eOne2.setId(2L);
        assertThat(eOne1).isNotEqualTo(eOne2);
        eOne1.setId(null);
        assertThat(eOne1).isNotEqualTo(eOne2);
    }
}
