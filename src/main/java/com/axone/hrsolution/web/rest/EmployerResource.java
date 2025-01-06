package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.Employer;
import com.axone.hrsolution.repository.EmployerRepository;
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
 * REST controller for managing {@link com.axone.hrsolution.domain.Employer}.
 */
@RestController
@RequestMapping("/api/employers")
@Transactional
public class EmployerResource {

    private final Logger log = LoggerFactory.getLogger(EmployerResource.class);

    private static final String ENTITY_NAME = "employer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmployerRepository employerRepository;

    public EmployerResource(EmployerRepository employerRepository) {
        this.employerRepository = employerRepository;
    }

    /**
     * {@code POST  /employers} : Create a new employer.
     *
     * @param employer the employer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employer, or with status {@code 400 (Bad Request)} if the employer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Employer> createEmployer(@Valid @RequestBody Employer employer) throws URISyntaxException {
        log.debug("REST request to save Employer : {}", employer);
        if (employer.getId() != null) {
            throw new BadRequestAlertException("A new employer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        employer = employerRepository.save(employer);
        return ResponseEntity.created(new URI("/api/employers/" + employer.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, employer.getId().toString()))
            .body(employer);
    }

    /**
     * {@code PUT  /employers/:id} : Updates an existing employer.
     *
     * @param id the id of the employer to save.
     * @param employer the employer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employer,
     * or with status {@code 400 (Bad Request)} if the employer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Employer> updateEmployer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Employer employer
    ) throws URISyntaxException {
        log.debug("REST request to update Employer : {}, {}", id, employer);
        if (employer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        employer = employerRepository.save(employer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employer.getId().toString()))
            .body(employer);
    }

    /**
     * {@code PATCH  /employers/:id} : Partial updates given fields of an existing employer, field will ignore if it is null
     *
     * @param id the id of the employer to save.
     * @param employer the employer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employer,
     * or with status {@code 400 (Bad Request)} if the employer is not valid,
     * or with status {@code 404 (Not Found)} if the employer is not found,
     * or with status {@code 500 (Internal Server Error)} if the employer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Employer> partialUpdateEmployer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Employer employer
    ) throws URISyntaxException {
        log.debug("REST request to partial update Employer partially : {}, {}", id, employer);
        if (employer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Employer> result = employerRepository
            .findById(employer.getId())
            .map(existingEmployer -> {
                if (employer.getFirstName() != null) {
                    existingEmployer.setFirstName(employer.getFirstName());
                }
                if (employer.getLastName() != null) {
                    existingEmployer.setLastName(employer.getLastName());
                }
                if (employer.getProfileImage() != null) {
                    existingEmployer.setProfileImage(employer.getProfileImage());
                }
                if (employer.getProfileImageContentType() != null) {
                    existingEmployer.setProfileImageContentType(employer.getProfileImageContentType());
                }
                if (employer.getAddress() != null) {
                    existingEmployer.setAddress(employer.getAddress());
                }
                if (employer.getRole() != null) {
                    existingEmployer.setRole(employer.getRole());
                }
                if (employer.getStatus() != null) {
                    existingEmployer.setStatus(employer.getStatus());
                }
                if (employer.getName() != null) {
                    existingEmployer.setName(employer.getName());
                }
                if (employer.getLabel() != null) {
                    existingEmployer.setLabel(employer.getLabel());
                }

                return existingEmployer;
            })
            .map(employerRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employer.getId().toString())
        );
    }

    /**
     * {@code GET  /employers} : get all the employers.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employers in body.
     */
    @GetMapping("")
    public List<Employer> getAllEmployers(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all Employers");
        if (eagerload) {
            return employerRepository.findAllWithEagerRelationships();
        } else {
            return employerRepository.findAll();
        }
    }

    /**
     * {@code GET  /employers/:id} : get the "id" employer.
     *
     * @param id the id of the employer to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employer, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Employer> getEmployer(@PathVariable("id") Long id) {
        log.debug("REST request to get Employer : {}", id);
        Optional<Employer> employer = employerRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(employer);
    }

    /**
     * {@code DELETE  /employers/:id} : delete the "id" employer.
     *
     * @param id the id of the employer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployer(@PathVariable("id") Long id) {
        log.debug("REST request to delete Employer : {}", id);
        employerRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
