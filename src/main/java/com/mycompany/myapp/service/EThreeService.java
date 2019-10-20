package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.EThreeDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.EThree}.
 */
public interface EThreeService {

    /**
     * Save a eThree.
     *
     * @param eThreeDTO the entity to save.
     * @return the persisted entity.
     */
    EThreeDTO save(EThreeDTO eThreeDTO);

    /**
     * Get all the eThrees.
     *
     * @return the list of entities.
     */
    List<EThreeDTO> findAll();


    /**
     * Get the "id" eThree.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EThreeDTO> findOne(Long id);

    /**
     * Delete the "id" eThree.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
