package io.crf.cattlelog.control.service;

import io.crf.cattlelog.control.domain.Kind;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Kind}.
 */
public interface KindService {

    /**
     * Save a kind.
     *
     * @param kind the entity to save.
     * @return the persisted entity.
     */
    Kind save(Kind kind);

    /**
     * Get all the kinds.
     *
     * @return the list of entities.
     */
    List<Kind> findAll();


    /**
     * Get the "id" kind.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Kind> findOne(Long id);

    /**
     * Delete the "id" kind.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
