package io.crf.cattlelog.control.web.rest;

import io.crf.cattlelog.control.domain.Breed;
import io.crf.cattlelog.control.service.BreedService;
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
 * REST controller for managing {@link io.crf.cattlelog.control.domain.Breed}.
 */
@RestController
@RequestMapping("/api")
public class BreedResource {

    private final Logger log = LoggerFactory.getLogger(BreedResource.class);

    private static final String ENTITY_NAME = "controlBreed";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BreedService breedService;

    public BreedResource(BreedService breedService) {
        this.breedService = breedService;
    }

    /**
     * {@code POST  /breeds} : Create a new breed.
     *
     * @param breed the breed to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new breed, or with status {@code 400 (Bad Request)} if the breed has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/breeds")
    public ResponseEntity<Breed> createBreed(@Valid @RequestBody Breed breed) throws URISyntaxException {
        log.debug("REST request to save Breed : {}", breed);
        if (breed.getId() != null) {
            throw new BadRequestAlertException("A new breed cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Breed result = breedService.save(breed);
        return ResponseEntity.created(new URI("/api/breeds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /breeds} : Updates an existing breed.
     *
     * @param breed the breed to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated breed,
     * or with status {@code 400 (Bad Request)} if the breed is not valid,
     * or with status {@code 500 (Internal Server Error)} if the breed couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/breeds")
    public ResponseEntity<Breed> updateBreed(@Valid @RequestBody Breed breed) throws URISyntaxException {
        log.debug("REST request to update Breed : {}", breed);
        if (breed.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Breed result = breedService.save(breed);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, breed.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /breeds} : get all the breeds.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of breeds in body.
     */
    @GetMapping("/breeds")
    public List<Breed> getAllBreeds() {
        log.debug("REST request to get all Breeds");
        return breedService.findAll();
    }

    /**
     * {@code GET  /breeds/:id} : get the "id" breed.
     *
     * @param id the id of the breed to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the breed, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/breeds/{id}")
    public ResponseEntity<Breed> getBreed(@PathVariable Long id) {
        log.debug("REST request to get Breed : {}", id);
        Optional<Breed> breed = breedService.findOne(id);
        return ResponseUtil.wrapOrNotFound(breed);
    }

    /**
     * {@code DELETE  /breeds/:id} : delete the "id" breed.
     *
     * @param id the id of the breed to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/breeds/{id}")
    public ResponseEntity<Void> deleteBreed(@PathVariable Long id) {
        log.debug("REST request to delete Breed : {}", id);
        breedService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
