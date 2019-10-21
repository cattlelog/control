package io.crf.cattlelog.control.service;

import io.crf.cattlelog.control.domain.Herd;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Herd}.
 */
public interface HerdService {

    /**
     * Save a herd.
     *
     * @param herd the entity to save.
     * @return the persisted entity.
     */
    Herd save(Herd herd);

    /**
     * Get all the herds.
     *
     * @return the list of entities.
     */
    List<Herd> findAll();


    /**
     * Get the "id" herd.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Herd> findOne(Long id);

    /**
     * Delete the "id" herd.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
