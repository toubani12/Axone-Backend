package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.Request;
import com.axone.hrsolution.repository.RequestRepository;
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
 * REST controller for managing {@link com.axone.hrsolution.domain.Request}.
 */
@RestController
@RequestMapping("/api/requests")
@Transactional
public class RequestResource {

    private final Logger log = LoggerFactory.getLogger(RequestResource.class);

    private static final String ENTITY_NAME = "request";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RequestRepository requestRepository;

    public RequestResource(RequestRepository requestRepository) {
        this.requestRepository = requestRepository;
    }

    /**
     * {@code POST  /requests} : Create a new request.
     *
     * @param request the request to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new request, or with status {@code 400 (Bad Request)} if the request has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Request> createRequest(@Valid @RequestBody Request request) throws URISyntaxException {
        log.debug("REST request to save Request : {}", request);
        if (request.getId() != null) {
            throw new BadRequestAlertException("A new request cannot already have an ID", ENTITY_NAME, "idexists");
        }
        request = requestRepository.save(request);
        return ResponseEntity.created(new URI("/api/requests/" + request.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, request.getId().toString()))
            .body(request);
    }

    /**
     * {@code PUT  /requests/:id} : Updates an existing request.
     *
     * @param id the id of the request to save.
     * @param request the request to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated request,
     * or with status {@code 400 (Bad Request)} if the request is not valid,
     * or with status {@code 500 (Internal Server Error)} if the request couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Request> updateRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Request request
    ) throws URISyntaxException {
        log.debug("REST request to update Request : {}, {}", id, request);
        if (request.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, request.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!requestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        request = requestRepository.save(request);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, request.getId().toString()))
            .body(request);
    }

    /**
     * {@code PATCH  /requests/:id} : Partial updates given fields of an existing request, field will ignore if it is null
     *
     * @param id the id of the request to save.
     * @param request the request to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated request,
     * or with status {@code 400 (Bad Request)} if the request is not valid,
     * or with status {@code 404 (Not Found)} if the request is not found,
     * or with status {@code 500 (Internal Server Error)} if the request couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Request> partialUpdateRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Request request
    ) throws URISyntaxException {
        log.debug("REST request to partial update Request partially : {}, {}", id, request);
        if (request.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, request.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!requestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Request> result = requestRepository
            .findById(request.getId())
            .map(existingRequest -> {
                if (request.getStatus() != null) {
                    existingRequest.setStatus(request.getStatus());
                }
                if (request.getExpressionOfInterest() != null) {
                    existingRequest.setExpressionOfInterest(request.getExpressionOfInterest());
                }

                return existingRequest;
            })
            .map(requestRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, request.getId().toString())
        );
    }

    /**
     * {@code GET  /requests} : get all the requests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of requests in body.
     */
    @GetMapping("")
    public List<Request> getAllRequests() {
        log.debug("REST request to get all Requests");
        return requestRepository.findAll();
    }

    /**
     * {@code GET  /requests/:id} : get the "id" request.
     *
     * @param id the id of the request to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the request, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Request> getRequest(@PathVariable("id") Long id) {
        log.debug("REST request to get Request : {}", id);
        Optional<Request> request = requestRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(request);
    }

    /**
     * {@code DELETE  /requests/:id} : delete the "id" request.
     *
     * @param id the id of the request to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequest(@PathVariable("id") Long id) {
        log.debug("REST request to delete Request : {}", id);
        requestRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
