package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterApp;
import com.mycompany.myapp.domain.ESeven;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.ESevenRepository;
import com.mycompany.myapp.service.ESevenService;
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
 * Integration tests for the {@link ESevenResource} REST controller.
 */
@SpringBootTest(classes = JhipsterApp.class)
public class ESevenResourceIT {

    private static final String DEFAULT_SEVEN = "AAAAAAAAAA";
    private static final String UPDATED_SEVEN = "BBBBBBBBBB";

    @Autowired
    private ESevenRepository eSevenRepository;

    @Autowired
    private ESevenService eSevenService;

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

    private MockMvc restESevenMockMvc;

    private ESeven eSeven;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ESevenResource eSevenResource = new ESevenResource(eSevenService);
        this.restESevenMockMvc = MockMvcBuilders.standaloneSetup(eSevenResource)
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
    public static ESeven createEntity(EntityManager em) {
        ESeven eSeven = new ESeven()
            .seven(DEFAULT_SEVEN);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        eSeven.setUser(user);
        return eSeven;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ESeven createUpdatedEntity(EntityManager em) {
        ESeven eSeven = new ESeven()
            .seven(UPDATED_SEVEN);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        eSeven.setUser(user);
        return eSeven;
    }

    @BeforeEach
    public void initTest() {
        eSeven = createEntity(em);
    }

    @Test
    @Transactional
    public void createESeven() throws Exception {
        int databaseSizeBeforeCreate = eSevenRepository.findAll().size();

        // Create the ESeven
        restESevenMockMvc.perform(post("/api/e-sevens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eSeven)))
            .andExpect(status().isCreated());

        // Validate the ESeven in the database
        List<ESeven> eSevenList = eSevenRepository.findAll();
        assertThat(eSevenList).hasSize(databaseSizeBeforeCreate + 1);
        ESeven testESeven = eSevenList.get(eSevenList.size() - 1);
        assertThat(testESeven.getSeven()).isEqualTo(DEFAULT_SEVEN);

        // Validate the id for MapsId, the ids must be same
        assertThat(testESeven.getId()).isEqualTo(testESeven.getUser().getId());
    }

    @Test
    @Transactional
    public void createESevenWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eSevenRepository.findAll().size();

        // Create the ESeven with an existing ID
        eSeven.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restESevenMockMvc.perform(post("/api/e-sevens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eSeven)))
            .andExpect(status().isBadRequest());

        // Validate the ESeven in the database
        List<ESeven> eSevenList = eSevenRepository.findAll();
        assertThat(eSevenList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void updateESevenMapsIdAssociationWithNewId() throws Exception {
        // Initialize the database
        eSevenService.save(eSeven);
        int databaseSizeBeforeCreate = eSevenRepository.findAll().size();

        // Add a new parent entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();

        // Load the eSeven
        ESeven updatedESeven = eSevenRepository.findById(eSeven.getId()).get();
        // Disconnect from session so that the updates on updatedESeven are not directly saved in db
        em.detach(updatedESeven);

        // Update the User with new association value
        updatedESeven.setUser(user);

        // Update the entity
        restESevenMockMvc.perform(put("/api/e-sevens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedESeven)))
            .andExpect(status().isOk());

        // Validate the ESeven in the database
        List<ESeven> eSevenList = eSevenRepository.findAll();
        assertThat(eSevenList).hasSize(databaseSizeBeforeCreate);
        ESeven testESeven = eSevenList.get(eSevenList.size() - 1);

        // Validate the id for MapsId, the ids must be same
        // Uncomment the following line for assertion. However, please note that there is a known issue and uncommenting will fail the test.
        // Please look at https://github.com/jhipster/generator-jhipster/issues/9100. You can modify this test as necessary.
        // assertThat(testESeven.getId()).isEqualTo(testESeven.getUser().getId());
    }

    @Test
    @Transactional
    public void getAllESevens() throws Exception {
        // Initialize the database
        eSevenRepository.saveAndFlush(eSeven);

        // Get all the eSevenList
        restESevenMockMvc.perform(get("/api/e-sevens?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eSeven.getId().intValue())))
            .andExpect(jsonPath("$.[*].seven").value(hasItem(DEFAULT_SEVEN)));
    }
    
    @Test
    @Transactional
    public void getESeven() throws Exception {
        // Initialize the database
        eSevenRepository.saveAndFlush(eSeven);

        // Get the eSeven
        restESevenMockMvc.perform(get("/api/e-sevens/{id}", eSeven.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(eSeven.getId().intValue()))
            .andExpect(jsonPath("$.seven").value(DEFAULT_SEVEN));
    }

    @Test
    @Transactional
    public void getNonExistingESeven() throws Exception {
        // Get the eSeven
        restESevenMockMvc.perform(get("/api/e-sevens/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateESeven() throws Exception {
        // Initialize the database
        eSevenService.save(eSeven);

        int databaseSizeBeforeUpdate = eSevenRepository.findAll().size();

        // Update the eSeven
        ESeven updatedESeven = eSevenRepository.findById(eSeven.getId()).get();
        // Disconnect from session so that the updates on updatedESeven are not directly saved in db
        em.detach(updatedESeven);
        updatedESeven
            .seven(UPDATED_SEVEN);

        restESevenMockMvc.perform(put("/api/e-sevens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedESeven)))
            .andExpect(status().isOk());

        // Validate the ESeven in the database
        List<ESeven> eSevenList = eSevenRepository.findAll();
        assertThat(eSevenList).hasSize(databaseSizeBeforeUpdate);
        ESeven testESeven = eSevenList.get(eSevenList.size() - 1);
        assertThat(testESeven.getSeven()).isEqualTo(UPDATED_SEVEN);
    }

    @Test
    @Transactional
    public void updateNonExistingESeven() throws Exception {
        int databaseSizeBeforeUpdate = eSevenRepository.findAll().size();

        // Create the ESeven

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restESevenMockMvc.perform(put("/api/e-sevens")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(eSeven)))
            .andExpect(status().isBadRequest());

        // Validate the ESeven in the database
        List<ESeven> eSevenList = eSevenRepository.findAll();
        assertThat(eSevenList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteESeven() throws Exception {
        // Initialize the database
        eSevenService.save(eSeven);

        int databaseSizeBeforeDelete = eSevenRepository.findAll().size();

        // Delete the eSeven
        restESevenMockMvc.perform(delete("/api/e-sevens/{id}", eSeven.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ESeven> eSevenList = eSevenRepository.findAll();
        assertThat(eSevenList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ESeven.class);
        ESeven eSeven1 = new ESeven();
        eSeven1.setId(1L);
        ESeven eSeven2 = new ESeven();
        eSeven2.setId(eSeven1.getId());
        assertThat(eSeven1).isEqualTo(eSeven2);
        eSeven2.setId(2L);
        assertThat(eSeven1).isNotEqualTo(eSeven2);
        eSeven1.setId(null);
        assertThat(eSeven1).isNotEqualTo(eSeven2);
    }
}
