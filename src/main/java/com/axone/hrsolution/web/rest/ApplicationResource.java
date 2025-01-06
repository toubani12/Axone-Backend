package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.Application;
import com.axone.hrsolution.repository.ApplicationRepository;
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
 * REST controller for managing {@link com.axone.hrsolution.domain.Application}.
 */
@RestController
@RequestMapping("/api/applications")
@Transactional
public class ApplicationResource {

    private final Logger log = LoggerFactory.getLogger(ApplicationResource.class);

    private static final String ENTITY_NAME = "application";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ApplicationRepository applicationRepository;

    public ApplicationResource(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    /**
     * {@code POST  /applications} : Create a new application.
     *
     * @param application the application to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new application, or with status {@code 400 (Bad Request)} if the application has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Application> createApplication(@Valid @RequestBody Application application) throws URISyntaxException {
        log.debug("REST request to save Application : {}", application);
        if (application.getId() != null) {
            throw new BadRequestAlertException("A new application cannot already have an ID", ENTITY_NAME, "idexists");
        }
        application = applicationRepository.save(application);
        return ResponseEntity.created(new URI("/api/applications/" + application.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, application.getId().toString()))
            .body(application);
    }

    /**
     * {@code PUT  /applications/:id} : Updates an existing application.
     *
     * @param id the id of the application to save.
     * @param application the application to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated application,
     * or with status {@code 400 (Bad Request)} if the application is not valid,
     * or with status {@code 500 (Internal Server Error)} if the application couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Application> updateApplication(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Application application
    ) throws URISyntaxException {
        log.debug("REST request to update Application : {}, {}", id, application);
        if (application.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, application.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        application = applicationRepository.save(application);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, application.getId().toString()))
            .body(application);
    }

    /**
     * {@code PATCH  /applications/:id} : Partial updates given fields of an existing application, field will ignore if it is null
     *
     * @param id the id of the application to save.
     * @param application the application to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated application,
     * or with status {@code 400 (Bad Request)} if the application is not valid,
     * or with status {@code 404 (Not Found)} if the application is not found,
     * or with status {@code 500 (Internal Server Error)} if the application couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Application> partialUpdateApplication(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Application application
    ) throws URISyntaxException {
        log.debug("REST request to partial update Application partially : {}, {}", id, application);
        if (application.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, application.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!applicationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Application> result = applicationRepository
            .findById(application.getId())
            .map(existingApplication -> {
                if (application.getTitle() != null) {
                    existingApplication.setTitle(application.getTitle());
                }
                if (application.getDescription() != null) {
                    existingApplication.setDescription(application.getDescription());
                }
                if (application.getNumberOfCandidates() != null) {
                    existingApplication.setNumberOfCandidates(application.getNumberOfCandidates());
                }
                if (application.getPaymentAmount() != null) {
                    existingApplication.setPaymentAmount(application.getPaymentAmount());
                }
                if (application.getRecruiterIncomeRate() != null) {
                    existingApplication.setRecruiterIncomeRate(application.getRecruiterIncomeRate());
                }
                if (application.getCandidateIncomeRate() != null) {
                    existingApplication.setCandidateIncomeRate(application.getCandidateIncomeRate());
                }
                if (application.getDeadline() != null) {
                    existingApplication.setDeadline(application.getDeadline());
                }
                if (application.getStatus() != null) {
                    existingApplication.setStatus(application.getStatus());
                }
                if (application.getCreatedAt() != null) {
                    existingApplication.setCreatedAt(application.getCreatedAt());
                }
                if (application.getOpenedAt() != null) {
                    existingApplication.setOpenedAt(application.getOpenedAt());
                }
                if (application.getClosedAt() != null) {
                    existingApplication.setClosedAt(application.getClosedAt());
                }
                if (application.getDoneAt() != null) {
                    existingApplication.setDoneAt(application.getDoneAt());
                }

                return existingApplication;
            })
            .map(applicationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, application.getId().toString())
        );
    }

    /**
     * {@code GET  /applications} : get all the applications.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of applications in body.
     */
    @GetMapping("")
    public List<Application> getAllApplications(
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("request-is-null".equals(filter)) {
            log.debug("REST request to get all Applications where request is null");
            return StreamSupport.stream(applicationRepository.findAll().spliterator(), false)
                .filter(application -> application.getRequest() == null)
                .toList();
        }
        log.debug("REST request to get all Applications");
        if (eagerload) {
            return applicationRepository.findAllWithEagerRelationships();
        } else {
            return applicationRepository.findAll();
        }
    }

    /**
     * {@code GET  /applications/:id} : get the "id" application.
     *
     * @param id the id of the application to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the application, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Application> getApplication(@PathVariable("id") Long id) {
        log.debug("REST request to get Application : {}", id);
        Optional<Application> application = applicationRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(application);
    }

    /**
     * {@code DELETE  /applications/:id} : delete the "id" application.
     *
     * @param id the id of the application to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable("id") Long id) {
        log.debug("REST request to delete Application : {}", id);
        applicationRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
