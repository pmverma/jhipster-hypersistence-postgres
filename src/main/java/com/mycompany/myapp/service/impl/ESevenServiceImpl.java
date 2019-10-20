package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.ESevenService;
import com.mycompany.myapp.domain.ESeven;
import com.mycompany.myapp.repository.ESevenRepository;
import com.mycompany.myapp.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link ESeven}.
 */
@Service
@Transactional
public class ESevenServiceImpl implements ESevenService {

    private final Logger log = LoggerFactory.getLogger(ESevenServiceImpl.class);

    private final ESevenRepository eSevenRepository;

    private final UserRepository userRepository;

    public ESevenServiceImpl(ESevenRepository eSevenRepository, UserRepository userRepository) {
        this.eSevenRepository = eSevenRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a eSeven.
     *
     * @param eSeven the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ESeven save(ESeven eSeven) {
        log.debug("Request to save ESeven : {}", eSeven);
        long userId = eSeven.getUser().getId();
        userRepository.findById(userId).ifPresent(eSeven::user);
        return eSevenRepository.save(eSeven);
    }

    /**
     * Get all the eSevens.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<ESeven> findAll() {
        log.debug("Request to get all ESevens");
        return eSevenRepository.findAll();
    }


    /**
     * Get one eSeven by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ESeven> findOne(Long id) {
        log.debug("Request to get ESeven : {}", id);
        return eSevenRepository.findById(id);
    }

    /**
     * Delete the eSeven by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ESeven : {}", id);
        eSevenRepository.deleteById(id);
    }
}
