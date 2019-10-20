package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;
import com.mycompany.myapp.domain.ESix;
import com.mycompany.myapp.repository.ESixRepository;
import com.mycompany.myapp.service.ESixService;
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
 * Integration tests for the {@link ESixResource} REST controller.
 */
@SpringBootTest(classes = JhipsterApp.class)
public class ESixResourceIT {

    private static final String DEFAULT_SIX = "AAAAAAAAAA";
    private static final String UPDATED_SIX = "BBBBBBBBBB";

    @Autowired
    private ESixRepository eSixRepository;

    @Autowired
    private ESixService eSixService;

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

    private MockMvc restESixMockMvc;

    private ESix eSix;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ESixResource eSixResource = new ESixResource(eSixService);
        this.restESixMockMvc = MockMvcBuilders.standaloneSetup(eSixResource)
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
    public static ESix createEntity(EntityManager em) {
        ESix eSix = new ESix()
            .six(DEFAULT_SIX);
        return eSix;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ESix createUpdatedEntity(EntityManager em) {
        ESix eSix = new ESix()
            .six(UPDATED_SIX);
        return eSix;
    }

    @BeforeEach
    public void initTest() {
        eSix = createEntity(em);
    }

    @Test
    @Transactional
    public void createESix() throws Exception {
        int databaseSizeBeforeCreate = eSixRepository.findAll().size();

        // Create the ESix
        restESixMockMvc.perform(post("/api/e-sixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eSix)))
            .andExpect(status().isCreated());

        // Validate the ESix in the database
        List<ESix> eSixList = eSixRepository.findAll();
        assertThat(eSixList).hasSize(databaseSizeBeforeCreate + 1);
        ESix testESix = eSixList.get(eSixList.size() - 1);
        assertThat(testESix.getSix()).isEqualTo(DEFAULT_SIX);
    }

    @Test
    @Transactional
    public void createESixWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eSixRepository.findAll().size();

        // Create the ESix with an existing ID
        eSix.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restESixMockMvc.perform(post("/api/e-sixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eSix)))
            .andExpect(status().isBadRequest());

        // Validate the ESix in the database
        List<ESix> eSixList = eSixRepository.findAll();
        assertThat(eSixList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkSixIsRequired() throws Exception {
        int databaseSizeBeforeTest = eSixRepository.findAll().size();
        // set the field null
        eSix.setSix(null);

        // Create the ESix, which fails.

        restESixMockMvc.perform(post("/api/e-sixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eSix)))
            .andExpect(status().isBadRequest());

        List<ESix> eSixList = eSixRepository.findAll();
        assertThat(eSixList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllESixes() throws Exception {
        // Initialize the database
        eSixRepository.saveAndFlush(eSix);

        // Get all the eSixList
        restESixMockMvc.perform(get("/api/e-sixes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eSix.getId().intValue())))
            .andExpect(jsonPath("$.[*].six").value(hasItem(DEFAULT_SIX)));
    }
    
    @Test
    @Transactional
    public void getESix() throws Exception {
        // Initialize the database
        eSixRepository.saveAndFlush(eSix);

        // Get the eSix
        restESixMockMvc.perform(get("/api/e-sixes/{id}", eSix.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(eSix.getId().intValue()))
            .andExpect(jsonPath("$.six").value(DEFAULT_SIX));
    }

    @Test
    @Transactional
    public void getNonExistingESix() throws Exception {
        // Get the eSix
        restESixMockMvc.perform(get("/api/e-sixes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateESix() throws Exception {
        // Initialize the database
        eSixService.save(eSix);

        int databaseSizeBeforeUpdate = eSixRepository.findAll().size();

        // Update the eSix
        ESix updatedESix = eSixRepository.findById(eSix.getId()).get();
        // Disconnect from session so that the updates on updatedESix are not directly saved in db
        em.detach(updatedESix);
        updatedESix
            .six(UPDATED_SIX);

        restESixMockMvc.perform(put("/api/e-sixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedESix)))
            .andExpect(status().isOk());

        // Validate the ESix in the database
        List<ESix> eSixList = eSixRepository.findAll();
        assertThat(eSixList).hasSize(databaseSizeBeforeUpdate);
        ESix testESix = eSixList.get(eSixList.size() - 1);
        assertThat(testESix.getSix()).isEqualTo(UPDATED_SIX);
    }

    @Test
    @Transactional
    public void updateNonExistingESix() throws Exception {
        int databaseSizeBeforeUpdate = eSixRepository.findAll().size();

        // Create the ESix

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restESixMockMvc.perform(put("/api/e-sixes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eSix)))
            .andExpect(status().isBadRequest());

        // Validate the ESix in the database
        List<ESix> eSixList = eSixRepository.findAll();
        assertThat(eSixList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteESix() throws Exception {
        // Initialize the database
        eSixService.save(eSix);

        int databaseSizeBeforeDelete = eSixRepository.findAll().size();

        // Delete the eSix
        restESixMockMvc.perform(delete("/api/e-sixes/{id}", eSix.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ESix> eSixList = eSixRepository.findAll();
        assertThat(eSixList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ESix.class);
        ESix eSix1 = new ESix();
        eSix1.setId(1L);
        ESix eSix2 = new ESix();
        eSix2.setId(eSix1.getId());
        assertThat(eSix1).isEqualTo(eSix2);
        eSix2.setId(2L);
        assertThat(eSix1).isNotEqualTo(eSix2);
        eSix1.setId(null);
        assertThat(eSix1).isNotEqualTo(eSix2);
    }
}
