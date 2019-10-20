package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;
import com.mycompany.myapp.domain.EThree;
import com.mycompany.myapp.repository.EThreeRepository;
import com.mycompany.myapp.service.EThreeService;
import com.mycompany.myapp.service.dto.EThreeDTO;
import com.mycompany.myapp.service.mapper.EThreeMapper;
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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EThreeResource} REST controller.
 */
@SpringBootTest(classes = JhipsterApp.class)
public class EThreeResourceIT {

    private static final Instant DEFAULT_THREE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_THREE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private EThreeRepository eThreeRepository;

    @Autowired
    private EThreeMapper eThreeMapper;

    @Autowired
    private EThreeService eThreeService;

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

    private MockMvc restEThreeMockMvc;

    private EThree eThree;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EThreeResource eThreeResource = new EThreeResource(eThreeService);
        this.restEThreeMockMvc = MockMvcBuilders.standaloneSetup(eThreeResource)
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
    public static EThree createEntity(EntityManager em) {
        EThree eThree = new EThree()
            .three(DEFAULT_THREE);
        return eThree;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EThree createUpdatedEntity(EntityManager em) {
        EThree eThree = new EThree()
            .three(UPDATED_THREE);
        return eThree;
    }

    @BeforeEach
    public void initTest() {
        eThree = createEntity(em);
    }

    @Test
    @Transactional
    public void createEThree() throws Exception {
        int databaseSizeBeforeCreate = eThreeRepository.findAll().size();

        // Create the EThree
        EThreeDTO eThreeDTO = eThreeMapper.toDto(eThree);
        restEThreeMockMvc.perform(post("/api/e-threes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eThreeDTO)))
            .andExpect(status().isCreated());

        // Validate the EThree in the database
        List<EThree> eThreeList = eThreeRepository.findAll();
        assertThat(eThreeList).hasSize(databaseSizeBeforeCreate + 1);
        EThree testEThree = eThreeList.get(eThreeList.size() - 1);
        assertThat(testEThree.getThree()).isEqualTo(DEFAULT_THREE);
    }

    @Test
    @Transactional
    public void createEThreeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eThreeRepository.findAll().size();

        // Create the EThree with an existing ID
        eThree.setId(1L);
        EThreeDTO eThreeDTO = eThreeMapper.toDto(eThree);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEThreeMockMvc.perform(post("/api/e-threes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eThreeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EThree in the database
        List<EThree> eThreeList = eThreeRepository.findAll();
        assertThat(eThreeList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkThreeIsRequired() throws Exception {
        int databaseSizeBeforeTest = eThreeRepository.findAll().size();
        // set the field null
        eThree.setThree(null);

        // Create the EThree, which fails.
        EThreeDTO eThreeDTO = eThreeMapper.toDto(eThree);

        restEThreeMockMvc.perform(post("/api/e-threes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eThreeDTO)))
            .andExpect(status().isBadRequest());

        List<EThree> eThreeList = eThreeRepository.findAll();
        assertThat(eThreeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEThrees() throws Exception {
        // Initialize the database
        eThreeRepository.saveAndFlush(eThree);

        // Get all the eThreeList
        restEThreeMockMvc.perform(get("/api/e-threes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eThree.getId().intValue())))
            .andExpect(jsonPath("$.[*].three").value(hasItem(DEFAULT_THREE.toString())));
    }
    
    @Test
    @Transactional
    public void getEThree() throws Exception {
        // Initialize the database
        eThreeRepository.saveAndFlush(eThree);

        // Get the eThree
        restEThreeMockMvc.perform(get("/api/e-threes/{id}", eThree.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(eThree.getId().intValue()))
            .andExpect(jsonPath("$.three").value(DEFAULT_THREE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEThree() throws Exception {
        // Get the eThree
        restEThreeMockMvc.perform(get("/api/e-threes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEThree() throws Exception {
        // Initialize the database
        eThreeRepository.saveAndFlush(eThree);

        int databaseSizeBeforeUpdate = eThreeRepository.findAll().size();

        // Update the eThree
        EThree updatedEThree = eThreeRepository.findById(eThree.getId()).get();
        // Disconnect from session so that the updates on updatedEThree are not directly saved in db
        em.detach(updatedEThree);
        updatedEThree
            .three(UPDATED_THREE);
        EThreeDTO eThreeDTO = eThreeMapper.toDto(updatedEThree);

        restEThreeMockMvc.perform(put("/api/e-threes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eThreeDTO)))
            .andExpect(status().isOk());

        // Validate the EThree in the database
        List<EThree> eThreeList = eThreeRepository.findAll();
        assertThat(eThreeList).hasSize(databaseSizeBeforeUpdate);
        EThree testEThree = eThreeList.get(eThreeList.size() - 1);
        assertThat(testEThree.getThree()).isEqualTo(UPDATED_THREE);
    }

    @Test
    @Transactional
    public void updateNonExistingEThree() throws Exception {
        int databaseSizeBeforeUpdate = eThreeRepository.findAll().size();

        // Create the EThree
        EThreeDTO eThreeDTO = eThreeMapper.toDto(eThree);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEThreeMockMvc.perform(put("/api/e-threes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eThreeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EThree in the database
        List<EThree> eThreeList = eThreeRepository.findAll();
        assertThat(eThreeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEThree() throws Exception {
        // Initialize the database
        eThreeRepository.saveAndFlush(eThree);

        int databaseSizeBeforeDelete = eThreeRepository.findAll().size();

        // Delete the eThree
        restEThreeMockMvc.perform(delete("/api/e-threes/{id}", eThree.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EThree> eThreeList = eThreeRepository.findAll();
        assertThat(eThreeList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EThree.class);
        EThree eThree1 = new EThree();
        eThree1.setId(1L);
        EThree eThree2 = new EThree();
        eThree2.setId(eThree1.getId());
        assertThat(eThree1).isEqualTo(eThree2);
        eThree2.setId(2L);
        assertThat(eThree1).isNotEqualTo(eThree2);
        eThree1.setId(null);
        assertThat(eThree1).isNotEqualTo(eThree2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EThreeDTO.class);
        EThreeDTO eThreeDTO1 = new EThreeDTO();
        eThreeDTO1.setId(1L);
        EThreeDTO eThreeDTO2 = new EThreeDTO();
        assertThat(eThreeDTO1).isNotEqualTo(eThreeDTO2);
        eThreeDTO2.setId(eThreeDTO1.getId());
        assertThat(eThreeDTO1).isEqualTo(eThreeDTO2);
        eThreeDTO2.setId(2L);
        assertThat(eThreeDTO1).isNotEqualTo(eThreeDTO2);
        eThreeDTO1.setId(null);
        assertThat(eThreeDTO1).isNotEqualTo(eThreeDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(eThreeMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(eThreeMapper.fromId(null)).isNull();
    }
}
