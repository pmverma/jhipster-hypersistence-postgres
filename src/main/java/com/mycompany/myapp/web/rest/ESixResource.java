package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ESix;
import com.mycompany.myapp.service.ESixService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.ESix}.
 */
@RestController
@RequestMapping("/api")
public class ESixResource {

    private final Logger log = LoggerFactory.getLogger(ESixResource.class);

    private static final String ENTITY_NAME = "eSix";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ESixService eSixService;

    public ESixResource(ESixService eSixService) {
        this.eSixService = eSixService;
    }

    /**
     * {@code POST  /e-sixes} : Create a new eSix.
     *
     * @param eSix the eSix to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eSix, or with status {@code 400 (Bad Request)} if the eSix has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/e-sixes")
    public ResponseEntity<ESix> createESix(@Valid @RequestBody ESix eSix) throws URISyntaxException {
        log.debug("REST request to save ESix : {}", eSix);
        if (eSix.getId() != null) {
            throw new BadRequestAlertException("A new eSix cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ESix result = eSixService.save(eSix);
        return ResponseEntity.created(new URI("/api/e-sixes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /e-sixes} : Updates an existing eSix.
     *
     * @param eSix the eSix to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eSix,
     * or with status {@code 400 (Bad Request)} if the eSix is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eSix couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/e-sixes")
    public ResponseEntity<ESix> updateESix(@Valid @RequestBody ESix eSix) throws URISyntaxException {
        log.debug("REST request to update ESix : {}", eSix);
        if (eSix.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ESix result = eSixService.save(eSix);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eSix.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /e-sixes} : get all the eSixes.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eSixes in body.
     */
    @GetMapping("/e-sixes")
    public List<ESix> getAllESixes() {
        log.debug("REST request to get all ESixes");
        return eSixService.findAll();
    }

    /**
     * {@code GET  /e-sixes/:id} : get the "id" eSix.
     *
     * @param id the id of the eSix to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eSix, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/e-sixes/{id}")
    public ResponseEntity<ESix> getESix(@PathVariable Long id) {
        log.debug("REST request to get ESix : {}", id);
        Optional<ESix> eSix = eSixService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eSix);
    }

    /**
     * {@code DELETE  /e-sixes/:id} : delete the "id" eSix.
     *
     * @param id the id of the eSix to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/e-sixes/{id}")
    public ResponseEntity<Void> deleteESix(@PathVariable Long id) {
        log.debug("REST request to delete ESix : {}", id);
        eSixService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
