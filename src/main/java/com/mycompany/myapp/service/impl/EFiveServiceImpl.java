package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.EFiveService;
import com.mycompany.myapp.domain.EFive;
import com.mycompany.myapp.repository.EFiveRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link EFive}.
 */
@Service
@Transactional
public class EFiveServiceImpl implements EFiveService {

    private final Logger log = LoggerFactory.getLogger(EFiveServiceImpl.class);

    private final EFiveRepository eFiveRepository;

    public EFiveServiceImpl(EFiveRepository eFiveRepository) {
        this.eFiveRepository = eFiveRepository;
    }

    /**
     * Save a eFive.
     *
     * @param eFive the entity to save.
     * @return the persisted entity.
     */
    @Override
    public EFive save(EFive eFive) {
        log.debug("Request to save EFive : {}", eFive);
        return eFiveRepository.save(eFive);
    }

    /**
     * Get all the eFives.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<EFive> findAll(Pageable pageable) {
        log.debug("Request to get all EFives");
        return eFiveRepository.findAll(pageable);
    }


    /**
     * Get one eFive by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EFive> findOne(Long id) {
        log.debug("Request to get EFive : {}", id);
        return eFiveRepository.findById(id);
    }

    /**
     * Delete the eFive by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete EFive : {}", id);
        eFiveRepository.deleteById(id);
    }
}
