package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.TechCVAchievement;
import com.axone.hrsolution.repository.TechCVAchievementRepository;
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
 * REST controller for managing {@link com.axone.hrsolution.domain.TechCVAchievement}.
 */
@RestController
@RequestMapping("/api/tech-cv-achievements")
@Transactional
public class TechCVAchievementResource {

    private final Logger log = LoggerFactory.getLogger(TechCVAchievementResource.class);

    private static final String ENTITY_NAME = "techCVAchievement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TechCVAchievementRepository techCVAchievementRepository;

    public TechCVAchievementResource(TechCVAchievementRepository techCVAchievementRepository) {
        this.techCVAchievementRepository = techCVAchievementRepository;
    }

    /**
     * {@code POST  /tech-cv-achievements} : Create a new techCVAchievement.
     *
     * @param techCVAchievement the techCVAchievement to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new techCVAchievement, or with status {@code 400 (Bad Request)} if the techCVAchievement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TechCVAchievement> createTechCVAchievement(@Valid @RequestBody TechCVAchievement techCVAchievement)
        throws URISyntaxException {
        log.debug("REST request to save TechCVAchievement : {}", techCVAchievement);
        if (techCVAchievement.getId() != null) {
            throw new BadRequestAlertException("A new techCVAchievement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        techCVAchievement = techCVAchievementRepository.save(techCVAchievement);
        return ResponseEntity.created(new URI("/api/tech-cv-achievements/" + techCVAchievement.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, techCVAchievement.getId().toString()))
            .body(techCVAchievement);
    }

    /**
     * {@code PUT  /tech-cv-achievements/:id} : Updates an existing techCVAchievement.
     *
     * @param id the id of the techCVAchievement to save.
     * @param techCVAchievement the techCVAchievement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated techCVAchievement,
     * or with status {@code 400 (Bad Request)} if the techCVAchievement is not valid,
     * or with status {@code 500 (Internal Server Error)} if the techCVAchievement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TechCVAchievement> updateTechCVAchievement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TechCVAchievement techCVAchievement
    ) throws URISyntaxException {
        log.debug("REST request to update TechCVAchievement : {}, {}", id, techCVAchievement);
        if (techCVAchievement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, techCVAchievement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!techCVAchievementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        techCVAchievement = techCVAchievementRepository.save(techCVAchievement);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, techCVAchievement.getId().toString()))
            .body(techCVAchievement);
    }

    /**
     * {@code PATCH  /tech-cv-achievements/:id} : Partial updates given fields of an existing techCVAchievement, field will ignore if it is null
     *
     * @param id the id of the techCVAchievement to save.
     * @param techCVAchievement the techCVAchievement to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated techCVAchievement,
     * or with status {@code 400 (Bad Request)} if the techCVAchievement is not valid,
     * or with status {@code 404 (Not Found)} if the techCVAchievement is not found,
     * or with status {@code 500 (Internal Server Error)} if the techCVAchievement couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TechCVAchievement> partialUpdateTechCVAchievement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TechCVAchievement techCVAchievement
    ) throws URISyntaxException {
        log.debug("REST request to partial update TechCVAchievement partially : {}, {}", id, techCVAchievement);
        if (techCVAchievement.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, techCVAchievement.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!techCVAchievementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TechCVAchievement> result = techCVAchievementRepository
            .findById(techCVAchievement.getId())
            .map(existingTechCVAchievement -> {
                if (techCVAchievement.getAchievement() != null) {
                    existingTechCVAchievement.setAchievement(techCVAchievement.getAchievement());
                }

                return existingTechCVAchievement;
            })
            .map(techCVAchievementRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, techCVAchievement.getId().toString())
        );
    }

    /**
     * {@code GET  /tech-cv-achievements} : get all the techCVAchievements.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of techCVAchievements in body.
     */
    @GetMapping("")
    public List<TechCVAchievement> getAllTechCVAchievements() {
        log.debug("REST request to get all TechCVAchievements");
        return techCVAchievementRepository.findAll();
    }

    /**
     * {@code GET  /tech-cv-achievements/:id} : get the "id" techCVAchievement.
     *
     * @param id the id of the techCVAchievement to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the techCVAchievement, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TechCVAchievement> getTechCVAchievement(@PathVariable("id") Long id) {
        log.debug("REST request to get TechCVAchievement : {}", id);
        Optional<TechCVAchievement> techCVAchievement = techCVAchievementRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(techCVAchievement);
    }

    /**
     * {@code DELETE  /tech-cv-achievements/:id} : delete the "id" techCVAchievement.
     *
     * @param id the id of the techCVAchievement to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechCVAchievement(@PathVariable("id") Long id) {
        log.debug("REST request to delete TechCVAchievement : {}", id);
        techCVAchievementRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
