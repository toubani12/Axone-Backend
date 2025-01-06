package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.AppTest;
import com.axone.hrsolution.repository.AppTestRepository;
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
 * REST controller for managing {@link com.axone.hrsolution.domain.AppTest}.
 */
@RestController
@RequestMapping("/api/app-tests")
@Transactional
public class AppTestResource {

    private final Logger log = LoggerFactory.getLogger(AppTestResource.class);

    private static final String ENTITY_NAME = "appTest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppTestRepository appTestRepository;

    public AppTestResource(AppTestRepository appTestRepository) {
        this.appTestRepository = appTestRepository;
    }

    /**
     * {@code POST  /app-tests} : Create a new appTest.
     *
     * @param appTest the appTest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appTest, or with status {@code 400 (Bad Request)} if the appTest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AppTest> createAppTest(@Valid @RequestBody AppTest appTest) throws URISyntaxException {
        log.debug("REST request to save AppTest : {}", appTest);
        if (appTest.getId() != null) {
            throw new BadRequestAlertException("A new appTest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        appTest = appTestRepository.save(appTest);
        return ResponseEntity.created(new URI("/api/app-tests/" + appTest.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, appTest.getId().toString()))
            .body(appTest);
    }

    /**
     * {@code PUT  /app-tests/:id} : Updates an existing appTest.
     *
     * @param id the id of the appTest to save.
     * @param appTest the appTest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appTest,
     * or with status {@code 400 (Bad Request)} if the appTest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appTest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AppTest> updateAppTest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppTest appTest
    ) throws URISyntaxException {
        log.debug("REST request to update AppTest : {}, {}", id, appTest);
        if (appTest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appTest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appTestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        appTest = appTestRepository.save(appTest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appTest.getId().toString()))
            .body(appTest);
    }

    /**
     * {@code PATCH  /app-tests/:id} : Partial updates given fields of an existing appTest, field will ignore if it is null
     *
     * @param id the id of the appTest to save.
     * @param appTest the appTest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appTest,
     * or with status {@code 400 (Bad Request)} if the appTest is not valid,
     * or with status {@code 404 (Not Found)} if the appTest is not found,
     * or with status {@code 500 (Internal Server Error)} if the appTest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppTest> partialUpdateAppTest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppTest appTest
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppTest partially : {}, {}", id, appTest);
        if (appTest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appTest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appTestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppTest> result = appTestRepository
            .findById(appTest.getId())
            .map(existingAppTest -> {
                if (appTest.getName() != null) {
                    existingAppTest.setName(appTest.getName());
                }
                if (appTest.getInvitationLink() != null) {
                    existingAppTest.setInvitationLink(appTest.getInvitationLink());
                }
                if (appTest.getTotalScore() != null) {
                    existingAppTest.setTotalScore(appTest.getTotalScore());
                }
                if (appTest.getStatus() != null) {
                    existingAppTest.setStatus(appTest.getStatus());
                }
                if (appTest.getCompleted() != null) {
                    existingAppTest.setCompleted(appTest.getCompleted());
                }
                if (appTest.getTestID() != null) {
                    existingAppTest.setTestID(appTest.getTestID());
                }
                if (appTest.getAlgorithm() != null) {
                    existingAppTest.setAlgorithm(appTest.getAlgorithm());
                }
                if (appTest.getIsCodeTest() != null) {
                    existingAppTest.setIsCodeTest(appTest.getIsCodeTest());
                }
                if (appTest.getDuration() != null) {
                    existingAppTest.setDuration(appTest.getDuration());
                }

                return existingAppTest;
            })
            .map(appTestRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appTest.getId().toString())
        );
    }

    /**
     * {@code GET  /app-tests} : get all the appTests.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appTests in body.
     */
    @GetMapping("")
    public List<AppTest> getAllAppTests(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all AppTests");
        if (eagerload) {
            return appTestRepository.findAllWithEagerRelationships();
        } else {
            return appTestRepository.findAll();
        }
    }

    /**
     * {@code GET  /app-tests/:id} : get the "id" appTest.
     *
     * @param id the id of the appTest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appTest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AppTest> getAppTest(@PathVariable("id") Long id) {
        log.debug("REST request to get AppTest : {}", id);
        Optional<AppTest> appTest = appTestRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(appTest);
    }

    /**
     * {@code DELETE  /app-tests/:id} : delete the "id" appTest.
     *
     * @param id the id of the appTest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppTest(@PathVariable("id") Long id) {
        log.debug("REST request to delete AppTest : {}", id);
        appTestRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
