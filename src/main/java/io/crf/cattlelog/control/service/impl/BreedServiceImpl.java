package io.crf.cattlelog.control.service.impl;

import io.crf.cattlelog.control.service.BreedService;
import io.crf.cattlelog.control.domain.Breed;
import io.crf.cattlelog.control.repository.BreedRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Breed}.
 */
@Service
@Transactional
public class BreedServiceImpl implements BreedService {

    private final Logger log = LoggerFactory.getLogger(BreedServiceImpl.class);

    private final BreedRepository breedRepository;

    public BreedServiceImpl(BreedRepository breedRepository) {
        this.breedRepository = breedRepository;
    }

    /**
     * Save a breed.
     *
     * @param breed the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Breed save(Breed breed) {
        log.debug("Request to save Breed : {}", breed);
        return breedRepository.save(breed);
    }

    /**
     * Get all the breeds.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Breed> findAll() {
        log.debug("Request to get all Breeds");
        return breedRepository.findAll();
    }


    /**
     * Get one breed by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Breed> findOne(Long id) {
        log.debug("Request to get Breed : {}", id);
        return breedRepository.findById(id);
    }

    /**
     * Delete the breed by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Breed : {}", id);
        breedRepository.deleteById(id);
    }
}
