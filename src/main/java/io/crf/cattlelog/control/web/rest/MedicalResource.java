package io.crf.cattlelog.control.web.rest;

import io.crf.cattlelog.control.domain.Medical;
import io.crf.cattlelog.control.service.MedicalService;
import io.crf.cattlelog.control.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.crf.cattlelog.control.domain.Medical}.
 */
@RestController
@RequestMapping("/api")
public class MedicalResource {

    private final Logger log = LoggerFactory.getLogger(MedicalResource.class);

    private static final String ENTITY_NAME = "controlMedical";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedicalService medicalService;

    public MedicalResource(MedicalService medicalService) {
        this.medicalService = medicalService;
    }

    /**
     * {@code POST  /medicals} : Create a new medical.
     *
     * @param medical the medical to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medical, or with status {@code 400 (Bad Request)} if the medical has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/medicals")
    public ResponseEntity<Medical> createMedical(@Valid @RequestBody Medical medical) throws URISyntaxException {
        log.debug("REST request to save Medical : {}", medical);
        if (medical.getId() != null) {
            throw new BadRequestAlertException("A new medical cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Medical result = medicalService.save(medical);
        return ResponseEntity.created(new URI("/api/medicals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /medicals} : Updates an existing medical.
     *
     * @param medical the medical to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medical,
     * or with status {@code 400 (Bad Request)} if the medical is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medical couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/medicals")
    public ResponseEntity<Medical> updateMedical(@Valid @RequestBody Medical medical) throws URISyntaxException {
        log.debug("REST request to update Medical : {}", medical);
        if (medical.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Medical result = medicalService.save(medical);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medical.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /medicals} : get all the medicals.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medicals in body.
     */
    @GetMapping("/medicals")
    public List<Medical> getAllMedicals() {
        log.debug("REST request to get all Medicals");
        return medicalService.findAll();
    }

    /**
     * {@code GET  /medicals/:id} : get the "id" medical.
     *
     * @param id the id of the medical to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medical, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/medicals/{id}")
    public ResponseEntity<Medical> getMedical(@PathVariable Long id) {
        log.debug("REST request to get Medical : {}", id);
        Optional<Medical> medical = medicalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medical);
    }

    /**
     * {@code DELETE  /medicals/:id} : delete the "id" medical.
     *
     * @param id the id of the medical to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/medicals/{id}")
    public ResponseEntity<Void> deleteMedical(@PathVariable Long id) {
        log.debug("REST request to delete Medical : {}", id);
        medicalService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
