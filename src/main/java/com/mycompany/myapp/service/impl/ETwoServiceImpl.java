package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.ETwoService;
import com.mycompany.myapp.domain.ETwo;
import com.mycompany.myapp.repository.ETwoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ETwo}.
 */
@Service
@Transactional
public class ETwoServiceImpl implements ETwoService {

    private final Logger log = LoggerFactory.getLogger(ETwoServiceImpl.class);

    private final ETwoRepository eTwoRepository;

    public ETwoServiceImpl(ETwoRepository eTwoRepository) {
        this.eTwoRepository = eTwoRepository;
    }

    /**
     * Save a eTwo.
     *
     * @param eTwo the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ETwo save(ETwo eTwo) {
        log.debug("Request to save ETwo : {}", eTwo);
        return eTwoRepository.save(eTwo);
    }

    /**
     * Get all the eTwos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ETwo> findAll(Pageable pageable) {
        log.debug("Request to get all ETwos");
        return eTwoRepository.findAll(pageable);
    }


    /**
     * Get one eTwo by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ETwo> findOne(Long id) {
        log.debug("Request to get ETwo : {}", id);
        return eTwoRepository.findById(id);
    }

    /**
     * Delete the eTwo by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ETwo : {}", id);
        eTwoRepository.deleteById(id);
    }
}
