package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.Candidate;
import com.axone.hrsolution.repository.CandidateRepository;
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
 * REST controller for managing {@link com.axone.hrsolution.domain.Candidate}.
 */
@RestController
@RequestMapping("/api/candidates")
@Transactional
public class CandidateResource {

    private final Logger log = LoggerFactory.getLogger(CandidateResource.class);

    private static final String ENTITY_NAME = "candidate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CandidateRepository candidateRepository;

    public CandidateResource(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    /**
     * {@code POST  /candidates} : Create a new candidate.
     *
     * @param candidate the candidate to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new candidate, or with status {@code 400 (Bad Request)} if the candidate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Candidate> createCandidate(@Valid @RequestBody Candidate candidate) throws URISyntaxException {
        log.debug("REST request to save Candidate : {}", candidate);
        if (candidate.getId() != null) {
            throw new BadRequestAlertException("A new candidate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        candidate = candidateRepository.save(candidate);
        return ResponseEntity.created(new URI("/api/candidates/" + candidate.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, candidate.getId().toString()))
            .body(candidate);
    }

    /**
     * {@code PUT  /candidates/:id} : Updates an existing candidate.
     *
     * @param id the id of the candidate to save.
     * @param candidate the candidate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated candidate,
     * or with status {@code 400 (Bad Request)} if the candidate is not valid,
     * or with status {@code 500 (Internal Server Error)} if the candidate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Candidate> updateCandidate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Candidate candidate
    ) throws URISyntaxException {
        log.debug("REST request to update Candidate : {}, {}", id, candidate);
        if (candidate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, candidate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!candidateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        candidate = candidateRepository.save(candidate);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, candidate.getId().toString()))
            .body(candidate);
    }

    /**
     * {@code PATCH  /candidates/:id} : Partial updates given fields of an existing candidate, field will ignore if it is null
     *
     * @param id the id of the candidate to save.
     * @param candidate the candidate to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated candidate,
     * or with status {@code 400 (Bad Request)} if the candidate is not valid,
     * or with status {@code 404 (Not Found)} if the candidate is not found,
     * or with status {@code 500 (Internal Server Error)} if the candidate couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Candidate> partialUpdateCandidate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Candidate candidate
    ) throws URISyntaxException {
        log.debug("REST request to partial update Candidate partially : {}, {}", id, candidate);
        if (candidate.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, candidate.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!candidateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Candidate> result = candidateRepository
            .findById(candidate.getId())
            .map(existingCandidate -> {
                if (candidate.getFirstName() != null) {
                    existingCandidate.setFirstName(candidate.getFirstName());
                }
                if (candidate.getLastName() != null) {
                    existingCandidate.setLastName(candidate.getLastName());
                }
                if (candidate.getLinkedinUrl() != null) {
                    existingCandidate.setLinkedinUrl(candidate.getLinkedinUrl());
                }
                if (candidate.getFullName() != null) {
                    existingCandidate.setFullName(candidate.getFullName());
                }
                if (candidate.getYearsOfExperience() != null) {
                    existingCandidate.setYearsOfExperience(candidate.getYearsOfExperience());
                }
                if (candidate.getCurrentSalary() != null) {
                    existingCandidate.setCurrentSalary(candidate.getCurrentSalary());
                }
                if (candidate.getDesiredSalary() != null) {
                    existingCandidate.setDesiredSalary(candidate.getDesiredSalary());
                }
                if (candidate.getHasContract() != null) {
                    existingCandidate.setHasContract(candidate.getHasContract());
                }
                if (candidate.getHired() != null) {
                    existingCandidate.setHired(candidate.getHired());
                }
                if (candidate.getRate() != null) {
                    existingCandidate.setRate(candidate.getRate());
                }

                return existingCandidate;
            })
            .map(candidateRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, candidate.getId().toString())
        );
    }

    /**
     * {@code GET  /candidates} : get all the candidates.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of candidates in body.
     */
    @GetMapping("")
    public List<Candidate> getAllCandidates(
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("contract-is-null".equals(filter)) {
            log.debug("REST request to get all Candidates where contract is null");
            return StreamSupport.stream(candidateRepository.findAll().spliterator(), false)
                .filter(candidate -> candidate.getContract() == null)
                .toList();
        }
        log.debug("REST request to get all Candidates");
        if (eagerload) {
            return candidateRepository.findAllWithEagerRelationships();
        } else {
            return candidateRepository.findAll();
        }
    }

    /**
     * {@code GET  /candidates/:id} : get the "id" candidate.
     *
     * @param id the id of the candidate to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the candidate, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getCandidate(@PathVariable("id") Long id) {
        log.debug("REST request to get Candidate : {}", id);
        Optional<Candidate> candidate = candidateRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(candidate);
    }

    /**
     * {@code DELETE  /candidates/:id} : delete the "id" candidate.
     *
     * @param id the id of the candidate to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable("id") Long id) {
        log.debug("REST request to delete Candidate : {}", id);
        candidateRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
