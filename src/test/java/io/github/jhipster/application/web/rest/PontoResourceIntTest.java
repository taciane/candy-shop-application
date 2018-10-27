package io.github.jhipster.application.web.rest;

import io.github.jhipster.application.CandyShopApp;

import io.github.jhipster.application.domain.Ponto;
import io.github.jhipster.application.repository.PontoRepository;
import io.github.jhipster.application.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;


import static io.github.jhipster.application.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PontoResource REST controller.
 *
 * @see PontoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CandyShopApp.class)
public class PontoResourceIntTest {

    private static final String DEFAULT_QUANTIDADE = "AAAAAAAAAA";
    private static final String UPDATED_QUANTIDADE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATA = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private PontoRepository pontoRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPontoMockMvc;

    private Ponto ponto;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PontoResource pontoResource = new PontoResource(pontoRepository);
        this.restPontoMockMvc = MockMvcBuilders.standaloneSetup(pontoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ponto createEntity(EntityManager em) {
        Ponto ponto = new Ponto()
            .quantidade(DEFAULT_QUANTIDADE)
            .data(DEFAULT_DATA);
        return ponto;
    }

    @Before
    public void initTest() {
        ponto = createEntity(em);
    }

    @Test
    @Transactional
    public void createPonto() throws Exception {
        int databaseSizeBeforeCreate = pontoRepository.findAll().size();

        // Create the Ponto
        restPontoMockMvc.perform(post("/api/pontos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ponto)))
            .andExpect(status().isCreated());

        // Validate the Ponto in the database
        List<Ponto> pontoList = pontoRepository.findAll();
        assertThat(pontoList).hasSize(databaseSizeBeforeCreate + 1);
        Ponto testPonto = pontoList.get(pontoList.size() - 1);
        assertThat(testPonto.getQuantidade()).isEqualTo(DEFAULT_QUANTIDADE);
        assertThat(testPonto.getData()).isEqualTo(DEFAULT_DATA);
    }

    @Test
    @Transactional
    public void createPontoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pontoRepository.findAll().size();

        // Create the Ponto with an existing ID
        ponto.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPontoMockMvc.perform(post("/api/pontos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ponto)))
            .andExpect(status().isBadRequest());

        // Validate the Ponto in the database
        List<Ponto> pontoList = pontoRepository.findAll();
        assertThat(pontoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPontos() throws Exception {
        // Initialize the database
        pontoRepository.saveAndFlush(ponto);

        // Get all the pontoList
        restPontoMockMvc.perform(get("/api/pontos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ponto.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantidade").value(hasItem(DEFAULT_QUANTIDADE.toString())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(DEFAULT_DATA.toString())));
    }
    
    @Test
    @Transactional
    public void getPonto() throws Exception {
        // Initialize the database
        pontoRepository.saveAndFlush(ponto);

        // Get the ponto
        restPontoMockMvc.perform(get("/api/pontos/{id}", ponto.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ponto.getId().intValue()))
            .andExpect(jsonPath("$.quantidade").value(DEFAULT_QUANTIDADE.toString()))
            .andExpect(jsonPath("$.data").value(DEFAULT_DATA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPonto() throws Exception {
        // Get the ponto
        restPontoMockMvc.perform(get("/api/pontos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePonto() throws Exception {
        // Initialize the database
        pontoRepository.saveAndFlush(ponto);

        int databaseSizeBeforeUpdate = pontoRepository.findAll().size();

        // Update the ponto
        Ponto updatedPonto = pontoRepository.findById(ponto.getId()).get();
        // Disconnect from session so that the updates on updatedPonto are not directly saved in db
        em.detach(updatedPonto);
        updatedPonto
            .quantidade(UPDATED_QUANTIDADE)
            .data(UPDATED_DATA);

        restPontoMockMvc.perform(put("/api/pontos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPonto)))
            .andExpect(status().isOk());

        // Validate the Ponto in the database
        List<Ponto> pontoList = pontoRepository.findAll();
        assertThat(pontoList).hasSize(databaseSizeBeforeUpdate);
        Ponto testPonto = pontoList.get(pontoList.size() - 1);
        assertThat(testPonto.getQuantidade()).isEqualTo(UPDATED_QUANTIDADE);
        assertThat(testPonto.getData()).isEqualTo(UPDATED_DATA);
    }

    @Test
    @Transactional
    public void updateNonExistingPonto() throws Exception {
        int databaseSizeBeforeUpdate = pontoRepository.findAll().size();

        // Create the Ponto

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPontoMockMvc.perform(put("/api/pontos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ponto)))
            .andExpect(status().isBadRequest());

        // Validate the Ponto in the database
        List<Ponto> pontoList = pontoRepository.findAll();
        assertThat(pontoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePonto() throws Exception {
        // Initialize the database
        pontoRepository.saveAndFlush(ponto);

        int databaseSizeBeforeDelete = pontoRepository.findAll().size();

        // Get the ponto
        restPontoMockMvc.perform(delete("/api/pontos/{id}", ponto.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Ponto> pontoList = pontoRepository.findAll();
        assertThat(pontoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Ponto.class);
        Ponto ponto1 = new Ponto();
        ponto1.setId(1L);
        Ponto ponto2 = new Ponto();
        ponto2.setId(ponto1.getId());
        assertThat(ponto1).isEqualTo(ponto2);
        ponto2.setId(2L);
        assertThat(ponto1).isNotEqualTo(ponto2);
        ponto1.setId(null);
        assertThat(ponto1).isNotEqualTo(ponto2);
    }
}
