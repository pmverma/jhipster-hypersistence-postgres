package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.EOne;
import com.mycompany.myapp.service.EOneService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.EOne}.
 */
@RestController
@RequestMapping("/api")
public class EOneResource {

    private final Logger log = LoggerFactory.getLogger(EOneResource.class);

    private static final String ENTITY_NAME = "eOne";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EOneService eOneService;

    public EOneResource(EOneService eOneService) {
        this.eOneService = eOneService;
    }

    /**
     * {@code POST  /e-ones} : Create a new eOne.
     *
     * @param eOne the eOne to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eOne, or with status {@code 400 (Bad Request)} if the eOne has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/e-ones")
    public ResponseEntity<EOne> createEOne(@Valid @RequestBody EOne eOne) throws URISyntaxException {
        log.debug("REST request to save EOne : {}", eOne);
        if (eOne.getId() != null) {
            throw new BadRequestAlertException("A new eOne cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EOne result = eOneService.save(eOne);
        return ResponseEntity.created(new URI("/api/e-ones/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /e-ones} : Updates an existing eOne.
     *
     * @param eOne the eOne to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eOne,
     * or with status {@code 400 (Bad Request)} if the eOne is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eOne couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/e-ones")
    public ResponseEntity<EOne> updateEOne(@Valid @RequestBody EOne eOne) throws URISyntaxException {
        log.debug("REST request to update EOne : {}", eOne);
        if (eOne.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EOne result = eOneService.save(eOne);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eOne.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /e-ones} : get all the eOnes.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eOnes in body.
     */
    @GetMapping("/e-ones")
    public ResponseEntity<List<EOne>> getAllEOnes(Pageable pageable) {
        log.debug("REST request to get a page of EOnes");
        Page<EOne> page = eOneService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /e-ones/:id} : get the "id" eOne.
     *
     * @param id the id of the eOne to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eOne, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/e-ones/{id}")
    public ResponseEntity<EOne> getEOne(@PathVariable Long id) {
        log.debug("REST request to get EOne : {}", id);
        Optional<EOne> eOne = eOneService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eOne);
    }

    /**
     * {@code DELETE  /e-ones/:id} : delete the "id" eOne.
     *
     * @param id the id of the eOne to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/e-ones/{id}")
    public ResponseEntity<Void> deleteEOne(@PathVariable Long id) {
        log.debug("REST request to delete EOne : {}", id);
        eOneService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
