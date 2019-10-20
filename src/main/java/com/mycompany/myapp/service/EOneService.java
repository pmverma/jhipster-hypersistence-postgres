package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.EOne;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link EOne}.
 */
public interface EOneService {

    /**
     * Save a eOne.
     *
     * @param eOne the entity to save.
     * @return the persisted entity.
     */
    EOne save(EOne eOne);

    /**
     * Get all the eOnes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EOne> findAll(Pageable pageable);


    /**
     * Get the "id" eOne.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EOne> findOne(Long id);

    /**
     * Delete the "id" eOne.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
