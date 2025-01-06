package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.AppTestType;
import com.axone.hrsolution.repository.AppTestTypeRepository;
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
 * REST controller for managing {@link com.axone.hrsolution.domain.AppTestType}.
 */
@RestController
@RequestMapping("/api/app-test-types")
@Transactional
public class AppTestTypeResource {

    private final Logger log = LoggerFactory.getLogger(AppTestTypeResource.class);

    private static final String ENTITY_NAME = "appTestType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppTestTypeRepository appTestTypeRepository;

    public AppTestTypeResource(AppTestTypeRepository appTestTypeRepository) {
        this.appTestTypeRepository = appTestTypeRepository;
    }

    /**
     * {@code POST  /app-test-types} : Create a new appTestType.
     *
     * @param appTestType the appTestType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appTestType, or with status {@code 400 (Bad Request)} if the appTestType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AppTestType> createAppTestType(@Valid @RequestBody AppTestType appTestType) throws URISyntaxException {
        log.debug("REST request to save AppTestType : {}", appTestType);
        if (appTestType.getId() != null) {
            throw new BadRequestAlertException("A new appTestType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        appTestType = appTestTypeRepository.save(appTestType);
        return ResponseEntity.created(new URI("/api/app-test-types/" + appTestType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, appTestType.getId().toString()))
            .body(appTestType);
    }

    /**
     * {@code PUT  /app-test-types/:id} : Updates an existing appTestType.
     *
     * @param id the id of the appTestType to save.
     * @param appTestType the appTestType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appTestType,
     * or with status {@code 400 (Bad Request)} if the appTestType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appTestType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AppTestType> updateAppTestType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppTestType appTestType
    ) throws URISyntaxException {
        log.debug("REST request to update AppTestType : {}, {}", id, appTestType);
        if (appTestType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appTestType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appTestTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        appTestType = appTestTypeRepository.save(appTestType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appTestType.getId().toString()))
            .body(appTestType);
    }

    /**
     * {@code PATCH  /app-test-types/:id} : Partial updates given fields of an existing appTestType, field will ignore if it is null
     *
     * @param id the id of the appTestType to save.
     * @param appTestType the appTestType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appTestType,
     * or with status {@code 400 (Bad Request)} if the appTestType is not valid,
     * or with status {@code 404 (Not Found)} if the appTestType is not found,
     * or with status {@code 500 (Internal Server Error)} if the appTestType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppTestType> partialUpdateAppTestType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppTestType appTestType
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppTestType partially : {}, {}", id, appTestType);
        if (appTestType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appTestType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appTestTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppTestType> result = appTestTypeRepository
            .findById(appTestType.getId())
            .map(existingAppTestType -> {
                if (appTestType.getType() != null) {
                    existingAppTestType.setType(appTestType.getType());
                }

                return existingAppTestType;
            })
            .map(appTestTypeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appTestType.getId().toString())
        );
    }

    /**
     * {@code GET  /app-test-types} : get all the appTestTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appTestTypes in body.
     */
    @GetMapping("")
    public List<AppTestType> getAllAppTestTypes() {
        log.debug("REST request to get all AppTestTypes");
        return appTestTypeRepository.findAll();
    }

    /**
     * {@code GET  /app-test-types/:id} : get the "id" appTestType.
     *
     * @param id the id of the appTestType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appTestType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AppTestType> getAppTestType(@PathVariable("id") Long id) {
        log.debug("REST request to get AppTestType : {}", id);
        Optional<AppTestType> appTestType = appTestTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(appTestType);
    }

    /**
     * {@code DELETE  /app-test-types/:id} : delete the "id" appTestType.
     *
     * @param id the id of the appTestType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppTestType(@PathVariable("id") Long id) {
        log.debug("REST request to delete AppTestType : {}", id);
        appTestTypeRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
