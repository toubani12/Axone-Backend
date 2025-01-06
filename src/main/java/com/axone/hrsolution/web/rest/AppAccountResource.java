package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.AppAccount;
import com.axone.hrsolution.repository.AppAccountRepository;
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
 * REST controller for managing {@link com.axone.hrsolution.domain.AppAccount}.
 */
@RestController
@RequestMapping("/api/app-accounts")
@Transactional
public class AppAccountResource {

    private final Logger log = LoggerFactory.getLogger(AppAccountResource.class);

    private static final String ENTITY_NAME = "appAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AppAccountRepository appAccountRepository;

    public AppAccountResource(AppAccountRepository appAccountRepository) {
        this.appAccountRepository = appAccountRepository;
    }

    /**
     * {@code POST  /app-accounts} : Create a new appAccount.
     *
     * @param appAccount the appAccount to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new appAccount, or with status {@code 400 (Bad Request)} if the appAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AppAccount> createAppAccount(@Valid @RequestBody AppAccount appAccount) throws URISyntaxException {
        log.debug("REST request to save AppAccount : {}", appAccount);
        if (appAccount.getId() != null) {
            throw new BadRequestAlertException("A new appAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        appAccount = appAccountRepository.save(appAccount);
        return ResponseEntity.created(new URI("/api/app-accounts/" + appAccount.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, appAccount.getId().toString()))
            .body(appAccount);
    }

    /**
     * {@code PUT  /app-accounts/:id} : Updates an existing appAccount.
     *
     * @param id the id of the appAccount to save.
     * @param appAccount the appAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appAccount,
     * or with status {@code 400 (Bad Request)} if the appAccount is not valid,
     * or with status {@code 500 (Internal Server Error)} if the appAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AppAccount> updateAppAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AppAccount appAccount
    ) throws URISyntaxException {
        log.debug("REST request to update AppAccount : {}, {}", id, appAccount);
        if (appAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        appAccount = appAccountRepository.save(appAccount);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appAccount.getId().toString()))
            .body(appAccount);
    }

    /**
     * {@code PATCH  /app-accounts/:id} : Partial updates given fields of an existing appAccount, field will ignore if it is null
     *
     * @param id the id of the appAccount to save.
     * @param appAccount the appAccount to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated appAccount,
     * or with status {@code 400 (Bad Request)} if the appAccount is not valid,
     * or with status {@code 404 (Not Found)} if the appAccount is not found,
     * or with status {@code 500 (Internal Server Error)} if the appAccount couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AppAccount> partialUpdateAppAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AppAccount appAccount
    ) throws URISyntaxException {
        log.debug("REST request to partial update AppAccount partially : {}, {}", id, appAccount);
        if (appAccount.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, appAccount.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!appAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AppAccount> result = appAccountRepository
            .findById(appAccount.getId())
            .map(existingAppAccount -> {
                if (appAccount.getAccountNumber() != null) {
                    existingAppAccount.setAccountNumber(appAccount.getAccountNumber());
                }
                if (appAccount.getCardNumber() != null) {
                    existingAppAccount.setCardNumber(appAccount.getCardNumber());
                }
                if (appAccount.getEndDate() != null) {
                    existingAppAccount.setEndDate(appAccount.getEndDate());
                }
                if (appAccount.getCvv() != null) {
                    existingAppAccount.setCvv(appAccount.getCvv());
                }

                return existingAppAccount;
            })
            .map(appAccountRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, appAccount.getId().toString())
        );
    }

    /**
     * {@code GET  /app-accounts} : get all the appAccounts.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of appAccounts in body.
     */
    @GetMapping("")
    public List<AppAccount> getAllAppAccounts(
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("relatedwallet-is-null".equals(filter)) {
            log.debug("REST request to get all AppAccounts where relatedWallet is null");
            return StreamSupport.stream(appAccountRepository.findAll().spliterator(), false)
                .filter(appAccount -> appAccount.getRelatedWallet() == null)
                .toList();
        }
        log.debug("REST request to get all AppAccounts");
        if (eagerload) {
            return appAccountRepository.findAllWithEagerRelationships();
        } else {
            return appAccountRepository.findAll();
        }
    }

    /**
     * {@code GET  /app-accounts/:id} : get the "id" appAccount.
     *
     * @param id the id of the appAccount to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the appAccount, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AppAccount> getAppAccount(@PathVariable("id") Long id) {
        log.debug("REST request to get AppAccount : {}", id);
        Optional<AppAccount> appAccount = appAccountRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(appAccount);
    }

    /**
     * {@code DELETE  /app-accounts/:id} : delete the "id" appAccount.
     *
     * @param id the id of the appAccount to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppAccount(@PathVariable("id") Long id) {
        log.debug("REST request to delete AppAccount : {}", id);
        appAccountRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
