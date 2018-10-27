package io.github.jhipster.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.application.domain.Categoria;
import io.github.jhipster.application.repository.CategoriaRepository;
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
 * REST controller for managing Categoria.
 */
@RestController
@RequestMapping("/api")
public class CategoriaResource {

    private final Logger log = LoggerFactory.getLogger(CategoriaResource.class);

    private static final String ENTITY_NAME = "candyShopCategoria";

    private CategoriaRepository categoriaRepository;

    public CategoriaResource(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    /**
     * POST  /categorias : Create a new categoria.
     *
     * @param categoria the categoria to create
     * @return the ResponseEntity with status 201 (Created) and with body the new categoria, or with status 400 (Bad Request) if the categoria has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/categorias")
    @Timed
    public ResponseEntity<Categoria> createCategoria(@RequestBody Categoria categoria) throws URISyntaxException {
        log.debug("REST request to save Categoria : {}", categoria);
        if (categoria.getId() != null) {
            throw new BadRequestAlertException("A new categoria cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Categoria result = categoriaRepository.save(categoria);
        return ResponseEntity.created(new URI("/api/categorias/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /categorias : Updates an existing categoria.
     *
     * @param categoria the categoria to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated categoria,
     * or with status 400 (Bad Request) if the categoria is not valid,
     * or with status 500 (Internal Server Error) if the categoria couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/categorias")
    @Timed
    public ResponseEntity<Categoria> updateCategoria(@RequestBody Categoria categoria) throws URISyntaxException {
        log.debug("REST request to update Categoria : {}", categoria);
        if (categoria.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Categoria result = categoriaRepository.save(categoria);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, categoria.getId().toString()))
            .body(result);
    }

    /**
     * GET  /categorias : get all the categorias.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of categorias in body
     */
    @GetMapping("/categorias")
    @Timed
    public List<Categoria> getAllCategorias() {
        log.debug("REST request to get all Categorias");
        return categoriaRepository.findAll();
    }

    /**
     * GET  /categorias/:id : get the "id" categoria.
     *
     * @param id the id of the categoria to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the categoria, or with status 404 (Not Found)
     */
    @GetMapping("/categorias/{id}")
    @Timed
    public ResponseEntity<Categoria> getCategoria(@PathVariable Long id) {
        log.debug("REST request to get Categoria : {}", id);
        Optional<Categoria> categoria = categoriaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(categoria);
    }

    /**
     * DELETE  /categorias/:id : delete the "id" categoria.
     *
     * @param id the id of the categoria to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/categorias/{id}")
    @Timed
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id) {
        log.debug("REST request to delete Categoria : {}", id);

        categoriaRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
