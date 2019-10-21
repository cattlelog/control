package io.crf.cattlelog.control.service.impl;

import io.crf.cattlelog.control.service.MedicalService;
import io.crf.cattlelog.control.domain.Medical;
import io.crf.cattlelog.control.repository.MedicalRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Medical}.
 */
@Service
@Transactional
public class MedicalServiceImpl implements MedicalService {

    private final Logger log = LoggerFactory.getLogger(MedicalServiceImpl.class);

    private final MedicalRepository medicalRepository;

    public MedicalServiceImpl(MedicalRepository medicalRepository) {
        this.medicalRepository = medicalRepository;
    }

    /**
     * Save a medical.
     *
     * @param medical the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Medical save(Medical medical) {
        log.debug("Request to save Medical : {}", medical);
        return medicalRepository.save(medical);
    }

    /**
     * Get all the medicals.
     *
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public List<Medical> findAll() {
        log.debug("Request to get all Medicals");
        return medicalRepository.findAll();
    }


    /**
     * Get one medical by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Medical> findOne(Long id) {
        log.debug("Request to get Medical : {}", id);
        return medicalRepository.findById(id);
    }

    /**
     * Delete the medical by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Medical : {}", id);
        medicalRepository.deleteById(id);
    }
}
