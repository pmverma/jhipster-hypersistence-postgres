package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.ETwo;
import com.mycompany.myapp.service.ETwoService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ETwo}.
 */
@RestController
@RequestMapping("/api")
public class ETwoResource {

    private final Logger log = LoggerFactory.getLogger(ETwoResource.class);

    private static final String ENTITY_NAME = "eTwo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ETwoService eTwoService;

    public ETwoResource(ETwoService eTwoService) {
        this.eTwoService = eTwoService;
    }

    /**
     * {@code POST  /e-twos} : Create a new eTwo.
     *
     * @param eTwo the eTwo to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eTwo, or with status {@code 400 (Bad Request)} if the eTwo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/e-twos")
    public ResponseEntity<ETwo> createETwo(@Valid @RequestBody ETwo eTwo) throws URISyntaxException {
        log.debug("REST request to save ETwo : {}", eTwo);
        if (eTwo.getId() != null) {
            throw new BadRequestAlertException("A new eTwo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ETwo result = eTwoService.save(eTwo);
        return ResponseEntity.created(new URI("/api/e-twos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /e-twos} : Updates an existing eTwo.
     *
     * @param eTwo the eTwo to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eTwo,
     * or with status {@code 400 (Bad Request)} if the eTwo is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eTwo couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/e-twos")
    public ResponseEntity<ETwo> updateETwo(@Valid @RequestBody ETwo eTwo) throws URISyntaxException {
        log.debug("REST request to update ETwo : {}", eTwo);
        if (eTwo.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ETwo result = eTwoService.save(eTwo);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eTwo.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /e-twos} : get all the eTwos.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eTwos in body.
     */
    @GetMapping("/e-twos")
    public ResponseEntity<List<ETwo>> getAllETwos(Pageable pageable) {
        log.debug("REST request to get a page of ETwos");
        Page<ETwo> page = eTwoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /e-twos/:id} : get the "id" eTwo.
     *
     * @param id the id of the eTwo to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eTwo, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/e-twos/{id}")
    public ResponseEntity<ETwo> getETwo(@PathVariable Long id) {
        log.debug("REST request to get ETwo : {}", id);
        Optional<ETwo> eTwo = eTwoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eTwo);
    }

    /**
     * {@code DELETE  /e-twos/:id} : delete the "id" eTwo.
     *
     * @param id the id of the eTwo to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/e-twos/{id}")
    public ResponseEntity<Void> deleteETwo(@PathVariable Long id) {
        log.debug("REST request to delete ETwo : {}", id);
        eTwoService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
