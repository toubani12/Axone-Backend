package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.Provider;
import com.axone.hrsolution.repository.ProviderRepository;
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
 * REST controller for managing {@link com.axone.hrsolution.domain.Provider}.
 */
@RestController
@RequestMapping("/api/providers")
@Transactional
public class ProviderResource {

    private final Logger log = LoggerFactory.getLogger(ProviderResource.class);

    private static final String ENTITY_NAME = "provider";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProviderRepository providerRepository;

    public ProviderResource(ProviderRepository providerRepository) {
        this.providerRepository = providerRepository;
    }

    /**
     * {@code POST  /providers} : Create a new provider.
     *
     * @param provider the provider to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new provider, or with status {@code 400 (Bad Request)} if the provider has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Provider> createProvider(@Valid @RequestBody Provider provider) throws URISyntaxException {
        log.debug("REST request to save Provider : {}", provider);
        if (provider.getId() != null) {
            throw new BadRequestAlertException("A new provider cannot already have an ID", ENTITY_NAME, "idexists");
        }
        provider = providerRepository.save(provider);
        return ResponseEntity.created(new URI("/api/providers/" + provider.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, provider.getId().toString()))
            .body(provider);
    }

    /**
     * {@code PUT  /providers/:id} : Updates an existing provider.
     *
     * @param id the id of the provider to save.
     * @param provider the provider to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated provider,
     * or with status {@code 400 (Bad Request)} if the provider is not valid,
     * or with status {@code 500 (Internal Server Error)} if the provider couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Provider> updateProvider(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Provider provider
    ) throws URISyntaxException {
        log.debug("REST request to update Provider : {}, {}", id, provider);
        if (provider.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, provider.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!providerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        provider = providerRepository.save(provider);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, provider.getId().toString()))
            .body(provider);
    }

    /**
     * {@code PATCH  /providers/:id} : Partial updates given fields of an existing provider, field will ignore if it is null
     *
     * @param id the id of the provider to save.
     * @param provider the provider to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated provider,
     * or with status {@code 400 (Bad Request)} if the provider is not valid,
     * or with status {@code 404 (Not Found)} if the provider is not found,
     * or with status {@code 500 (Internal Server Error)} if the provider couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Provider> partialUpdateProvider(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Provider provider
    ) throws URISyntaxException {
        log.debug("REST request to partial update Provider partially : {}, {}", id, provider);
        if (provider.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, provider.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!providerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Provider> result = providerRepository
            .findById(provider.getId())
            .map(existingProvider -> {
                if (provider.getName() != null) {
                    existingProvider.setName(provider.getName());
                }
                if (provider.getCountry() != null) {
                    existingProvider.setCountry(provider.getCountry());
                }

                return existingProvider;
            })
            .map(providerRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, provider.getId().toString())
        );
    }

    /**
     * {@code GET  /providers} : get all the providers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of providers in body.
     */
    @GetMapping("")
    public List<Provider> getAllProviders() {
        log.debug("REST request to get all Providers");
        return providerRepository.findAll();
    }

    /**
     * {@code GET  /providers/:id} : get the "id" provider.
     *
     * @param id the id of the provider to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the provider, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Provider> getProvider(@PathVariable("id") Long id) {
        log.debug("REST request to get Provider : {}", id);
        Optional<Provider> provider = providerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(provider);
    }

    /**
     * {@code DELETE  /providers/:id} : delete the "id" provider.
     *
     * @param id the id of the provider to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvider(@PathVariable("id") Long id) {
        log.debug("REST request to delete Provider : {}", id);
        providerRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
