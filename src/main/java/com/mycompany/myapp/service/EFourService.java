package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.EFour;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link EFour}.
 */
public interface EFourService {

    /**
     * Save a eFour.
     *
     * @param eFour the entity to save.
     * @return the persisted entity.
     */
    EFour save(EFour eFour);

    /**
     * Get all the eFours.
     *
     * @return the list of entities.
     */
    List<EFour> findAll();

    /**
     * Get all the eFours with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    Page<EFour> findAllWithEagerRelationships(Pageable pageable);
    
    /**
     * Get the "id" eFour.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EFour> findOne(Long id);

    /**
     * Delete the "id" eFour.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
