package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.TechCVDocs;
import com.axone.hrsolution.repository.TechCVDocsRepository;
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
 * REST controller for managing {@link com.axone.hrsolution.domain.TechCVDocs}.
 */
@RestController
@RequestMapping("/api/tech-cv-docs")
@Transactional
public class TechCVDocsResource {

    private final Logger log = LoggerFactory.getLogger(TechCVDocsResource.class);

    private static final String ENTITY_NAME = "techCVDocs";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TechCVDocsRepository techCVDocsRepository;

    public TechCVDocsResource(TechCVDocsRepository techCVDocsRepository) {
        this.techCVDocsRepository = techCVDocsRepository;
    }

    /**
     * {@code POST  /tech-cv-docs} : Create a new techCVDocs.
     *
     * @param techCVDocs the techCVDocs to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new techCVDocs, or with status {@code 400 (Bad Request)} if the techCVDocs has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TechCVDocs> createTechCVDocs(@Valid @RequestBody TechCVDocs techCVDocs) throws URISyntaxException {
        log.debug("REST request to save TechCVDocs : {}", techCVDocs);
        if (techCVDocs.getId() != null) {
            throw new BadRequestAlertException("A new techCVDocs cannot already have an ID", ENTITY_NAME, "idexists");
        }
        techCVDocs = techCVDocsRepository.save(techCVDocs);
        return ResponseEntity.created(new URI("/api/tech-cv-docs/" + techCVDocs.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, techCVDocs.getId().toString()))
            .body(techCVDocs);
    }

    /**
     * {@code PUT  /tech-cv-docs/:id} : Updates an existing techCVDocs.
     *
     * @param id the id of the techCVDocs to save.
     * @param techCVDocs the techCVDocs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated techCVDocs,
     * or with status {@code 400 (Bad Request)} if the techCVDocs is not valid,
     * or with status {@code 500 (Internal Server Error)} if the techCVDocs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TechCVDocs> updateTechCVDocs(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TechCVDocs techCVDocs
    ) throws URISyntaxException {
        log.debug("REST request to update TechCVDocs : {}, {}", id, techCVDocs);
        if (techCVDocs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, techCVDocs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!techCVDocsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        techCVDocs = techCVDocsRepository.save(techCVDocs);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, techCVDocs.getId().toString()))
            .body(techCVDocs);
    }

    /**
     * {@code PATCH  /tech-cv-docs/:id} : Partial updates given fields of an existing techCVDocs, field will ignore if it is null
     *
     * @param id the id of the techCVDocs to save.
     * @param techCVDocs the techCVDocs to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated techCVDocs,
     * or with status {@code 400 (Bad Request)} if the techCVDocs is not valid,
     * or with status {@code 404 (Not Found)} if the techCVDocs is not found,
     * or with status {@code 500 (Internal Server Error)} if the techCVDocs couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TechCVDocs> partialUpdateTechCVDocs(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TechCVDocs techCVDocs
    ) throws URISyntaxException {
        log.debug("REST request to partial update TechCVDocs partially : {}, {}", id, techCVDocs);
        if (techCVDocs.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, techCVDocs.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!techCVDocsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TechCVDocs> result = techCVDocsRepository
            .findById(techCVDocs.getId())
            .map(existingTechCVDocs -> {
                if (techCVDocs.getAttachedDoc() != null) {
                    existingTechCVDocs.setAttachedDoc(techCVDocs.getAttachedDoc());
                }
                if (techCVDocs.getAttachedDocContentType() != null) {
                    existingTechCVDocs.setAttachedDocContentType(techCVDocs.getAttachedDocContentType());
                }

                return existingTechCVDocs;
            })
            .map(techCVDocsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, techCVDocs.getId().toString())
        );
    }

    /**
     * {@code GET  /tech-cv-docs} : get all the techCVDocs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of techCVDocs in body.
     */
    @GetMapping("")
    public List<TechCVDocs> getAllTechCVDocs() {
        log.debug("REST request to get all TechCVDocs");
        return techCVDocsRepository.findAll();
    }

    /**
     * {@code GET  /tech-cv-docs/:id} : get the "id" techCVDocs.
     *
     * @param id the id of the techCVDocs to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the techCVDocs, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TechCVDocs> getTechCVDocs(@PathVariable("id") Long id) {
        log.debug("REST request to get TechCVDocs : {}", id);
        Optional<TechCVDocs> techCVDocs = techCVDocsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(techCVDocs);
    }

    /**
     * {@code DELETE  /tech-cv-docs/:id} : delete the "id" techCVDocs.
     *
     * @param id the id of the techCVDocs to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechCVDocs(@PathVariable("id") Long id) {
        log.debug("REST request to delete TechCVDocs : {}", id);
        techCVDocsRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
