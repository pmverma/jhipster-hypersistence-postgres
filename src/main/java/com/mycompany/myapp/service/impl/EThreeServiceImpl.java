package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.service.EThreeService;
import com.mycompany.myapp.domain.EThree;
import com.mycompany.myapp.repository.EThreeRepository;
import com.mycompany.myapp.service.dto.EThreeDTO;
import com.mycompany.myapp.service.mapper.EThreeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link EThree}.
 */
@Service
@Transactional
public class EThreeServiceImpl implements EThreeService {

    private final Logger log = LoggerFactory.getLogger(EThreeServiceImpl.class);

    private final EThreeRepository eThreeRepository;

    private final EThreeMapper eThreeMapper;

    public EThreeServiceImpl(EThreeRepository eThreeRepository, EThreeMapper eThreeMapper) {
        this.eThreeRepository = eThreeRepository;
        this.eThreeMapper = eThreeMapper;
    }

    /**
     * Save a eThree.
     *
     * @param eThreeDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public EThreeDTO save(EThreeDTO eThreeDTO) {
        log.debug("Request to save EThree : {}", eThreeDTO);
        EThree eThree = eThreeMapper.toEntity(eThreeDTO);
        eThree = eThreeRepository.save(eThree);
        return eThreeMapper.toDto(eThree);
    }

    /**
     * Get all the eThrees.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<EThreeDTO> findAll() {
        log.debug("Request to get all EThrees");
        return eThreeRepository.findAll().stream()
            .map(eThreeMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one eThree by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<EThreeDTO> findOne(Long id) {
        log.debug("Request to get EThree : {}", id);
        return eThreeRepository.findById(id)
            .map(eThreeMapper::toDto);
    }

    /**
     * Delete the eThree by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete EThree : {}", id);
        eThreeRepository.deleteById(id);
    }
}
