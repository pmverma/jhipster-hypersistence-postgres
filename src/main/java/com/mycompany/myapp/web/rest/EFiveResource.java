package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.EFive;
import com.mycompany.myapp.service.EFiveService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.EFive}.
 */
@RestController
@RequestMapping("/api")
public class EFiveResource {

    private final Logger log = LoggerFactory.getLogger(EFiveResource.class);

    private static final String ENTITY_NAME = "eFive";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EFiveService eFiveService;

    public EFiveResource(EFiveService eFiveService) {
        this.eFiveService = eFiveService;
    }

    /**
     * {@code POST  /e-fives} : Create a new eFive.
     *
     * @param eFive the eFive to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eFive, or with status {@code 400 (Bad Request)} if the eFive has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/e-fives")
    public ResponseEntity<EFive> createEFive(@Valid @RequestBody EFive eFive) throws URISyntaxException {
        log.debug("REST request to save EFive : {}", eFive);
        if (eFive.getId() != null) {
            throw new BadRequestAlertException("A new eFive cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EFive result = eFiveService.save(eFive);
        return ResponseEntity.created(new URI("/api/e-fives/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /e-fives} : Updates an existing eFive.
     *
     * @param eFive the eFive to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eFive,
     * or with status {@code 400 (Bad Request)} if the eFive is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eFive couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/e-fives")
    public ResponseEntity<EFive> updateEFive(@Valid @RequestBody EFive eFive) throws URISyntaxException {
        log.debug("REST request to update EFive : {}", eFive);
        if (eFive.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EFive result = eFiveService.save(eFive);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eFive.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /e-fives} : get all the eFives.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eFives in body.
     */
    @GetMapping("/e-fives")
    public ResponseEntity<List<EFive>> getAllEFives(Pageable pageable) {
        log.debug("REST request to get a page of EFives");
        Page<EFive> page = eFiveService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /e-fives/:id} : get the "id" eFive.
     *
     * @param id the id of the eFive to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eFive, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/e-fives/{id}")
    public ResponseEntity<EFive> getEFive(@PathVariable Long id) {
        log.debug("REST request to get EFive : {}", id);
        Optional<EFive> eFive = eFiveService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eFive);
    }

    /**
     * {@code DELETE  /e-fives/:id} : delete the "id" eFive.
     *
     * @param id the id of the eFive to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/e-fives/{id}")
    public ResponseEntity<Void> deleteEFive(@PathVariable Long id) {
        log.debug("REST request to delete EFive : {}", id);
        eFiveService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
