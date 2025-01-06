package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.TechCVEmployment;
import com.axone.hrsolution.repository.TechCVEmploymentRepository;
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
 * REST controller for managing {@link com.axone.hrsolution.domain.TechCVEmployment}.
 */
@RestController
@RequestMapping("/api/tech-cv-employments")
@Transactional
public class TechCVEmploymentResource {

    private final Logger log = LoggerFactory.getLogger(TechCVEmploymentResource.class);

    private static final String ENTITY_NAME = "techCVEmployment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TechCVEmploymentRepository techCVEmploymentRepository;

    public TechCVEmploymentResource(TechCVEmploymentRepository techCVEmploymentRepository) {
        this.techCVEmploymentRepository = techCVEmploymentRepository;
    }

    /**
     * {@code POST  /tech-cv-employments} : Create a new techCVEmployment.
     *
     * @param techCVEmployment the techCVEmployment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new techCVEmployment, or with status {@code 400 (Bad Request)} if the techCVEmployment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TechCVEmployment> createTechCVEmployment(@Valid @RequestBody TechCVEmployment techCVEmployment)
        throws URISyntaxException {
        log.debug("REST request to save TechCVEmployment : {}", techCVEmployment);
        if (techCVEmployment.getId() != null) {
            throw new BadRequestAlertException("A new techCVEmployment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        techCVEmployment = techCVEmploymentRepository.save(techCVEmployment);
        return ResponseEntity.created(new URI("/api/tech-cv-employments/" + techCVEmployment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, techCVEmployment.getId().toString()))
            .body(techCVEmployment);
    }

    /**
     * {@code PUT  /tech-cv-employments/:id} : Updates an existing techCVEmployment.
     *
     * @param id the id of the techCVEmployment to save.
     * @param techCVEmployment the techCVEmployment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated techCVEmployment,
     * or with status {@code 400 (Bad Request)} if the techCVEmployment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the techCVEmployment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TechCVEmployment> updateTechCVEmployment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TechCVEmployment techCVEmployment
    ) throws URISyntaxException {
        log.debug("REST request to update TechCVEmployment : {}, {}", id, techCVEmployment);
        if (techCVEmployment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, techCVEmployment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!techCVEmploymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        techCVEmployment = techCVEmploymentRepository.save(techCVEmployment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, techCVEmployment.getId().toString()))
            .body(techCVEmployment);
    }

    /**
     * {@code PATCH  /tech-cv-employments/:id} : Partial updates given fields of an existing techCVEmployment, field will ignore if it is null
     *
     * @param id the id of the techCVEmployment to save.
     * @param techCVEmployment the techCVEmployment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated techCVEmployment,
     * or with status {@code 400 (Bad Request)} if the techCVEmployment is not valid,
     * or with status {@code 404 (Not Found)} if the techCVEmployment is not found,
     * or with status {@code 500 (Internal Server Error)} if the techCVEmployment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TechCVEmployment> partialUpdateTechCVEmployment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TechCVEmployment techCVEmployment
    ) throws URISyntaxException {
        log.debug("REST request to partial update TechCVEmployment partially : {}, {}", id, techCVEmployment);
        if (techCVEmployment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, techCVEmployment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!techCVEmploymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TechCVEmployment> result = techCVEmploymentRepository
            .findById(techCVEmployment.getId())
            .map(existingTechCVEmployment -> {
                if (techCVEmployment.getEmployment() != null) {
                    existingTechCVEmployment.setEmployment(techCVEmployment.getEmployment());
                }

                return existingTechCVEmployment;
            })
            .map(techCVEmploymentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, techCVEmployment.getId().toString())
        );
    }

    /**
     * {@code GET  /tech-cv-employments} : get all the techCVEmployments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of techCVEmployments in body.
     */
    @GetMapping("")
    public List<TechCVEmployment> getAllTechCVEmployments() {
        log.debug("REST request to get all TechCVEmployments");
        return techCVEmploymentRepository.findAll();
    }

    /**
     * {@code GET  /tech-cv-employments/:id} : get the "id" techCVEmployment.
     *
     * @param id the id of the techCVEmployment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the techCVEmployment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TechCVEmployment> getTechCVEmployment(@PathVariable("id") Long id) {
        log.debug("REST request to get TechCVEmployment : {}", id);
        Optional<TechCVEmployment> techCVEmployment = techCVEmploymentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(techCVEmployment);
    }

    /**
     * {@code DELETE  /tech-cv-employments/:id} : delete the "id" techCVEmployment.
     *
     * @param id the id of the techCVEmployment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechCVEmployment(@PathVariable("id") Long id) {
        log.debug("REST request to delete TechCVEmployment : {}", id);
        techCVEmploymentRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
