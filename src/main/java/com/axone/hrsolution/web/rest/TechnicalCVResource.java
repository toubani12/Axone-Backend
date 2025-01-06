package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.TechnicalCV;
import com.axone.hrsolution.repository.TechnicalCVRepository;
import com.axone.hrsolution.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.axone.hrsolution.domain.TechnicalCV}.
 */
@RestController
@RequestMapping("/api/technical-cvs")
@Transactional
public class TechnicalCVResource {

    private final Logger log = LoggerFactory.getLogger(TechnicalCVResource.class);

    private static final String ENTITY_NAME = "technicalCV";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TechnicalCVRepository technicalCVRepository;

    public TechnicalCVResource(TechnicalCVRepository technicalCVRepository) {
        this.technicalCVRepository = technicalCVRepository;
    }

    /**
     * {@code POST  /technical-cvs} : Create a new technicalCV.
     *
     * @param technicalCV the technicalCV to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new technicalCV, or with status {@code 400 (Bad Request)} if the technicalCV has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TechnicalCV> createTechnicalCV(@Valid @RequestBody TechnicalCV technicalCV) throws URISyntaxException {
        log.debug("REST request to save TechnicalCV : {}", technicalCV);
        if (technicalCV.getId() != null) {
            throw new BadRequestAlertException("A new technicalCV cannot already have an ID", ENTITY_NAME, "idexists");
        }
        technicalCV = technicalCVRepository.save(technicalCV);
        return ResponseEntity.created(new URI("/api/technical-cvs/" + technicalCV.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, technicalCV.getId().toString()))
            .body(technicalCV);
    }

    /**
     * {@code PUT  /technical-cvs/:id} : Updates an existing technicalCV.
     *
     * @param id the id of the technicalCV to save.
     * @param technicalCV the technicalCV to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated technicalCV,
     * or with status {@code 400 (Bad Request)} if the technicalCV is not valid,
     * or with status {@code 500 (Internal Server Error)} if the technicalCV couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TechnicalCV> updateTechnicalCV(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TechnicalCV technicalCV
    ) throws URISyntaxException {
        log.debug("REST request to update TechnicalCV : {}, {}", id, technicalCV);
        if (technicalCV.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, technicalCV.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!technicalCVRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        technicalCV = technicalCVRepository.save(technicalCV);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, technicalCV.getId().toString()))
            .body(technicalCV);
    }

    /**
     * {@code PATCH  /technical-cvs/:id} : Partial updates given fields of an existing technicalCV, field will ignore if it is null
     *
     * @param id the id of the technicalCV to save.
     * @param technicalCV the technicalCV to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated technicalCV,
     * or with status {@code 400 (Bad Request)} if the technicalCV is not valid,
     * or with status {@code 404 (Not Found)} if the technicalCV is not found,
     * or with status {@code 500 (Internal Server Error)} if the technicalCV couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TechnicalCV> partialUpdateTechnicalCV(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TechnicalCV technicalCV
    ) throws URISyntaxException {
        log.debug("REST request to partial update TechnicalCV partially : {}, {}", id, technicalCV);
        if (technicalCV.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, technicalCV.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!technicalCVRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TechnicalCV> result = technicalCVRepository
            .findById(technicalCV.getId())
            .map(existingTechnicalCV -> {
                if (technicalCV.getName() != null) {
                    existingTechnicalCV.setName(technicalCV.getName());
                }
                if (technicalCV.getLevel() != null) {
                    existingTechnicalCV.setLevel(technicalCV.getLevel());
                }
                if (technicalCV.getProfileDescription() != null) {
                    existingTechnicalCV.setProfileDescription(technicalCV.getProfileDescription());
                }

                return existingTechnicalCV;
            })
            .map(technicalCVRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, technicalCV.getId().toString())
        );
    }

    /**
     * {@code GET  /technical-cvs} : get all the technicalCVS.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of technicalCVS in body.
     */
    @GetMapping("")
    public List<TechnicalCV> getAllTechnicalCVS(@RequestParam(name = "filter", required = false) String filter) {
        if ("candidate-is-null".equals(filter)) {
            log.debug("REST request to get all TechnicalCVs where candidate is null");
            return StreamSupport.stream(technicalCVRepository.findAll().spliterator(), false)
                .filter(technicalCV -> technicalCV.getCandidate() == null)
                .toList();
        }
        log.debug("REST request to get all TechnicalCVS");
        return technicalCVRepository.findAll();
    }

    /**
     * {@code GET  /technical-cvs/:id} : get the "id" technicalCV.
     *
     * @param id the id of the technicalCV to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the technicalCV, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TechnicalCV> getTechnicalCV(@PathVariable("id") Long id) {
        log.debug("REST request to get TechnicalCV : {}", id);
        Optional<TechnicalCV> technicalCV = technicalCVRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(technicalCV);
    }

    /**
     * {@code DELETE  /technical-cvs/:id} : delete the "id" technicalCV.
     *
     * @param id the id of the technicalCV to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechnicalCV(@PathVariable("id") Long id) {
        log.debug("REST request to delete TechnicalCV : {}", id);
        technicalCVRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
