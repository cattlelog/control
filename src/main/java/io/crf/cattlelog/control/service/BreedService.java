package io.crf.cattlelog.control.service;

import io.crf.cattlelog.control.domain.Breed;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Breed}.
 */
public interface BreedService {

    /**
     * Save a breed.
     *
     * @param breed the entity to save.
     * @return the persisted entity.
     */
    Breed save(Breed breed);

    /**
     * Get all the breeds.
     *
     * @return the list of entities.
     */
    List<Breed> findAll();


    /**
     * Get the "id" breed.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Breed> findOne(Long id);

    /**
     * Delete the "id" breed.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
