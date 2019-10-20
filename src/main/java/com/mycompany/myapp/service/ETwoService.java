package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ETwo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link ETwo}.
 */
public interface ETwoService {

    /**
     * Save a eTwo.
     *
     * @param eTwo the entity to save.
     * @return the persisted entity.
     */
    ETwo save(ETwo eTwo);

    /**
     * Get all the eTwos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ETwo> findAll(Pageable pageable);


    /**
     * Get the "id" eTwo.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ETwo> findOne(Long id);

    /**
     * Delete the "id" eTwo.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
