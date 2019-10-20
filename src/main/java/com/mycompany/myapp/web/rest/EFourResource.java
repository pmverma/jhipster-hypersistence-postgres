package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.EFour;
import com.mycompany.myapp.service.EFourService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.EFour}.
 */
@RestController
@RequestMapping("/api")
public class EFourResource {

    private final Logger log = LoggerFactory.getLogger(EFourResource.class);

    private static final String ENTITY_NAME = "eFour";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EFourService eFourService;

    public EFourResource(EFourService eFourService) {
        this.eFourService = eFourService;
    }

    /**
     * {@code POST  /e-fours} : Create a new eFour.
     *
     * @param eFour the eFour to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eFour, or with status {@code 400 (Bad Request)} if the eFour has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/e-fours")
    public ResponseEntity<EFour> createEFour(@Valid @RequestBody EFour eFour) throws URISyntaxException {
        log.debug("REST request to save EFour : {}", eFour);
        if (eFour.getId() != null) {
            throw new BadRequestAlertException("A new eFour cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EFour result = eFourService.save(eFour);
        return ResponseEntity.created(new URI("/api/e-fours/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /e-fours} : Updates an existing eFour.
     *
     * @param eFour the eFour to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eFour,
     * or with status {@code 400 (Bad Request)} if the eFour is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eFour couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/e-fours")
    public ResponseEntity<EFour> updateEFour(@Valid @RequestBody EFour eFour) throws URISyntaxException {
        log.debug("REST request to update EFour : {}", eFour);
        if (eFour.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        EFour result = eFourService.save(eFour);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, eFour.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /e-fours} : get all the eFours.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eFours in body.
     */
    @GetMapping("/e-fours")
    public List<EFour> getAllEFours(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all EFours");
        return eFourService.findAll();
    }

    /**
     * {@code GET  /e-fours/:id} : get the "id" eFour.
     *
     * @param id the id of the eFour to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eFour, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/e-fours/{id}")
    public ResponseEntity<EFour> getEFour(@PathVariable Long id) {
        log.debug("REST request to get EFour : {}", id);
        Optional<EFour> eFour = eFourService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eFour);
    }

    /**
     * {@code DELETE  /e-fours/:id} : delete the "id" eFour.
     *
     * @param id the id of the eFour to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/e-fours/{id}")
    public ResponseEntity<Void> deleteEFour(@PathVariable Long id) {
        log.debug("REST request to delete EFour : {}", id);
        eFourService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
