package io.github.jhipster.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.application.domain.Ponto;
import io.github.jhipster.application.repository.PontoRepository;
import io.github.jhipster.application.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.application.web.rest.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Ponto.
 */
@RestController
@RequestMapping("/api")
public class PontoResource {

    private final Logger log = LoggerFactory.getLogger(PontoResource.class);

    private static final String ENTITY_NAME = "candyShopPonto";

    private PontoRepository pontoRepository;

    public PontoResource(PontoRepository pontoRepository) {
        this.pontoRepository = pontoRepository;
    }

    /**
     * POST  /pontos : Create a new ponto.
     *
     * @param ponto the ponto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ponto, or with status 400 (Bad Request) if the ponto has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/pontos")
    @Timed
    public ResponseEntity<Ponto> createPonto(@RequestBody Ponto ponto) throws URISyntaxException {
        log.debug("REST request to save Ponto : {}", ponto);
        if (ponto.getId() != null) {
            throw new BadRequestAlertException("A new ponto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ponto result = pontoRepository.save(ponto);
        return ResponseEntity.created(new URI("/api/pontos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /pontos : Updates an existing ponto.
     *
     * @param ponto the ponto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ponto,
     * or with status 400 (Bad Request) if the ponto is not valid,
     * or with status 500 (Internal Server Error) if the ponto couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/pontos")
    @Timed
    public ResponseEntity<Ponto> updatePonto(@RequestBody Ponto ponto) throws URISyntaxException {
        log.debug("REST request to update Ponto : {}", ponto);
        if (ponto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Ponto result = pontoRepository.save(ponto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, ponto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /pontos : get all the pontos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of pontos in body
     */
    @GetMapping("/pontos")
    @Timed
    public List<Ponto> getAllPontos() {
        log.debug("REST request to get all Pontos");
        return pontoRepository.findAll();
    }

    /**
     * GET  /pontos/:id : get the "id" ponto.
     *
     * @param id the id of the ponto to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ponto, or with status 404 (Not Found)
     */
    @GetMapping("/pontos/{id}")
    @Timed
    public ResponseEntity<Ponto> getPonto(@PathVariable Long id) {
        log.debug("REST request to get Ponto : {}", id);
        Optional<Ponto> ponto = pontoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ponto);
    }

    /**
     * DELETE  /pontos/:id : delete the "id" ponto.
     *
     * @param id the id of the ponto to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/pontos/{id}")
    @Timed
    public ResponseEntity<Void> deletePonto(@PathVariable Long id) {
        log.debug("REST request to delete Ponto : {}", id);

        pontoRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
