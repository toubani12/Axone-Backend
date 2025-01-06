package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.NDA;
import com.axone.hrsolution.repository.NDARepository;
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
 * REST controller for managing {@link com.axone.hrsolution.domain.NDA}.
 */
@RestController
@RequestMapping("/api/ndas")
@Transactional
public class NDAResource {

    private final Logger log = LoggerFactory.getLogger(NDAResource.class);

    private static final String ENTITY_NAME = "nDA";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NDARepository nDARepository;

    public NDAResource(NDARepository nDARepository) {
        this.nDARepository = nDARepository;
    }

    /**
     * {@code POST  /ndas} : Create a new nDA.
     *
     * @param nDA the nDA to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nDA, or with status {@code 400 (Bad Request)} if the nDA has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NDA> createNDA(@Valid @RequestBody NDA nDA) throws URISyntaxException {
        log.debug("REST request to save NDA : {}", nDA);
        if (nDA.getId() != null) {
            throw new BadRequestAlertException("A new nDA cannot already have an ID", ENTITY_NAME, "idexists");
        }
        nDA = nDARepository.save(nDA);
        return ResponseEntity.created(new URI("/api/ndas/" + nDA.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, nDA.getId().toString()))
            .body(nDA);
    }

    /**
     * {@code PUT  /ndas/:id} : Updates an existing nDA.
     *
     * @param id the id of the nDA to save.
     * @param nDA the nDA to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nDA,
     * or with status {@code 400 (Bad Request)} if the nDA is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nDA couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NDA> updateNDA(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody NDA nDA)
        throws URISyntaxException {
        log.debug("REST request to update NDA : {}, {}", id, nDA);
        if (nDA.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nDA.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nDARepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        nDA = nDARepository.save(nDA);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nDA.getId().toString()))
            .body(nDA);
    }

    /**
     * {@code PATCH  /ndas/:id} : Partial updates given fields of an existing nDA, field will ignore if it is null
     *
     * @param id the id of the nDA to save.
     * @param nDA the nDA to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nDA,
     * or with status {@code 400 (Bad Request)} if the nDA is not valid,
     * or with status {@code 404 (Not Found)} if the nDA is not found,
     * or with status {@code 500 (Internal Server Error)} if the nDA couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NDA> partialUpdateNDA(@PathVariable(value = "id", required = false) final Long id, @NotNull @RequestBody NDA nDA)
        throws URISyntaxException {
        log.debug("REST request to partial update NDA partially : {}, {}", id, nDA);
        if (nDA.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nDA.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nDARepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NDA> result = nDARepository
            .findById(nDA.getId())
            .map(existingNDA -> {
                if (nDA.getDocument() != null) {
                    existingNDA.setDocument(nDA.getDocument());
                }
                if (nDA.getDocumentContentType() != null) {
                    existingNDA.setDocumentContentType(nDA.getDocumentContentType());
                }
                if (nDA.getStatus() != null) {
                    existingNDA.setStatus(nDA.getStatus());
                }
                if (nDA.getPeriod() != null) {
                    existingNDA.setPeriod(nDA.getPeriod());
                }

                return existingNDA;
            })
            .map(nDARepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nDA.getId().toString())
        );
    }

    /**
     * {@code GET  /ndas} : get all the nDAS.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nDAS in body.
     */
    @GetMapping("")
    public List<NDA> getAllNDAS(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all NDAS");
        if (eagerload) {
            return nDARepository.findAllWithEagerRelationships();
        } else {
            return nDARepository.findAll();
        }
    }

    /**
     * {@code GET  /ndas/:id} : get the "id" nDA.
     *
     * @param id the id of the nDA to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nDA, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NDA> getNDA(@PathVariable("id") Long id) {
        log.debug("REST request to get NDA : {}", id);
        Optional<NDA> nDA = nDARepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(nDA);
    }

    /**
     * {@code DELETE  /ndas/:id} : delete the "id" nDA.
     *
     * @param id the id of the nDA to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNDA(@PathVariable("id") Long id) {
        log.debug("REST request to delete NDA : {}", id);
        nDARepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
