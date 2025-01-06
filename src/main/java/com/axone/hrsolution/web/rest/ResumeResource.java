package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.Resume;
import com.axone.hrsolution.repository.ResumeRepository;
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
 * REST controller for managing {@link com.axone.hrsolution.domain.Resume}.
 */
@RestController
@RequestMapping("/api/resumes")
@Transactional
public class ResumeResource {

    private final Logger log = LoggerFactory.getLogger(ResumeResource.class);

    private static final String ENTITY_NAME = "resume";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResumeRepository resumeRepository;

    public ResumeResource(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    /**
     * {@code POST  /resumes} : Create a new resume.
     *
     * @param resume the resume to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resume, or with status {@code 400 (Bad Request)} if the resume has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Resume> createResume(@Valid @RequestBody Resume resume) throws URISyntaxException {
        log.debug("REST request to save Resume : {}", resume);
        if (resume.getId() != null) {
            throw new BadRequestAlertException("A new resume cannot already have an ID", ENTITY_NAME, "idexists");
        }
        resume = resumeRepository.save(resume);
        return ResponseEntity.created(new URI("/api/resumes/" + resume.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, resume.getId().toString()))
            .body(resume);
    }

    /**
     * {@code PUT  /resumes/:id} : Updates an existing resume.
     *
     * @param id the id of the resume to save.
     * @param resume the resume to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resume,
     * or with status {@code 400 (Bad Request)} if the resume is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resume couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Resume> updateResume(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Resume resume
    ) throws URISyntaxException {
        log.debug("REST request to update Resume : {}, {}", id, resume);
        if (resume.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resume.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resumeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        resume = resumeRepository.save(resume);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resume.getId().toString()))
            .body(resume);
    }

    /**
     * {@code PATCH  /resumes/:id} : Partial updates given fields of an existing resume, field will ignore if it is null
     *
     * @param id the id of the resume to save.
     * @param resume the resume to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resume,
     * or with status {@code 400 (Bad Request)} if the resume is not valid,
     * or with status {@code 404 (Not Found)} if the resume is not found,
     * or with status {@code 500 (Internal Server Error)} if the resume couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Resume> partialUpdateResume(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Resume resume
    ) throws URISyntaxException {
        log.debug("REST request to partial update Resume partially : {}, {}", id, resume);
        if (resume.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, resume.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!resumeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Resume> result = resumeRepository
            .findById(resume.getId())
            .map(existingResume -> {
                if (resume.getResume() != null) {
                    existingResume.setResume(resume.getResume());
                }
                if (resume.getResumeContentType() != null) {
                    existingResume.setResumeContentType(resume.getResumeContentType());
                }

                return existingResume;
            })
            .map(resumeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, resume.getId().toString())
        );
    }

    /**
     * {@code GET  /resumes} : get all the resumes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resumes in body.
     */
    @GetMapping("")
    public List<Resume> getAllResumes() {
        log.debug("REST request to get all Resumes");
        return resumeRepository.findAll();
    }

    /**
     * {@code GET  /resumes/:id} : get the "id" resume.
     *
     * @param id the id of the resume to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resume, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Resume> getResume(@PathVariable("id") Long id) {
        log.debug("REST request to get Resume : {}", id);
        Optional<Resume> resume = resumeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(resume);
    }

    /**
     * {@code DELETE  /resumes/:id} : delete the "id" resume.
     *
     * @param id the id of the resume to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResume(@PathVariable("id") Long id) {
        log.debug("REST request to delete Resume : {}", id);
        resumeRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
