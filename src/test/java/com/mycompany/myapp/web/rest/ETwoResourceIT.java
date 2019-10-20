package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;
import com.mycompany.myapp.domain.ETwo;
import com.mycompany.myapp.domain.EOne;
import com.mycompany.myapp.repository.ETwoRepository;
import com.mycompany.myapp.service.ETwoService;
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
 * Integration tests for the {@link ETwoResource} REST controller.
 */
@SpringBootTest(classes = JhipsterApp.class)
public class ETwoResourceIT {

    private static final Integer DEFAULT_TWO = 1;
    private static final Integer UPDATED_TWO = 2;

    @Autowired
    private ETwoRepository eTwoRepository;

    @Autowired
    private ETwoService eTwoService;

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

    private MockMvc restETwoMockMvc;

    private ETwo eTwo;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ETwoResource eTwoResource = new ETwoResource(eTwoService);
        this.restETwoMockMvc = MockMvcBuilders.standaloneSetup(eTwoResource)
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
    public static ETwo createEntity(EntityManager em) {
        ETwo eTwo = new ETwo()
            .two(DEFAULT_TWO);
        // Add required entity
        EOne eOne;
        if (TestUtil.findAll(em, EOne.class).isEmpty()) {
            eOne = EOneResourceIT.createEntity(em);
            em.persist(eOne);
            em.flush();
        } else {
            eOne = TestUtil.findAll(em, EOne.class).get(0);
        }
        eTwo.setEOne(eOne);
        return eTwo;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ETwo createUpdatedEntity(EntityManager em) {
        ETwo eTwo = new ETwo()
            .two(UPDATED_TWO);
        // Add required entity
        EOne eOne;
        if (TestUtil.findAll(em, EOne.class).isEmpty()) {
            eOne = EOneResourceIT.createUpdatedEntity(em);
            em.persist(eOne);
            em.flush();
        } else {
            eOne = TestUtil.findAll(em, EOne.class).get(0);
        }
        eTwo.setEOne(eOne);
        return eTwo;
    }

    @BeforeEach
    public void initTest() {
        eTwo = createEntity(em);
    }

    @Test
    @Transactional
    public void createETwo() throws Exception {
        int databaseSizeBeforeCreate = eTwoRepository.findAll().size();

        // Create the ETwo
        restETwoMockMvc.perform(post("/api/e-twos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eTwo)))
            .andExpect(status().isCreated());

        // Validate the ETwo in the database
        List<ETwo> eTwoList = eTwoRepository.findAll();
        assertThat(eTwoList).hasSize(databaseSizeBeforeCreate + 1);
        ETwo testETwo = eTwoList.get(eTwoList.size() - 1);
        assertThat(testETwo.getTwo()).isEqualTo(DEFAULT_TWO);
    }

    @Test
    @Transactional
    public void createETwoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eTwoRepository.findAll().size();

        // Create the ETwo with an existing ID
        eTwo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restETwoMockMvc.perform(post("/api/e-twos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eTwo)))
            .andExpect(status().isBadRequest());

        // Validate the ETwo in the database
        List<ETwo> eTwoList = eTwoRepository.findAll();
        assertThat(eTwoList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTwoIsRequired() throws Exception {
        int databaseSizeBeforeTest = eTwoRepository.findAll().size();
        // set the field null
        eTwo.setTwo(null);

        // Create the ETwo, which fails.

        restETwoMockMvc.perform(post("/api/e-twos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eTwo)))
            .andExpect(status().isBadRequest());

        List<ETwo> eTwoList = eTwoRepository.findAll();
        assertThat(eTwoList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllETwos() throws Exception {
        // Initialize the database
        eTwoRepository.saveAndFlush(eTwo);

        // Get all the eTwoList
        restETwoMockMvc.perform(get("/api/e-twos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eTwo.getId().intValue())))
            .andExpect(jsonPath("$.[*].two").value(hasItem(DEFAULT_TWO)));
    }
    
    @Test
    @Transactional
    public void getETwo() throws Exception {
        // Initialize the database
        eTwoRepository.saveAndFlush(eTwo);

        // Get the eTwo
        restETwoMockMvc.perform(get("/api/e-twos/{id}", eTwo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(eTwo.getId().intValue()))
            .andExpect(jsonPath("$.two").value(DEFAULT_TWO));
    }

    @Test
    @Transactional
    public void getNonExistingETwo() throws Exception {
        // Get the eTwo
        restETwoMockMvc.perform(get("/api/e-twos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateETwo() throws Exception {
        // Initialize the database
        eTwoService.save(eTwo);

        int databaseSizeBeforeUpdate = eTwoRepository.findAll().size();

        // Update the eTwo
        ETwo updatedETwo = eTwoRepository.findById(eTwo.getId()).get();
        // Disconnect from session so that the updates on updatedETwo are not directly saved in db
        em.detach(updatedETwo);
        updatedETwo
            .two(UPDATED_TWO);

        restETwoMockMvc.perform(put("/api/e-twos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedETwo)))
            .andExpect(status().isOk());

        // Validate the ETwo in the database
        List<ETwo> eTwoList = eTwoRepository.findAll();
        assertThat(eTwoList).hasSize(databaseSizeBeforeUpdate);
        ETwo testETwo = eTwoList.get(eTwoList.size() - 1);
        assertThat(testETwo.getTwo()).isEqualTo(UPDATED_TWO);
    }

    @Test
    @Transactional
    public void updateNonExistingETwo() throws Exception {
        int databaseSizeBeforeUpdate = eTwoRepository.findAll().size();

        // Create the ETwo

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restETwoMockMvc.perform(put("/api/e-twos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eTwo)))
            .andExpect(status().isBadRequest());

        // Validate the ETwo in the database
        List<ETwo> eTwoList = eTwoRepository.findAll();
        assertThat(eTwoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteETwo() throws Exception {
        // Initialize the database
        eTwoService.save(eTwo);

        int databaseSizeBeforeDelete = eTwoRepository.findAll().size();

        // Delete the eTwo
        restETwoMockMvc.perform(delete("/api/e-twos/{id}", eTwo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ETwo> eTwoList = eTwoRepository.findAll();
        assertThat(eTwoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ETwo.class);
        ETwo eTwo1 = new ETwo();
        eTwo1.setId(1L);
        ETwo eTwo2 = new ETwo();
        eTwo2.setId(eTwo1.getId());
        assertThat(eTwo1).isEqualTo(eTwo2);
        eTwo2.setId(2L);
        assertThat(eTwo1).isNotEqualTo(eTwo2);
        eTwo1.setId(null);
        assertThat(eTwo1).isNotEqualTo(eTwo2);
    }
}
