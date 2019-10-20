package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.EFive;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link EFive}.
 */
public interface EFiveService {

    /**
     * Save a eFive.
     *
     * @param eFive the entity to save.
     * @return the persisted entity.
     */
    EFive save(EFive eFive);

    /**
     * Get all the eFives.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EFive> findAll(Pageable pageable);


    /**
     * Get the "id" eFive.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EFive> findOne(Long id);

    /**
     * Delete the "id" eFive.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
