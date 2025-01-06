package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.TechCVSoftSkills;
import com.axone.hrsolution.repository.TechCVSoftSkillsRepository;
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
 * REST controller for managing {@link com.axone.hrsolution.domain.TechCVSoftSkills}.
 */
@RestController
@RequestMapping("/api/tech-cv-soft-skills")
@Transactional
public class TechCVSoftSkillsResource {

    private final Logger log = LoggerFactory.getLogger(TechCVSoftSkillsResource.class);

    private static final String ENTITY_NAME = "techCVSoftSkills";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TechCVSoftSkillsRepository techCVSoftSkillsRepository;

    public TechCVSoftSkillsResource(TechCVSoftSkillsRepository techCVSoftSkillsRepository) {
        this.techCVSoftSkillsRepository = techCVSoftSkillsRepository;
    }

    /**
     * {@code POST  /tech-cv-soft-skills} : Create a new techCVSoftSkills.
     *
     * @param techCVSoftSkills the techCVSoftSkills to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new techCVSoftSkills, or with status {@code 400 (Bad Request)} if the techCVSoftSkills has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TechCVSoftSkills> createTechCVSoftSkills(@Valid @RequestBody TechCVSoftSkills techCVSoftSkills)
        throws URISyntaxException {
        log.debug("REST request to save TechCVSoftSkills : {}", techCVSoftSkills);
        if (techCVSoftSkills.getId() != null) {
            throw new BadRequestAlertException("A new techCVSoftSkills cannot already have an ID", ENTITY_NAME, "idexists");
        }
        techCVSoftSkills = techCVSoftSkillsRepository.save(techCVSoftSkills);
        return ResponseEntity.created(new URI("/api/tech-cv-soft-skills/" + techCVSoftSkills.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, techCVSoftSkills.getId().toString()))
            .body(techCVSoftSkills);
    }

    /**
     * {@code PUT  /tech-cv-soft-skills/:id} : Updates an existing techCVSoftSkills.
     *
     * @param id the id of the techCVSoftSkills to save.
     * @param techCVSoftSkills the techCVSoftSkills to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated techCVSoftSkills,
     * or with status {@code 400 (Bad Request)} if the techCVSoftSkills is not valid,
     * or with status {@code 500 (Internal Server Error)} if the techCVSoftSkills couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TechCVSoftSkills> updateTechCVSoftSkills(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TechCVSoftSkills techCVSoftSkills
    ) throws URISyntaxException {
        log.debug("REST request to update TechCVSoftSkills : {}, {}", id, techCVSoftSkills);
        if (techCVSoftSkills.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, techCVSoftSkills.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!techCVSoftSkillsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        techCVSoftSkills = techCVSoftSkillsRepository.save(techCVSoftSkills);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, techCVSoftSkills.getId().toString()))
            .body(techCVSoftSkills);
    }

    /**
     * {@code PATCH  /tech-cv-soft-skills/:id} : Partial updates given fields of an existing techCVSoftSkills, field will ignore if it is null
     *
     * @param id the id of the techCVSoftSkills to save.
     * @param techCVSoftSkills the techCVSoftSkills to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated techCVSoftSkills,
     * or with status {@code 400 (Bad Request)} if the techCVSoftSkills is not valid,
     * or with status {@code 404 (Not Found)} if the techCVSoftSkills is not found,
     * or with status {@code 500 (Internal Server Error)} if the techCVSoftSkills couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TechCVSoftSkills> partialUpdateTechCVSoftSkills(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TechCVSoftSkills techCVSoftSkills
    ) throws URISyntaxException {
        log.debug("REST request to partial update TechCVSoftSkills partially : {}, {}", id, techCVSoftSkills);
        if (techCVSoftSkills.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, techCVSoftSkills.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!techCVSoftSkillsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TechCVSoftSkills> result = techCVSoftSkillsRepository
            .findById(techCVSoftSkills.getId())
            .map(existingTechCVSoftSkills -> {
                if (techCVSoftSkills.getSkills() != null) {
                    existingTechCVSoftSkills.setSkills(techCVSoftSkills.getSkills());
                }

                return existingTechCVSoftSkills;
            })
            .map(techCVSoftSkillsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, techCVSoftSkills.getId().toString())
        );
    }

    /**
     * {@code GET  /tech-cv-soft-skills} : get all the techCVSoftSkills.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of techCVSoftSkills in body.
     */
    @GetMapping("")
    public List<TechCVSoftSkills> getAllTechCVSoftSkills() {
        log.debug("REST request to get all TechCVSoftSkills");
        return techCVSoftSkillsRepository.findAll();
    }

    /**
     * {@code GET  /tech-cv-soft-skills/:id} : get the "id" techCVSoftSkills.
     *
     * @param id the id of the techCVSoftSkills to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the techCVSoftSkills, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TechCVSoftSkills> getTechCVSoftSkills(@PathVariable("id") Long id) {
        log.debug("REST request to get TechCVSoftSkills : {}", id);
        Optional<TechCVSoftSkills> techCVSoftSkills = techCVSoftSkillsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(techCVSoftSkills);
    }

    /**
     * {@code DELETE  /tech-cv-soft-skills/:id} : delete the "id" techCVSoftSkills.
     *
     * @param id the id of the techCVSoftSkills to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechCVSoftSkills(@PathVariable("id") Long id) {
        log.debug("REST request to delete TechCVSoftSkills : {}", id);
        techCVSoftSkillsRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
