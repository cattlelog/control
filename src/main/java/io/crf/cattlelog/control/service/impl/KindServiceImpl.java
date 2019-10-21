package io.crf.cattlelog.control.service.impl;

import io.crf.cattlelog.control.service.KindService;
import io.crf.cattlelog.control.domain.Kind;
import io.crf.cattlelog.control.repository.KindRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Kind}.
 */
@Service
@Transactional
public class KindServiceImpl implements KindService {

    private final Logger log = LoggerFactory.getLogger(KindServiceImpl.class);

    private final KindRepository kindRepository;

    public KindServiceImpl(KindRepository kindRepository) {
        this.kindRepository = kindRepository;
    }

    /**
     * Save a kind.
     *
     * @param kind the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Kind save(Kind kind) {
        log.debug("Request to save Kind : {}", kind);
        return kindRepository.save(kind);
    }

    /**
     * Get all the kinds.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Kind> findAll() {
        log.debug("Request to get all Kinds");
        return kindRepository.findAll();
    }


    /**
     * Get one kind by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Kind> findOne(Long id) {
        log.debug("Request to get Kind : {}", id);
        return kindRepository.findById(id);
    }

    /**
     * Delete the kind by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Kind : {}", id);
        kindRepository.deleteById(id);
    }
}
