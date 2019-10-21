package io.crf.cattlelog.control.web.rest;

import io.crf.cattlelog.control.domain.Herd;
import io.crf.cattlelog.control.service.HerdService;
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
 * REST controller for managing {@link io.crf.cattlelog.control.domain.Herd}.
 */
@RestController
@RequestMapping("/api")
public class HerdResource {

    private final Logger log = LoggerFactory.getLogger(HerdResource.class);

    private static final String ENTITY_NAME = "controlHerd";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HerdService herdService;

    public HerdResource(HerdService herdService) {
        this.herdService = herdService;
    }

    /**
     * {@code POST  /herds} : Create a new herd.
     *
     * @param herd the herd to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new herd, or with status {@code 400 (Bad Request)} if the herd has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/herds")
    public ResponseEntity<Herd> createHerd(@Valid @RequestBody Herd herd) throws URISyntaxException {
        log.debug("REST request to save Herd : {}", herd);
        if (herd.getId() != null) {
            throw new BadRequestAlertException("A new herd cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Herd result = herdService.save(herd);
        return ResponseEntity.created(new URI("/api/herds/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /herds} : Updates an existing herd.
     *
     * @param herd the herd to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated herd,
     * or with status {@code 400 (Bad Request)} if the herd is not valid,
     * or with status {@code 500 (Internal Server Error)} if the herd couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/herds")
    public ResponseEntity<Herd> updateHerd(@Valid @RequestBody Herd herd) throws URISyntaxException {
        log.debug("REST request to update Herd : {}", herd);
        if (herd.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Herd result = herdService.save(herd);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, herd.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /herds} : get all the herds.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of herds in body.
     */
    @GetMapping("/herds")
    public List<Herd> getAllHerds() {
        log.debug("REST request to get all Herds");
        return herdService.findAll();
    }

    /**
     * {@code GET  /herds/:id} : get the "id" herd.
     *
     * @param id the id of the herd to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the herd, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/herds/{id}")
    public ResponseEntity<Herd> getHerd(@PathVariable Long id) {
        log.debug("REST request to get Herd : {}", id);
        Optional<Herd> herd = herdService.findOne(id);
        return ResponseUtil.wrapOrNotFound(herd);
    }

    /**
     * {@code DELETE  /herds/:id} : delete the "id" herd.
     *
     * @param id the id of the herd to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/herds/{id}")
    public ResponseEntity<Void> deleteHerd(@PathVariable Long id) {
        log.debug("REST request to delete Herd : {}", id);
        herdService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
