package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.TechCVEducation;
import com.axone.hrsolution.repository.TechCVEducationRepository;
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
 * REST controller for managing {@link com.axone.hrsolution.domain.TechCVEducation}.
 */
@RestController
@RequestMapping("/api/tech-cv-educations")
@Transactional
public class TechCVEducationResource {

    private final Logger log = LoggerFactory.getLogger(TechCVEducationResource.class);

    private static final String ENTITY_NAME = "techCVEducation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TechCVEducationRepository techCVEducationRepository;

    public TechCVEducationResource(TechCVEducationRepository techCVEducationRepository) {
        this.techCVEducationRepository = techCVEducationRepository;
    }

    /**
     * {@code POST  /tech-cv-educations} : Create a new techCVEducation.
     *
     * @param techCVEducation the techCVEducation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new techCVEducation, or with status {@code 400 (Bad Request)} if the techCVEducation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TechCVEducation> createTechCVEducation(@Valid @RequestBody TechCVEducation techCVEducation)
        throws URISyntaxException {
        log.debug("REST request to save TechCVEducation : {}", techCVEducation);
        if (techCVEducation.getId() != null) {
            throw new BadRequestAlertException("A new techCVEducation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        techCVEducation = techCVEducationRepository.save(techCVEducation);
        return ResponseEntity.created(new URI("/api/tech-cv-educations/" + techCVEducation.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, techCVEducation.getId().toString()))
            .body(techCVEducation);
    }

    /**
     * {@code PUT  /tech-cv-educations/:id} : Updates an existing techCVEducation.
     *
     * @param id the id of the techCVEducation to save.
     * @param techCVEducation the techCVEducation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated techCVEducation,
     * or with status {@code 400 (Bad Request)} if the techCVEducation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the techCVEducation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TechCVEducation> updateTechCVEducation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TechCVEducation techCVEducation
    ) throws URISyntaxException {
        log.debug("REST request to update TechCVEducation : {}, {}", id, techCVEducation);
        if (techCVEducation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, techCVEducation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!techCVEducationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        techCVEducation = techCVEducationRepository.save(techCVEducation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, techCVEducation.getId().toString()))
            .body(techCVEducation);
    }

    /**
     * {@code PATCH  /tech-cv-educations/:id} : Partial updates given fields of an existing techCVEducation, field will ignore if it is null
     *
     * @param id the id of the techCVEducation to save.
     * @param techCVEducation the techCVEducation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated techCVEducation,
     * or with status {@code 400 (Bad Request)} if the techCVEducation is not valid,
     * or with status {@code 404 (Not Found)} if the techCVEducation is not found,
     * or with status {@code 500 (Internal Server Error)} if the techCVEducation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TechCVEducation> partialUpdateTechCVEducation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TechCVEducation techCVEducation
    ) throws URISyntaxException {
        log.debug("REST request to partial update TechCVEducation partially : {}, {}", id, techCVEducation);
        if (techCVEducation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, techCVEducation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!techCVEducationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TechCVEducation> result = techCVEducationRepository
            .findById(techCVEducation.getId())
            .map(existingTechCVEducation -> {
                if (techCVEducation.getEducation() != null) {
                    existingTechCVEducation.setEducation(techCVEducation.getEducation());
                }

                return existingTechCVEducation;
            })
            .map(techCVEducationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, techCVEducation.getId().toString())
        );
    }

    /**
     * {@code GET  /tech-cv-educations} : get all the techCVEducations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of techCVEducations in body.
     */
    @GetMapping("")
    public List<TechCVEducation> getAllTechCVEducations() {
        log.debug("REST request to get all TechCVEducations");
        return techCVEducationRepository.findAll();
    }

    /**
     * {@code GET  /tech-cv-educations/:id} : get the "id" techCVEducation.
     *
     * @param id the id of the techCVEducation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the techCVEducation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TechCVEducation> getTechCVEducation(@PathVariable("id") Long id) {
        log.debug("REST request to get TechCVEducation : {}", id);
        Optional<TechCVEducation> techCVEducation = techCVEducationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(techCVEducation);
    }

    /**
     * {@code DELETE  /tech-cv-educations/:id} : delete the "id" techCVEducation.
     *
     * @param id the id of the techCVEducation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechCVEducation(@PathVariable("id") Long id) {
        log.debug("REST request to delete TechCVEducation : {}", id);
        techCVEducationRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
