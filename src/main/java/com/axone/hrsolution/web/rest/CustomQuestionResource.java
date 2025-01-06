package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.CustomQuestion;
import com.axone.hrsolution.repository.CustomQuestionRepository;
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
 * REST controller for managing {@link com.axone.hrsolution.domain.CustomQuestion}.
 */
@RestController
@RequestMapping("/api/custom-questions")
@Transactional
public class CustomQuestionResource {

    private final Logger log = LoggerFactory.getLogger(CustomQuestionResource.class);

    private static final String ENTITY_NAME = "customQuestion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomQuestionRepository customQuestionRepository;

    public CustomQuestionResource(CustomQuestionRepository customQuestionRepository) {
        this.customQuestionRepository = customQuestionRepository;
    }

    /**
     * {@code POST  /custom-questions} : Create a new customQuestion.
     *
     * @param customQuestion the customQuestion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customQuestion, or with status {@code 400 (Bad Request)} if the customQuestion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<CustomQuestion> createCustomQuestion(@Valid @RequestBody CustomQuestion customQuestion)
        throws URISyntaxException {
        log.debug("REST request to save CustomQuestion : {}", customQuestion);
        if (customQuestion.getId() != null) {
            throw new BadRequestAlertException("A new customQuestion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        customQuestion = customQuestionRepository.save(customQuestion);
        return ResponseEntity.created(new URI("/api/custom-questions/" + customQuestion.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, customQuestion.getId().toString()))
            .body(customQuestion);
    }

    /**
     * {@code PUT  /custom-questions/:id} : Updates an existing customQuestion.
     *
     * @param id the id of the customQuestion to save.
     * @param customQuestion the customQuestion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customQuestion,
     * or with status {@code 400 (Bad Request)} if the customQuestion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customQuestion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CustomQuestion> updateCustomQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomQuestion customQuestion
    ) throws URISyntaxException {
        log.debug("REST request to update CustomQuestion : {}, {}", id, customQuestion);
        if (customQuestion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customQuestion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        customQuestion = customQuestionRepository.save(customQuestion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customQuestion.getId().toString()))
            .body(customQuestion);
    }

    /**
     * {@code PATCH  /custom-questions/:id} : Partial updates given fields of an existing customQuestion, field will ignore if it is null
     *
     * @param id the id of the customQuestion to save.
     * @param customQuestion the customQuestion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customQuestion,
     * or with status {@code 400 (Bad Request)} if the customQuestion is not valid,
     * or with status {@code 404 (Not Found)} if the customQuestion is not found,
     * or with status {@code 500 (Internal Server Error)} if the customQuestion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomQuestion> partialUpdateCustomQuestion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomQuestion customQuestion
    ) throws URISyntaxException {
        log.debug("REST request to partial update CustomQuestion partially : {}, {}", id, customQuestion);
        if (customQuestion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customQuestion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customQuestionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomQuestion> result = customQuestionRepository
            .findById(customQuestion.getId())
            .map(existingCustomQuestion -> {
                if (customQuestion.getQuestion() != null) {
                    existingCustomQuestion.setQuestion(customQuestion.getQuestion());
                }
                if (customQuestion.getAnswer() != null) {
                    existingCustomQuestion.setAnswer(customQuestion.getAnswer());
                }
                if (customQuestion.getCorrectAnswer() != null) {
                    existingCustomQuestion.setCorrectAnswer(customQuestion.getCorrectAnswer());
                }

                return existingCustomQuestion;
            })
            .map(customQuestionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customQuestion.getId().toString())
        );
    }

    /**
     * {@code GET  /custom-questions} : get all the customQuestions.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customQuestions in body.
     */
    @GetMapping("")
    public List<CustomQuestion> getAllCustomQuestions() {
        log.debug("REST request to get all CustomQuestions");
        return customQuestionRepository.findAll();
    }

    /**
     * {@code GET  /custom-questions/:id} : get the "id" customQuestion.
     *
     * @param id the id of the customQuestion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customQuestion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CustomQuestion> getCustomQuestion(@PathVariable("id") Long id) {
        log.debug("REST request to get CustomQuestion : {}", id);
        Optional<CustomQuestion> customQuestion = customQuestionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(customQuestion);
    }

    /**
     * {@code DELETE  /custom-questions/:id} : delete the "id" customQuestion.
     *
     * @param id the id of the customQuestion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomQuestion(@PathVariable("id") Long id) {
        log.debug("REST request to delete CustomQuestion : {}", id);
        customQuestionRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
