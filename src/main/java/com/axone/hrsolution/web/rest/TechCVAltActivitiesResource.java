package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.TechCVAltActivities;
import com.axone.hrsolution.repository.TechCVAltActivitiesRepository;
import com.axone.hrsolution.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.axone.hrsolution.domain.TechCVAltActivities}.
 */
@RestController
@RequestMapping("/api/tech-cv-alt-activities")
@Transactional
public class TechCVAltActivitiesResource {

    private final Logger log = LoggerFactory.getLogger(TechCVAltActivitiesResource.class);

    private static final String ENTITY_NAME = "techCVAltActivities";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TechCVAltActivitiesRepository techCVAltActivitiesRepository;

    public TechCVAltActivitiesResource(TechCVAltActivitiesRepository techCVAltActivitiesRepository) {
        this.techCVAltActivitiesRepository = techCVAltActivitiesRepository;
    }

    /**
     * {@code POST  /tech-cv-alt-activities} : Create a new techCVAltActivities.
     *
     * @param techCVAltActivities the techCVAltActivities to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new techCVAltActivities, or with status {@code 400 (Bad Request)} if the techCVAltActivities has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TechCVAltActivities> createTechCVAltActivities(@Valid @RequestBody TechCVAltActivities techCVAltActivities)
        throws URISyntaxException {
        log.debug("REST request to save TechCVAltActivities : {}", techCVAltActivities);
        if (techCVAltActivities.getId() != null) {
            throw new BadRequestAlertException("A new techCVAltActivities cannot already have an ID", ENTITY_NAME, "idexists");
        }
        techCVAltActivities = techCVAltActivitiesRepository.save(techCVAltActivities);
        return ResponseEntity.created(new URI("/api/tech-cv-alt-activities/" + techCVAltActivities.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, techCVAltActivities.getId().toString()))
            .body(techCVAltActivities);
    }

    /**
     * {@code PUT  /tech-cv-alt-activities/:id} : Updates an existing techCVAltActivities.
     *
     * @param id the id of the techCVAltActivities to save.
     * @param techCVAltActivities the techCVAltActivities to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated techCVAltActivities,
     * or with status {@code 400 (Bad Request)} if the techCVAltActivities is not valid,
     * or with status {@code 500 (Internal Server Error)} if the techCVAltActivities couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TechCVAltActivities> updateTechCVAltActivities(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TechCVAltActivities techCVAltActivities
    ) throws URISyntaxException {
        log.debug("REST request to update TechCVAltActivities : {}, {}", id, techCVAltActivities);
        if (techCVAltActivities.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, techCVAltActivities.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!techCVAltActivitiesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        techCVAltActivities = techCVAltActivitiesRepository.save(techCVAltActivities);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, techCVAltActivities.getId().toString()))
            .body(techCVAltActivities);
    }

    /**
     * {@code PATCH  /tech-cv-alt-activities/:id} : Partial updates given fields of an existing techCVAltActivities, field will ignore if it is null
     *
     * @param id the id of the techCVAltActivities to save.
     * @param techCVAltActivities the techCVAltActivities to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated techCVAltActivities,
     * or with status {@code 400 (Bad Request)} if the techCVAltActivities is not valid,
     * or with status {@code 404 (Not Found)} if the techCVAltActivities is not found,
     * or with status {@code 500 (Internal Server Error)} if the techCVAltActivities couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TechCVAltActivities> partialUpdateTechCVAltActivities(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TechCVAltActivities techCVAltActivities
    ) throws URISyntaxException {
        log.debug("REST request to partial update TechCVAltActivities partially : {}, {}", id, techCVAltActivities);
        if (techCVAltActivities.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, techCVAltActivities.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!techCVAltActivitiesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TechCVAltActivities> result = techCVAltActivitiesRepository
            .findById(techCVAltActivities.getId())
            .map(existingTechCVAltActivities -> {
                if (techCVAltActivities.getActivities() != null) {
                    existingTechCVAltActivities.setActivities(techCVAltActivities.getActivities());
                }

                return existingTechCVAltActivities;
            })
            .map(techCVAltActivitiesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, techCVAltActivities.getId().toString())
        );
    }

    /**
     * {@code GET  /tech-cv-alt-activities} : get all the techCVAltActivities.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of techCVAltActivities in body.
     */
    @GetMapping("")
    public List<TechCVAltActivities> getAllTechCVAltActivities() {
        log.debug("REST request to get all TechCVAltActivities");
        return techCVAltActivitiesRepository.findAll();
    }

    /**
     * {@code GET  /tech-cv-alt-activities/:id} : get the "id" techCVAltActivities.
     *
     * @param id the id of the techCVAltActivities to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the techCVAltActivities, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TechCVAltActivities> getTechCVAltActivities(@PathVariable("id") Long id) {
        log.debug("REST request to get TechCVAltActivities : {}", id);
        Optional<TechCVAltActivities> techCVAltActivities = techCVAltActivitiesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(techCVAltActivities);
    }

    /**
     * {@code DELETE  /tech-cv-alt-activities/:id} : delete the "id" techCVAltActivities.
     *
     * @param id the id of the techCVAltActivities to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechCVAltActivities(@PathVariable("id") Long id) {
        log.debug("REST request to delete TechCVAltActivities : {}", id);
        techCVAltActivitiesRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
