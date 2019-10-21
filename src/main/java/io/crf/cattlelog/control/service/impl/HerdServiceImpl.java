package io.crf.cattlelog.control.service.impl;

import io.crf.cattlelog.control.service.HerdService;
import io.crf.cattlelog.control.domain.Herd;
import io.crf.cattlelog.control.repository.HerdRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Herd}.
 */
@Service
@Transactional
public class HerdServiceImpl implements HerdService {

    private final Logger log = LoggerFactory.getLogger(HerdServiceImpl.class);

    private final HerdRepository herdRepository;

    public HerdServiceImpl(HerdRepository herdRepository) {
        this.herdRepository = herdRepository;
    }

    /**
     * Save a herd.
     *
     * @param herd the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Herd save(Herd herd) {
        log.debug("Request to save Herd : {}", herd);
        return herdRepository.save(herd);
    }

    /**
     * Get all the herds.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Herd> findAll() {
        log.debug("Request to get all Herds");
        return herdRepository.findAll();
    }


    /**
     * Get one herd by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Herd> findOne(Long id) {
        log.debug("Request to get Herd : {}", id);
        return herdRepository.findById(id);
    }

    /**
     * Delete the herd by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Herd : {}", id);
        herdRepository.deleteById(id);
    }
}
