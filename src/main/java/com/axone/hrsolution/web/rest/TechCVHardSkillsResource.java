package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.TechCVHardSkills;
import com.axone.hrsolution.repository.TechCVHardSkillsRepository;
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
 * REST controller for managing {@link com.axone.hrsolution.domain.TechCVHardSkills}.
 */
@RestController
@RequestMapping("/api/tech-cv-hard-skills")
@Transactional
public class TechCVHardSkillsResource {

    private final Logger log = LoggerFactory.getLogger(TechCVHardSkillsResource.class);

    private static final String ENTITY_NAME = "techCVHardSkills";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TechCVHardSkillsRepository techCVHardSkillsRepository;

    public TechCVHardSkillsResource(TechCVHardSkillsRepository techCVHardSkillsRepository) {
        this.techCVHardSkillsRepository = techCVHardSkillsRepository;
    }

    /**
     * {@code POST  /tech-cv-hard-skills} : Create a new techCVHardSkills.
     *
     * @param techCVHardSkills the techCVHardSkills to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new techCVHardSkills, or with status {@code 400 (Bad Request)} if the techCVHardSkills has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TechCVHardSkills> createTechCVHardSkills(@Valid @RequestBody TechCVHardSkills techCVHardSkills)
        throws URISyntaxException {
        log.debug("REST request to save TechCVHardSkills : {}", techCVHardSkills);
        if (techCVHardSkills.getId() != null) {
            throw new BadRequestAlertException("A new techCVHardSkills cannot already have an ID", ENTITY_NAME, "idexists");
        }
        techCVHardSkills = techCVHardSkillsRepository.save(techCVHardSkills);
        return ResponseEntity.created(new URI("/api/tech-cv-hard-skills/" + techCVHardSkills.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, techCVHardSkills.getId().toString()))
            .body(techCVHardSkills);
    }

    /**
     * {@code PUT  /tech-cv-hard-skills/:id} : Updates an existing techCVHardSkills.
     *
     * @param id the id of the techCVHardSkills to save.
     * @param techCVHardSkills the techCVHardSkills to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated techCVHardSkills,
     * or with status {@code 400 (Bad Request)} if the techCVHardSkills is not valid,
     * or with status {@code 500 (Internal Server Error)} if the techCVHardSkills couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TechCVHardSkills> updateTechCVHardSkills(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TechCVHardSkills techCVHardSkills
    ) throws URISyntaxException {
        log.debug("REST request to update TechCVHardSkills : {}, {}", id, techCVHardSkills);
        if (techCVHardSkills.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, techCVHardSkills.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!techCVHardSkillsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        techCVHardSkills = techCVHardSkillsRepository.save(techCVHardSkills);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, techCVHardSkills.getId().toString()))
            .body(techCVHardSkills);
    }

    /**
     * {@code PATCH  /tech-cv-hard-skills/:id} : Partial updates given fields of an existing techCVHardSkills, field will ignore if it is null
     *
     * @param id the id of the techCVHardSkills to save.
     * @param techCVHardSkills the techCVHardSkills to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated techCVHardSkills,
     * or with status {@code 400 (Bad Request)} if the techCVHardSkills is not valid,
     * or with status {@code 404 (Not Found)} if the techCVHardSkills is not found,
     * or with status {@code 500 (Internal Server Error)} if the techCVHardSkills couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TechCVHardSkills> partialUpdateTechCVHardSkills(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TechCVHardSkills techCVHardSkills
    ) throws URISyntaxException {
        log.debug("REST request to partial update TechCVHardSkills partially : {}, {}", id, techCVHardSkills);
        if (techCVHardSkills.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, techCVHardSkills.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!techCVHardSkillsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TechCVHardSkills> result = techCVHardSkillsRepository
            .findById(techCVHardSkills.getId())
            .map(existingTechCVHardSkills -> {
                if (techCVHardSkills.getSkills() != null) {
                    existingTechCVHardSkills.setSkills(techCVHardSkills.getSkills());
                }

                return existingTechCVHardSkills;
            })
            .map(techCVHardSkillsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, techCVHardSkills.getId().toString())
        );
    }

    /**
     * {@code GET  /tech-cv-hard-skills} : get all the techCVHardSkills.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of techCVHardSkills in body.
     */
    @GetMapping("")
    public List<TechCVHardSkills> getAllTechCVHardSkills() {
        log.debug("REST request to get all TechCVHardSkills");
        return techCVHardSkillsRepository.findAll();
    }

    /**
     * {@code GET  /tech-cv-hard-skills/:id} : get the "id" techCVHardSkills.
     *
     * @param id the id of the techCVHardSkills to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the techCVHardSkills, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TechCVHardSkills> getTechCVHardSkills(@PathVariable("id") Long id) {
        log.debug("REST request to get TechCVHardSkills : {}", id);
        Optional<TechCVHardSkills> techCVHardSkills = techCVHardSkillsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(techCVHardSkills);
    }

    /**
     * {@code DELETE  /tech-cv-hard-skills/:id} : delete the "id" techCVHardSkills.
     *
     * @param id the id of the techCVHardSkills to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechCVHardSkills(@PathVariable("id") Long id) {
        log.debug("REST request to delete TechCVHardSkills : {}", id);
        techCVHardSkillsRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
