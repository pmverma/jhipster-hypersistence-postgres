package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.EFourService;
import com.mycompany.myapp.domain.EFour;
import com.mycompany.myapp.repository.EFourRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link EFour}.
 */
@Service
@Transactional
public class EFourServiceImpl implements EFourService {

    private final Logger log = LoggerFactory.getLogger(EFourServiceImpl.class);

    private final EFourRepository eFourRepository;

    public EFourServiceImpl(EFourRepository eFourRepository) {
        this.eFourRepository = eFourRepository;
    }

    /**
     * Save a eFour.
     *
     * @param eFour the entity to save.
     * @return the persisted entity.
     */
    @Override
    public EFour save(EFour eFour) {
        log.debug("Request to save EFour : {}", eFour);
        return eFourRepository.save(eFour);
    }

    /**
     * Get all the eFours.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<EFour> findAll() {
        log.debug("Request to get all EFours");
        return eFourRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the eFours with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<EFour> findAllWithEagerRelationships(Pageable pageable) {
        return eFourRepository.findAllWithEagerRelationships(pageable);
    }
    

    /**
     * Get one eFour by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EFour> findOne(Long id) {
        log.debug("Request to get EFour : {}", id);
        return eFourRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the eFour by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete EFour : {}", id);
        eFourRepository.deleteById(id);
    }
}
