package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.Recruiter;
import com.axone.hrsolution.repository.RecruiterRepository;
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
 * REST controller for managing {@link com.axone.hrsolution.domain.Recruiter}.
 */
@RestController
@RequestMapping("/api/recruiters")
@Transactional
public class RecruiterResource {

    private final Logger log = LoggerFactory.getLogger(RecruiterResource.class);

    private static final String ENTITY_NAME = "recruiter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecruiterRepository recruiterRepository;

    public RecruiterResource(RecruiterRepository recruiterRepository) {
        this.recruiterRepository = recruiterRepository;
    }

    /**
     * {@code POST  /recruiters} : Create a new recruiter.
     *
     * @param recruiter the recruiter to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recruiter, or with status {@code 400 (Bad Request)} if the recruiter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Recruiter> createRecruiter(@Valid @RequestBody Recruiter recruiter) throws URISyntaxException {
        log.debug("REST request to save Recruiter : {}", recruiter);
        if (recruiter.getId() != null) {
            throw new BadRequestAlertException("A new recruiter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        recruiter = recruiterRepository.save(recruiter);
        return ResponseEntity.created(new URI("/api/recruiters/" + recruiter.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, recruiter.getId().toString()))
            .body(recruiter);
    }

    /**
     * {@code PUT  /recruiters/:id} : Updates an existing recruiter.
     *
     * @param id the id of the recruiter to save.
     * @param recruiter the recruiter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recruiter,
     * or with status {@code 400 (Bad Request)} if the recruiter is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recruiter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Recruiter> updateRecruiter(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Recruiter recruiter
    ) throws URISyntaxException {
        log.debug("REST request to update Recruiter : {}, {}", id, recruiter);
        if (recruiter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recruiter.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recruiterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        recruiter = recruiterRepository.save(recruiter);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recruiter.getId().toString()))
            .body(recruiter);
    }

    /**
     * {@code PATCH  /recruiters/:id} : Partial updates given fields of an existing recruiter, field will ignore if it is null
     *
     * @param id the id of the recruiter to save.
     * @param recruiter the recruiter to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recruiter,
     * or with status {@code 400 (Bad Request)} if the recruiter is not valid,
     * or with status {@code 404 (Not Found)} if the recruiter is not found,
     * or with status {@code 500 (Internal Server Error)} if the recruiter couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Recruiter> partialUpdateRecruiter(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Recruiter recruiter
    ) throws URISyntaxException {
        log.debug("REST request to partial update Recruiter partially : {}, {}", id, recruiter);
        if (recruiter.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recruiter.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recruiterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Recruiter> result = recruiterRepository
            .findById(recruiter.getId())
            .map(existingRecruiter -> {
                if (recruiter.getFirstName() != null) {
                    existingRecruiter.setFirstName(recruiter.getFirstName());
                }
                if (recruiter.getLastName() != null) {
                    existingRecruiter.setLastName(recruiter.getLastName());
                }
                if (recruiter.getProfileImage() != null) {
                    existingRecruiter.setProfileImage(recruiter.getProfileImage());
                }
                if (recruiter.getProfileImageContentType() != null) {
                    existingRecruiter.setProfileImageContentType(recruiter.getProfileImageContentType());
                }
                if (recruiter.getAddress() != null) {
                    existingRecruiter.setAddress(recruiter.getAddress());
                }
                if (recruiter.getRole() != null) {
                    existingRecruiter.setRole(recruiter.getRole());
                }
                if (recruiter.getStatus() != null) {
                    existingRecruiter.setStatus(recruiter.getStatus());
                }
                if (recruiter.getName() != null) {
                    existingRecruiter.setName(recruiter.getName());
                }
                if (recruiter.getLabel() != null) {
                    existingRecruiter.setLabel(recruiter.getLabel());
                }
                if (recruiter.getLinkedinUrl() != null) {
                    existingRecruiter.setLinkedinUrl(recruiter.getLinkedinUrl());
                }
                if (recruiter.getApprovedExperience() != null) {
                    existingRecruiter.setApprovedExperience(recruiter.getApprovedExperience());
                }
                if (recruiter.getScore() != null) {
                    existingRecruiter.setScore(recruiter.getScore());
                }

                return existingRecruiter;
            })
            .map(recruiterRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, recruiter.getId().toString())
        );
    }

    /**
     * {@code GET  /recruiters} : get all the recruiters.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recruiters in body.
     */
    @GetMapping("")
    public List<Recruiter> getAllRecruiters(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all Recruiters");
        if (eagerload) {
            return recruiterRepository.findAllWithEagerRelationships();
        } else {
            return recruiterRepository.findAll();
        }
    }

    /**
     * {@code GET  /recruiters/:id} : get the "id" recruiter.
     *
     * @param id the id of the recruiter to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recruiter, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Recruiter> getRecruiter(@PathVariable("id") Long id) {
        log.debug("REST request to get Recruiter : {}", id);
        Optional<Recruiter> recruiter = recruiterRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(recruiter);
    }

    /**
     * {@code DELETE  /recruiters/:id} : delete the "id" recruiter.
     *
     * @param id the id of the recruiter to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRecruiter(@PathVariable("id") Long id) {
        log.debug("REST request to delete Recruiter : {}", id);
        recruiterRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
