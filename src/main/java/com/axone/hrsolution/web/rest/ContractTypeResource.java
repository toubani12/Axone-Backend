package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.ContractType;
import com.axone.hrsolution.repository.ContractTypeRepository;
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
 * REST controller for managing {@link com.axone.hrsolution.domain.ContractType}.
 */
@RestController
@RequestMapping("/api/contract-types")
@Transactional
public class ContractTypeResource {

    private final Logger log = LoggerFactory.getLogger(ContractTypeResource.class);

    private static final String ENTITY_NAME = "contractType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContractTypeRepository contractTypeRepository;

    public ContractTypeResource(ContractTypeRepository contractTypeRepository) {
        this.contractTypeRepository = contractTypeRepository;
    }

    /**
     * {@code POST  /contract-types} : Create a new contractType.
     *
     * @param contractType the contractType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contractType, or with status {@code 400 (Bad Request)} if the contractType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContractType> createContractType(@Valid @RequestBody ContractType contractType) throws URISyntaxException {
        log.debug("REST request to save ContractType : {}", contractType);
        if (contractType.getId() != null) {
            throw new BadRequestAlertException("A new contractType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contractType = contractTypeRepository.save(contractType);
        return ResponseEntity.created(new URI("/api/contract-types/" + contractType.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, contractType.getId().toString()))
            .body(contractType);
    }

    /**
     * {@code PUT  /contract-types/:id} : Updates an existing contractType.
     *
     * @param id the id of the contractType to save.
     * @param contractType the contractType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractType,
     * or with status {@code 400 (Bad Request)} if the contractType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contractType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContractType> updateContractType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContractType contractType
    ) throws URISyntaxException {
        log.debug("REST request to update ContractType : {}, {}", id, contractType);
        if (contractType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contractType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contractType = contractTypeRepository.save(contractType);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contractType.getId().toString()))
            .body(contractType);
    }

    /**
     * {@code PATCH  /contract-types/:id} : Partial updates given fields of an existing contractType, field will ignore if it is null
     *
     * @param id the id of the contractType to save.
     * @param contractType the contractType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractType,
     * or with status {@code 400 (Bad Request)} if the contractType is not valid,
     * or with status {@code 404 (Not Found)} if the contractType is not found,
     * or with status {@code 500 (Internal Server Error)} if the contractType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContractType> partialUpdateContractType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContractType contractType
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContractType partially : {}, {}", id, contractType);
        if (contractType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contractType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContractType> result = contractTypeRepository
            .findById(contractType.getId())
            .map(existingContractType -> {
                if (contractType.getName() != null) {
                    existingContractType.setName(contractType.getName());
                }

                return existingContractType;
            })
            .map(contractTypeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contractType.getId().toString())
        );
    }

    /**
     * {@code GET  /contract-types} : get all the contractTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contractTypes in body.
     */
    @GetMapping("")
    public List<ContractType> getAllContractTypes() {
        log.debug("REST request to get all ContractTypes");
        return contractTypeRepository.findAll();
    }

    /**
     * {@code GET  /contract-types/:id} : get the "id" contractType.
     *
     * @param id the id of the contractType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contractType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContractType> getContractType(@PathVariable("id") Long id) {
        log.debug("REST request to get ContractType : {}", id);
        Optional<ContractType> contractType = contractTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(contractType);
    }

    /**
     * {@code DELETE  /contract-types/:id} : delete the "id" contractType.
     *
     * @param id the id of the contractType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContractType(@PathVariable("id") Long id) {
        log.debug("REST request to delete ContractType : {}", id);
        contractTypeRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
