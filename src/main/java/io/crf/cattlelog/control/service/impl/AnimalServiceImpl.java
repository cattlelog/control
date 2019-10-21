package io.crf.cattlelog.control.service.impl;

import io.crf.cattlelog.control.service.AnimalService;
import io.crf.cattlelog.control.domain.Animal;
import io.crf.cattlelog.control.repository.AnimalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Animal}.
 */
@Service
@Transactional
public class AnimalServiceImpl implements AnimalService {

    private final Logger log = LoggerFactory.getLogger(AnimalServiceImpl.class);

    private final AnimalRepository animalRepository;

    public AnimalServiceImpl(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    /**
     * Save a animal.
     *
     * @param animal the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Animal save(Animal animal) {
        log.debug("Request to save Animal : {}", animal);
        return animalRepository.save(animal);
    }

    /**
     * Get all the animals.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Animal> findAll() {
        log.debug("Request to get all Animals");
        return animalRepository.findAll();
    }


    /**
     * Get one animal by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Animal> findOne(Long id) {
        log.debug("Request to get Animal : {}", id);
        return animalRepository.findById(id);
    }

    /**
     * Delete the animal by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Animal : {}", id);
        animalRepository.deleteById(id);
    }
}
