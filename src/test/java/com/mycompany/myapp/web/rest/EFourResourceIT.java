package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;
import com.mycompany.myapp.domain.EFour;
import com.mycompany.myapp.repository.EFourRepository;
import com.mycompany.myapp.service.EFourService;
import com.mycompany.myapp.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EFourResource} REST controller.
 */
@SpringBootTest(classes = JhipsterApp.class)
public class EFourResourceIT {

    private static final byte[] DEFAULT_FOUR = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_FOUR = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_FOUR_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_FOUR_CONTENT_TYPE = "image/png";

    @Autowired
    private EFourRepository eFourRepository;

    @Mock
    private EFourRepository eFourRepositoryMock;

    @Mock
    private EFourService eFourServiceMock;

    @Autowired
    private EFourService eFourService;

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

    private MockMvc restEFourMockMvc;

    private EFour eFour;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final EFourResource eFourResource = new EFourResource(eFourService);
        this.restEFourMockMvc = MockMvcBuilders.standaloneSetup(eFourResource)
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
    public static EFour createEntity(EntityManager em) {
        EFour eFour = new EFour()
            .four(DEFAULT_FOUR)
            .fourContentType(DEFAULT_FOUR_CONTENT_TYPE);
        return eFour;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EFour createUpdatedEntity(EntityManager em) {
        EFour eFour = new EFour()
            .four(UPDATED_FOUR)
            .fourContentType(UPDATED_FOUR_CONTENT_TYPE);
        return eFour;
    }

    @BeforeEach
    public void initTest() {
        eFour = createEntity(em);
    }

    @Test
    @Transactional
    public void createEFour() throws Exception {
        int databaseSizeBeforeCreate = eFourRepository.findAll().size();

        // Create the EFour
        restEFourMockMvc.perform(post("/api/e-fours")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eFour)))
            .andExpect(status().isCreated());

        // Validate the EFour in the database
        List<EFour> eFourList = eFourRepository.findAll();
        assertThat(eFourList).hasSize(databaseSizeBeforeCreate + 1);
        EFour testEFour = eFourList.get(eFourList.size() - 1);
        assertThat(testEFour.getFour()).isEqualTo(DEFAULT_FOUR);
        assertThat(testEFour.getFourContentType()).isEqualTo(DEFAULT_FOUR_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createEFourWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eFourRepository.findAll().size();

        // Create the EFour with an existing ID
        eFour.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEFourMockMvc.perform(post("/api/e-fours")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eFour)))
            .andExpect(status().isBadRequest());

        // Validate the EFour in the database
        List<EFour> eFourList = eFourRepository.findAll();
        assertThat(eFourList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllEFours() throws Exception {
        // Initialize the database
        eFourRepository.saveAndFlush(eFour);

        // Get all the eFourList
        restEFourMockMvc.perform(get("/api/e-fours?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eFour.getId().intValue())))
            .andExpect(jsonPath("$.[*].fourContentType").value(hasItem(DEFAULT_FOUR_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].four").value(hasItem(Base64Utils.encodeToString(DEFAULT_FOUR))));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllEFoursWithEagerRelationshipsIsEnabled() throws Exception {
        EFourResource eFourResource = new EFourResource(eFourServiceMock);
        when(eFourServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restEFourMockMvc = MockMvcBuilders.standaloneSetup(eFourResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restEFourMockMvc.perform(get("/api/e-fours?eagerload=true"))
        .andExpect(status().isOk());

        verify(eFourServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllEFoursWithEagerRelationshipsIsNotEnabled() throws Exception {
        EFourResource eFourResource = new EFourResource(eFourServiceMock);
            when(eFourServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restEFourMockMvc = MockMvcBuilders.standaloneSetup(eFourResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restEFourMockMvc.perform(get("/api/e-fours?eagerload=true"))
        .andExpect(status().isOk());

            verify(eFourServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getEFour() throws Exception {
        // Initialize the database
        eFourRepository.saveAndFlush(eFour);

        // Get the eFour
        restEFourMockMvc.perform(get("/api/e-fours/{id}", eFour.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(eFour.getId().intValue()))
            .andExpect(jsonPath("$.fourContentType").value(DEFAULT_FOUR_CONTENT_TYPE))
            .andExpect(jsonPath("$.four").value(Base64Utils.encodeToString(DEFAULT_FOUR)));
    }

    @Test
    @Transactional
    public void getNonExistingEFour() throws Exception {
        // Get the eFour
        restEFourMockMvc.perform(get("/api/e-fours/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEFour() throws Exception {
        // Initialize the database
        eFourService.save(eFour);

        int databaseSizeBeforeUpdate = eFourRepository.findAll().size();

        // Update the eFour
        EFour updatedEFour = eFourRepository.findById(eFour.getId()).get();
        // Disconnect from session so that the updates on updatedEFour are not directly saved in db
        em.detach(updatedEFour);
        updatedEFour
            .four(UPDATED_FOUR)
            .fourContentType(UPDATED_FOUR_CONTENT_TYPE);

        restEFourMockMvc.perform(put("/api/e-fours")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEFour)))
            .andExpect(status().isOk());

        // Validate the EFour in the database
        List<EFour> eFourList = eFourRepository.findAll();
        assertThat(eFourList).hasSize(databaseSizeBeforeUpdate);
        EFour testEFour = eFourList.get(eFourList.size() - 1);
        assertThat(testEFour.getFour()).isEqualTo(UPDATED_FOUR);
        assertThat(testEFour.getFourContentType()).isEqualTo(UPDATED_FOUR_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingEFour() throws Exception {
        int databaseSizeBeforeUpdate = eFourRepository.findAll().size();

        // Create the EFour

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEFourMockMvc.perform(put("/api/e-fours")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eFour)))
            .andExpect(status().isBadRequest());

        // Validate the EFour in the database
        List<EFour> eFourList = eFourRepository.findAll();
        assertThat(eFourList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEFour() throws Exception {
        // Initialize the database
        eFourService.save(eFour);

        int databaseSizeBeforeDelete = eFourRepository.findAll().size();

        // Delete the eFour
        restEFourMockMvc.perform(delete("/api/e-fours/{id}", eFour.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EFour> eFourList = eFourRepository.findAll();
        assertThat(eFourList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EFour.class);
        EFour eFour1 = new EFour();
        eFour1.setId(1L);
        EFour eFour2 = new EFour();
        eFour2.setId(eFour1.getId());
        assertThat(eFour1).isEqualTo(eFour2);
        eFour2.setId(2L);
        assertThat(eFour1).isNotEqualTo(eFour2);
        eFour1.setId(null);
        assertThat(eFour1).isNotEqualTo(eFour2);
    }
}
