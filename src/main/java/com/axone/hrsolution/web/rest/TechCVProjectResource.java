package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.TechCVProject;
import com.axone.hrsolution.repository.TechCVProjectRepository;
import com.axone.hrsolution.service.ProcessService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.axone.hrsolution.domain.TechCVProject}.
 */
@RestController
@RequestMapping("/api/tech-cv-projects")
@Transactional
public class TechCVProjectResource {

    private final Logger log = LoggerFactory.getLogger(TechCVProjectResource.class);

    private static final String ENTITY_NAME = "techCVProject";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TechCVProjectRepository techCVProjectRepository;

    public TechCVProjectResource(TechCVProjectRepository techCVProjectRepository) {
        this.techCVProjectRepository = techCVProjectRepository;
    }

    /**
     * {@code POST  /tech-cv-projects} : Create a new techCVProject.
     *
     * @param techCVProject the techCVProject to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new techCVProject, or with status {@code 400 (Bad Request)} if the techCVProject has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TechCVProject> createTechCVProject(@Valid @RequestBody TechCVProject techCVProject) throws URISyntaxException {
        log.debug("REST request to save TechCVProject : {}", techCVProject);
        if (techCVProject.getId() != null) {
            throw new BadRequestAlertException("A new techCVProject cannot already have an ID", ENTITY_NAME, "idexists");
        }
        techCVProject = techCVProjectRepository.save(techCVProject);
        return ResponseEntity.created(new URI("/api/tech-cv-projects/" + techCVProject.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, techCVProject.getId().toString()))
            .body(techCVProject);
    }

    /**
     * {@code PUT  /tech-cv-projects/:id} : Updates an existing techCVProject.
     *
     * @param id the id of the techCVProject to save.
     * @param techCVProject the techCVProject to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated techCVProject,
     * or with status {@code 400 (Bad Request)} if the techCVProject is not valid,
     * or with status {@code 500 (Internal Server Error)} if the techCVProject couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TechCVProject> updateTechCVProject(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TechCVProject techCVProject
    ) throws URISyntaxException {
        log.debug("REST request to update TechCVProject : {}, {}", id, techCVProject);
        if (techCVProject.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, techCVProject.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!techCVProjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        techCVProject = techCVProjectRepository.save(techCVProject);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, techCVProject.getId().toString()))
            .body(techCVProject);
    }

    /**
     * {@code PATCH  /tech-cv-projects/:id} : Partial updates given fields of an existing techCVProject, field will ignore if it is null
     *
     * @param id the id of the techCVProject to save.
     * @param techCVProject the techCVProject to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated techCVProject,
     * or with status {@code 400 (Bad Request)} if the techCVProject is not valid,
     * or with status {@code 404 (Not Found)} if the techCVProject is not found,
     * or with status {@code 500 (Internal Server Error)} if the techCVProject couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TechCVProject> partialUpdateTechCVProject(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TechCVProject techCVProject
    ) throws URISyntaxException {
        log.debug("REST request to partial update TechCVProject partially : {}, {}", id, techCVProject);
        if (techCVProject.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, techCVProject.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!techCVProjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TechCVProject> result = techCVProjectRepository
            .findById(techCVProject.getId())
            .map(existingTechCVProject -> {
                if (techCVProject.getProject() != null) {
                    existingTechCVProject.setProject(techCVProject.getProject());
                }

                return existingTechCVProject;
            })
            .map(techCVProjectRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, techCVProject.getId().toString())
        );
    }

    /**
     * {@code GET  /tech-cv-projects} : get all the techCVProjects.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of techCVProjects in body.
     */
    @GetMapping("")
    public List<TechCVProject> getAllTechCVProjects() {
        log.debug("REST request to get all TechCVProjects");
        return techCVProjectRepository.findAll();
    }

    /**
     * {@code GET  /tech-cv-projects/:id} : get the "id" techCVProject.
     *
     * @param id the id of the techCVProject to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the techCVProject, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TechCVProject> getTechCVProject(@PathVariable("id") Long id) {
        log.debug("REST request to get TechCVProject : {}", id);
        Optional<TechCVProject> techCVProject = techCVProjectRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(techCVProject);
    }

    /**
     * {@code DELETE  /tech-cv-projects/:id} : delete the "id" techCVProject.
     *
     * @param id the id of the techCVProject to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTechCVProject(@PathVariable("id") Long id) {
        log.debug("REST request to delete TechCVProject : {}", id);
        techCVProjectRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    @RestController
    public static class ProcessController {

        @Autowired
        private ProcessService processService;

        @GetMapping("/start-process")
        public String startProcess() {
            processService.startProcess();
            return "Process started";
        }
    }
}
