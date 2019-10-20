package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ESeven;
import com.mycompany.myapp.service.ESevenService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ESeven}.
 */
@RestController
@RequestMapping("/api")
public class ESevenResource {

    private final Logger log = LoggerFactory.getLogger(ESevenResource.class);

    private static final String ENTITY_NAME = "eSeven";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ESevenService eSevenService;

    public ESevenResource(ESevenService eSevenService) {
        this.eSevenService = eSevenService;
    }

    /**
     * {@code POST  /e-sevens} : Create a new eSeven.
     *
     * @param eSeven the eSeven to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eSeven, or with status {@code 400 (Bad Request)} if the eSeven has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/e-sevens")
    public ResponseEntity<ESeven> createESeven(@RequestBody ESeven eSeven) throws URISyntaxException {
        log.debug("REST request to save ESeven : {}", eSeven);
        if (eSeven.getId() != null) {
            throw new BadRequestAlertException("A new eSeven cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (Objects.isNull(eSeven.getUser())) {
            throw new BadRequestAlertException("Invalid association value provided", ENTITY_NAME, "null");
        }
        ESeven result = eSevenService.save(eSeven);
        return ResponseEntity.created(new URI("/api/e-sevens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /e-sevens} : Updates an existing eSeven.
     *
     * @param eSeven the eSeven to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eSeven,
     * or with status {@code 400 (Bad Request)} if the eSeven is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eSeven couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/e-sevens")
    public ResponseEntity<ESeven> updateESeven(@RequestBody ESeven eSeven) throws URISyntaxException {
        log.debug("REST request to update ESeven : {}", eSeven);
        if (eSeven.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ESeven result = eSevenService.save(eSeven);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eSeven.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /e-sevens} : get all the eSevens.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eSevens in body.
     */
    @GetMapping("/e-sevens")
    public List<ESeven> getAllESevens() {
        log.debug("REST request to get all ESevens");
        return eSevenService.findAll();
    }

    /**
     * {@code GET  /e-sevens/:id} : get the "id" eSeven.
     *
     * @param id the id of the eSeven to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eSeven, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/e-sevens/{id}")
    public ResponseEntity<ESeven> getESeven(@PathVariable Long id) {
        log.debug("REST request to get ESeven : {}", id);
        Optional<ESeven> eSeven = eSevenService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eSeven);
    }

    /**
     * {@code DELETE  /e-sevens/:id} : delete the "id" eSeven.
     *
     * @param id the id of the eSeven to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/e-sevens/{id}")
    public ResponseEntity<Void> deleteESeven(@PathVariable Long id) {
        log.debug("REST request to delete ESeven : {}", id);
        eSevenService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
