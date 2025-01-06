package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.Domain;
import com.axone.hrsolution.repository.DomainRepository;
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
 * REST controller for managing {@link com.axone.hrsolution.domain.Domain}.
 */
@RestController
@RequestMapping("/api/domains")
@Transactional
public class DomainResource {

    private final Logger log = LoggerFactory.getLogger(DomainResource.class);

    private static final String ENTITY_NAME = "domain";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DomainRepository domainRepository;

    public DomainResource(DomainRepository domainRepository) {
        this.domainRepository = domainRepository;
    }

    /**
     * {@code POST  /domains} : Create a new domain.
     *
     * @param domain the domain to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new domain, or with status {@code 400 (Bad Request)} if the domain has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Domain> createDomain(@Valid @RequestBody Domain domain) throws URISyntaxException {
        log.debug("REST request to save Domain : {}", domain);
        if (domain.getId() != null) {
            throw new BadRequestAlertException("A new domain cannot already have an ID", ENTITY_NAME, "idexists");
        }
        domain = domainRepository.save(domain);
        return ResponseEntity.created(new URI("/api/domains/" + domain.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, domain.getId().toString()))
            .body(domain);
    }

    /**
     * {@code PUT  /domains/:id} : Updates an existing domain.
     *
     * @param id the id of the domain to save.
     * @param domain the domain to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated domain,
     * or with status {@code 400 (Bad Request)} if the domain is not valid,
     * or with status {@code 500 (Internal Server Error)} if the domain couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Domain> updateDomain(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Domain domain
    ) throws URISyntaxException {
        log.debug("REST request to update Domain : {}, {}", id, domain);
        if (domain.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, domain.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!domainRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        domain = domainRepository.save(domain);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, domain.getId().toString()))
            .body(domain);
    }

    /**
     * {@code PATCH  /domains/:id} : Partial updates given fields of an existing domain, field will ignore if it is null
     *
     * @param id the id of the domain to save.
     * @param domain the domain to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated domain,
     * or with status {@code 400 (Bad Request)} if the domain is not valid,
     * or with status {@code 404 (Not Found)} if the domain is not found,
     * or with status {@code 500 (Internal Server Error)} if the domain couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Domain> partialUpdateDomain(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Domain domain
    ) throws URISyntaxException {
        log.debug("REST request to partial update Domain partially : {}, {}", id, domain);
        if (domain.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, domain.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!domainRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Domain> result = domainRepository
            .findById(domain.getId())
            .map(existingDomain -> {
                if (domain.getName() != null) {
                    existingDomain.setName(domain.getName());
                }

                return existingDomain;
            })
            .map(domainRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, domain.getId().toString())
        );
    }

    /**
     * {@code GET  /domains} : get all the domains.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of domains in body.
     */
    @GetMapping("")
    public List<Domain> getAllDomains() {
        log.debug("REST request to get all Domains");
        return domainRepository.findAll();
    }

    /**
     * {@code GET  /domains/:id} : get the "id" domain.
     *
     * @param id the id of the domain to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the domain, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Domain> getDomain(@PathVariable("id") Long id) {
        log.debug("REST request to get Domain : {}", id);
        Optional<Domain> domain = domainRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(domain);
    }

    /**
     * {@code DELETE  /domains/:id} : delete the "id" domain.
     *
     * @param id the id of the domain to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDomain(@PathVariable("id") Long id) {
        log.debug("REST request to delete Domain : {}", id);
        domainRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
