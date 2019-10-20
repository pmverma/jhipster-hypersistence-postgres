package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.service.EThreeService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.EThreeDTO;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.EThree}.
 */
@RestController
@RequestMapping("/api")
public class EThreeResource {

    private final Logger log = LoggerFactory.getLogger(EThreeResource.class);

    private static final String ENTITY_NAME = "eThree";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EThreeService eThreeService;

    public EThreeResource(EThreeService eThreeService) {
        this.eThreeService = eThreeService;
    }

    /**
     * {@code POST  /e-threes} : Create a new eThree.
     *
     * @param eThreeDTO the eThreeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eThreeDTO, or with status {@code 400 (Bad Request)} if the eThree has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/e-threes")
    public ResponseEntity<EThreeDTO> createEThree(@Valid @RequestBody EThreeDTO eThreeDTO) throws URISyntaxException {
        log.debug("REST request to save EThree : {}", eThreeDTO);
        if (eThreeDTO.getId() != null) {
            throw new BadRequestAlertException("A new eThree cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EThreeDTO result = eThreeService.save(eThreeDTO);
        return ResponseEntity.created(new URI("/api/e-threes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /e-threes} : Updates an existing eThree.
     *
     * @param eThreeDTO the eThreeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eThreeDTO,
     * or with status {@code 400 (Bad Request)} if the eThreeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eThreeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/e-threes")
    public ResponseEntity<EThreeDTO> updateEThree(@Valid @RequestBody EThreeDTO eThreeDTO) throws URISyntaxException {
        log.debug("REST request to update EThree : {}", eThreeDTO);
        if (eThreeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EThreeDTO result = eThreeService.save(eThreeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eThreeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /e-threes} : get all the eThrees.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eThrees in body.
     */
    @GetMapping("/e-threes")
    public List<EThreeDTO> getAllEThrees() {
        log.debug("REST request to get all EThrees");
        return eThreeService.findAll();
    }

    /**
     * {@code GET  /e-threes/:id} : get the "id" eThree.
     *
     * @param id the id of the eThreeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eThreeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/e-threes/{id}")
    public ResponseEntity<EThreeDTO> getEThree(@PathVariable Long id) {
        log.debug("REST request to get EThree : {}", id);
        Optional<EThreeDTO> eThreeDTO = eThreeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eThreeDTO);
    }

    /**
     * {@code DELETE  /e-threes/:id} : delete the "id" eThree.
     *
     * @param id the id of the eThreeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/e-threes/{id}")
    public ResponseEntity<Void> deleteEThree(@PathVariable Long id) {
        log.debug("REST request to delete EThree : {}", id);
        eThreeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
