package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.AppAccountType;
import com.axone.hrsolution.repository.AppAccountTypeRepository;
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
 * REST controller for managing {@link com.axone.hrsolution.domain.AppAccountType}.
 */
@RestController
@RequestMapping("/api/app-account-types")
@Transactional
public class AppAccountTypeResource {

    private final Logger log = LoggerFactory.getLogger(AppAccountTypeResource.class);

    private static final String ENTITY_NAME = "appAccountType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppAccountTypeRepository appAccountTypeRepository;

    public AppAccountTypeResource(AppAccountTypeRepository appAccountTypeRepository) {
        this.appAccountTypeRepository = appAccountTypeRepository;
    }

    /**
     * {@code POST  /app-account-types} : Create a new appAccountType.
     *
     * @param appAccountType the appAccountType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appAccountType, or with status {@code 400 (Bad Request)} if the appAccountType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AppAccountType> createAppAccountType(@Valid @RequestBody AppAccountType appAccountType)
        throws URISyntaxException {
        log.debug("REST request to save AppAccountType : {}", appAccountType);
        if (appAccountType.getId() != null) {
            throw new BadRequestAlertException("A new appAccountType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        appAccountType = appAccountTypeRepository.save(appAccountType);
        return ResponseEntity.created(new URI("/api/app-account-types/" + appAccountType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, appAccountType.getId().toString()))
            .body(appAccountType);
    }

    /**
     * {@code PUT  /app-account-types/:id} : Updates an existing appAccountType.
     *
     * @param id the id of the appAccountType to save.
     * @param appAccountType the appAccountType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appAccountType,
     * or with status {@code 400 (Bad Request)} if the appAccountType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appAccountType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AppAccountType> updateAppAccountType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppAccountType appAccountType
    ) throws URISyntaxException {
        log.debug("REST request to update AppAccountType : {}, {}", id, appAccountType);
        if (appAccountType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appAccountType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appAccountTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        appAccountType = appAccountTypeRepository.save(appAccountType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appAccountType.getId().toString()))
            .body(appAccountType);
    }

    /**
     * {@code PATCH  /app-account-types/:id} : Partial updates given fields of an existing appAccountType, field will ignore if it is null
     *
     * @param id the id of the appAccountType to save.
     * @param appAccountType the appAccountType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appAccountType,
     * or with status {@code 400 (Bad Request)} if the appAccountType is not valid,
     * or with status {@code 404 (Not Found)} if the appAccountType is not found,
     * or with status {@code 500 (Internal Server Error)} if the appAccountType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppAccountType> partialUpdateAppAccountType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppAccountType appAccountType
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppAccountType partially : {}, {}", id, appAccountType);
        if (appAccountType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appAccountType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appAccountTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppAccountType> result = appAccountTypeRepository
            .findById(appAccountType.getId())
            .map(existingAppAccountType -> {
                if (appAccountType.getType() != null) {
                    existingAppAccountType.setType(appAccountType.getType());
                }

                return existingAppAccountType;
            })
            .map(appAccountTypeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appAccountType.getId().toString())
        );
    }

    /**
     * {@code GET  /app-account-types} : get all the appAccountTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appAccountTypes in body.
     */
    @GetMapping("")
    public List<AppAccountType> getAllAppAccountTypes() {
        log.debug("REST request to get all AppAccountTypes");
        return appAccountTypeRepository.findAll();
    }

    /**
     * {@code GET  /app-account-types/:id} : get the "id" appAccountType.
     *
     * @param id the id of the appAccountType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appAccountType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AppAccountType> getAppAccountType(@PathVariable("id") Long id) {
        log.debug("REST request to get AppAccountType : {}", id);
        Optional<AppAccountType> appAccountType = appAccountTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(appAccountType);
    }

    /**
     * {@code DELETE  /app-account-types/:id} : delete the "id" appAccountType.
     *
     * @param id the id of the appAccountType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppAccountType(@PathVariable("id") Long id) {
        log.debug("REST request to delete AppAccountType : {}", id);
        appAccountTypeRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
