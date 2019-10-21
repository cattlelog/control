package io.crf.cattlelog.control.service;

import io.crf.cattlelog.control.domain.Medical;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Medical}.
 */
public interface MedicalService {

    /**
     * Save a medical.
     *
     * @param medical the entity to save.
     * @return the persisted entity.
     */
    Medical save(Medical medical);

    /**
     * Get all the medicals.
     *
     * @return the list of entities.
     */
    List<Medical> findAll();


    /**
     * Get the "id" medical.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Medical> findOne(Long id);

    /**
     * Delete the "id" medical.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
