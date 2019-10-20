package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ESeven;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ESeven}.
 */
public interface ESevenService {

    /**
     * Save a eSeven.
     *
     * @param eSeven the entity to save.
     * @return the persisted entity.
     */
    ESeven save(ESeven eSeven);

    /**
     * Get all the eSevens.
     *
     * @return the list of entities.
     */
    List<ESeven> findAll();


    /**
     * Get the "id" eSeven.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ESeven> findOne(Long id);

    /**
     * Delete the "id" eSeven.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
