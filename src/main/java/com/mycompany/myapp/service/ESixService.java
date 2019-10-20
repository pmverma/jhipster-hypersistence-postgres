package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ESix;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ESix}.
 */
public interface ESixService {

    /**
     * Save a eSix.
     *
     * @param eSix the entity to save.
     * @return the persisted entity.
     */
    ESix save(ESix eSix);

    /**
     * Get all the eSixes.
     *
     * @return the list of entities.
     */
    List<ESix> findAll();


    /**
     * Get the "id" eSix.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ESix> findOne(Long id);

    /**
     * Delete the "id" eSix.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
