package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.ESixService;
import com.mycompany.myapp.domain.ESix;
import com.mycompany.myapp.repository.ESixRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ESix}.
 */
@Service
@Transactional
public class ESixServiceImpl implements ESixService {

    private final Logger log = LoggerFactory.getLogger(ESixServiceImpl.class);

    private final ESixRepository eSixRepository;

    public ESixServiceImpl(ESixRepository eSixRepository) {
        this.eSixRepository = eSixRepository;
    }

    /**
     * Save a eSix.
     *
     * @param eSix the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ESix save(ESix eSix) {
        log.debug("Request to save ESix : {}", eSix);
        return eSixRepository.save(eSix);
    }

    /**
     * Get all the eSixes.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ESix> findAll() {
        log.debug("Request to get all ESixes");
        return eSixRepository.findAll();
    }


    /**
     * Get one eSix by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ESix> findOne(Long id) {
        log.debug("Request to get ESix : {}", id);
        return eSixRepository.findById(id);
    }

    /**
     * Delete the eSix by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ESix : {}", id);
        eSixRepository.deleteById(id);
    }
}
