package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.Contract;
import com.axone.hrsolution.repository.ContractRepository;
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
 * REST controller for managing {@link com.axone.hrsolution.domain.Contract}.
 */
@RestController
@RequestMapping("/api/contracts")
@Transactional
public class ContractResource {

    private final Logger log = LoggerFactory.getLogger(ContractResource.class);

    private static final String ENTITY_NAME = "contract";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContractRepository contractRepository;

    public ContractResource(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    /**
     * {@code POST  /contracts} : Create a new contract.
     *
     * @param contract the contract to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contract, or with status {@code 400 (Bad Request)} if the contract has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Contract> createContract(@Valid @RequestBody Contract contract) throws URISyntaxException {
        log.debug("REST request to save Contract : {}", contract);
        if (contract.getId() != null) {
            throw new BadRequestAlertException("A new contract cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contract = contractRepository.save(contract);
        return ResponseEntity.created(new URI("/api/contracts/" + contract.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, contract.getId().toString()))
            .body(contract);
    }

    /**
     * {@code PUT  /contracts/:id} : Updates an existing contract.
     *
     * @param id the id of the contract to save.
     * @param contract the contract to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contract,
     * or with status {@code 400 (Bad Request)} if the contract is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contract couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Contract> updateContract(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Contract contract
    ) throws URISyntaxException {
        log.debug("REST request to update Contract : {}, {}", id, contract);
        if (contract.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contract.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contract = contractRepository.save(contract);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contract.getId().toString()))
            .body(contract);
    }

    /**
     * {@code PATCH  /contracts/:id} : Partial updates given fields of an existing contract, field will ignore if it is null
     *
     * @param id the id of the contract to save.
     * @param contract the contract to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contract,
     * or with status {@code 400 (Bad Request)} if the contract is not valid,
     * or with status {@code 404 (Not Found)} if the contract is not found,
     * or with status {@code 500 (Internal Server Error)} if the contract couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Contract> partialUpdateContract(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Contract contract
    ) throws URISyntaxException {
        log.debug("REST request to partial update Contract partially : {}, {}", id, contract);
        if (contract.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contract.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Contract> result = contractRepository
            .findById(contract.getId())
            .map(existingContract -> {
                if (contract.getLabel() != null) {
                    existingContract.setLabel(contract.getLabel());
                }
                if (contract.getType() != null) {
                    existingContract.setType(contract.getType());
                }
                if (contract.getStatus() != null) {
                    existingContract.setStatus(contract.getStatus());
                }
                if (contract.getDirectContract() != null) {
                    existingContract.setDirectContract(contract.getDirectContract());
                }
                if (contract.getPaymentAmount() != null) {
                    existingContract.setPaymentAmount(contract.getPaymentAmount());
                }
                if (contract.getRecruiterIncomeRate() != null) {
                    existingContract.setRecruiterIncomeRate(contract.getRecruiterIncomeRate());
                }
                if (contract.getCandidateIncomeRate() != null) {
                    existingContract.setCandidateIncomeRate(contract.getCandidateIncomeRate());
                }

                return existingContract;
            })
            .map(contractRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contract.getId().toString())
        );
    }

    /**
     * {@code GET  /contracts} : get all the contracts.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contracts in body.
     */
    @GetMapping("")
    public List<Contract> getAllContracts(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        log.debug("REST request to get all Contracts");
        if (eagerload) {
            return contractRepository.findAllWithEagerRelationships();
        } else {
            return contractRepository.findAll();
        }
    }

    /**
     * {@code GET  /contracts/:id} : get the "id" contract.
     *
     * @param id the id of the contract to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contract, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Contract> getContract(@PathVariable("id") Long id) {
        log.debug("REST request to get Contract : {}", id);
        Optional<Contract> contract = contractRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(contract);
    }

    /**
     * {@code DELETE  /contracts/:id} : delete the "id" contract.
     *
     * @param id the id of the contract to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable("id") Long id) {
        log.debug("REST request to delete Contract : {}", id);
        contractRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
