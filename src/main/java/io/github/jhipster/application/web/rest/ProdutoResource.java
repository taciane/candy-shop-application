package io.github.jhipster.application.web.rest;

import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.application.domain.Produto;
import io.github.jhipster.application.repository.ProdutoRepository;
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
 * REST controller for managing Produto.
 */
@RestController
@RequestMapping("/api")
public class ProdutoResource {

    private final Logger log = LoggerFactory.getLogger(ProdutoResource.class);

    private static final String ENTITY_NAME = "candyShopProduto";

    private ProdutoRepository produtoRepository;

    public ProdutoResource(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    /**
     * POST  /produtos : Create a new produto.
     *
     * @param produto the produto to create
     * @return the ResponseEntity with status 201 (Created) and with body the new produto, or with status 400 (Bad Request) if the produto has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/produtos")
    @Timed
    public ResponseEntity<Produto> createProduto(@RequestBody Produto produto) throws URISyntaxException {
        log.debug("REST request to save Produto : {}", produto);
        if (produto.getId() != null) {
            throw new BadRequestAlertException("A new produto cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Produto result = produtoRepository.save(produto);
        return ResponseEntity.created(new URI("/api/produtos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /produtos : Updates an existing produto.
     *
     * @param produto the produto to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated produto,
     * or with status 400 (Bad Request) if the produto is not valid,
     * or with status 500 (Internal Server Error) if the produto couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/produtos")
    @Timed
    public ResponseEntity<Produto> updateProduto(@RequestBody Produto produto) throws URISyntaxException {
        log.debug("REST request to update Produto : {}", produto);
        if (produto.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Produto result = produtoRepository.save(produto);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, produto.getId().toString()))
            .body(result);
    }

    /**
     * GET  /produtos : get all the produtos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of produtos in body
     */
    @GetMapping("/produtos")
    @Timed
    public List<Produto> getAllProdutos() {
        log.debug("REST request to get all Produtos");
        return produtoRepository.findAll();
    }

    /**
     * GET  /produtos/:id : get the "id" produto.
     *
     * @param id the id of the produto to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the produto, or with status 404 (Not Found)
     */
    @GetMapping("/produtos/{id}")
    @Timed
    public ResponseEntity<Produto> getProduto(@PathVariable Long id) {
        log.debug("REST request to get Produto : {}", id);
        Optional<Produto> produto = produtoRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(produto);
    }

    /**
     * DELETE  /produtos/:id : delete the "id" produto.
     *
     * @param id the id of the produto to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/produtos/{id}")
    @Timed
    public ResponseEntity<Void> deleteProduto(@PathVariable Long id) {
        log.debug("REST request to delete Produto : {}", id);

        produtoRepository.deleteById(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
