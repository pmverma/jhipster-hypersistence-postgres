package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.EOneService;
import com.mycompany.myapp.domain.EOne;
import com.mycompany.myapp.repository.EOneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link EOne}.
 */
@Service
@Transactional
public class EOneServiceImpl implements EOneService {

    private final Logger log = LoggerFactory.getLogger(EOneServiceImpl.class);

    private final EOneRepository eOneRepository;

    public EOneServiceImpl(EOneRepository eOneRepository) {
        this.eOneRepository = eOneRepository;
    }

    /**
     * Save a eOne.
     *
     * @param eOne the entity to save.
     * @return the persisted entity.
     */
    @Override
    public EOne save(EOne eOne) {
        log.debug("Request to save EOne : {}", eOne);
        return eOneRepository.save(eOne);
    }

    /**
     * Get all the eOnes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EOne> findAll(Pageable pageable) {
        log.debug("Request to get all EOnes");
        return eOneRepository.findAll(pageable);
    }


    /**
     * Get one eOne by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EOne> findOne(Long id) {
        log.debug("Request to get EOne : {}", id);
        return eOneRepository.findById(id);
    }

    /**
     * Delete the eOne by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete EOne : {}", id);
        eOneRepository.deleteById(id);
    }
}
