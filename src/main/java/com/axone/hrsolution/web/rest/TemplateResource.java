package com.axone.hrsolution.web.rest;

import com.axone.hrsolution.domain.Template;
import com.axone.hrsolution.repository.TemplateRepository;
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
 * REST controller for managing {@link com.axone.hrsolution.domain.Template}.
 */
@RestController
@RequestMapping("/api/templates")
@Transactional
public class TemplateResource {

    private final Logger log = LoggerFactory.getLogger(TemplateResource.class);

    private static final String ENTITY_NAME = "template";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TemplateRepository templateRepository;

    public TemplateResource(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    /**
     * {@code POST  /templates} : Create a new template.
     *
     * @param template the template to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new template, or with status {@code 400 (Bad Request)} if the template has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Template> createTemplate(@Valid @RequestBody Template template) throws URISyntaxException {
        log.debug("REST request to save Template : {}", template);
        if (template.getId() != null) {
            throw new BadRequestAlertException("A new template cannot already have an ID", ENTITY_NAME, "idexists");
        }
        template = templateRepository.save(template);
        return ResponseEntity.created(new URI("/api/templates/" + template.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, template.getId().toString()))
            .body(template);
    }

    /**
     * {@code PUT  /templates/:id} : Updates an existing template.
     *
     * @param id the id of the template to save.
     * @param template the template to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated template,
     * or with status {@code 400 (Bad Request)} if the template is not valid,
     * or with status {@code 500 (Internal Server Error)} if the template couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Template> updateTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Template template
    ) throws URISyntaxException {
        log.debug("REST request to update Template : {}, {}", id, template);
        if (template.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, template.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!templateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        template = templateRepository.save(template);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, template.getId().toString()))
            .body(template);
    }

    /**
     * {@code PATCH  /templates/:id} : Partial updates given fields of an existing template, field will ignore if it is null
     *
     * @param id the id of the template to save.
     * @param template the template to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated template,
     * or with status {@code 400 (Bad Request)} if the template is not valid,
     * or with status {@code 404 (Not Found)} if the template is not found,
     * or with status {@code 500 (Internal Server Error)} if the template couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Template> partialUpdateTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Template template
    ) throws URISyntaxException {
        log.debug("REST request to partial update Template partially : {}, {}", id, template);
        if (template.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, template.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!templateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Template> result = templateRepository
            .findById(template.getId())
            .map(existingTemplate -> {
                if (template.getLabel() != null) {
                    existingTemplate.setLabel(template.getLabel());
                }
                if (template.getType() != null) {
                    existingTemplate.setType(template.getType());
                }
                if (template.getDescription() != null) {
                    existingTemplate.setDescription(template.getDescription());
                }
                if (template.getStandard() != null) {
                    existingTemplate.setStandard(template.getStandard());
                }
                if (template.getDocLink() != null) {
                    existingTemplate.setDocLink(template.getDocLink());
                }
                if (template.getDocLinkContentType() != null) {
                    existingTemplate.setDocLinkContentType(template.getDocLinkContentType());
                }

                return existingTemplate;
            })
            .map(templateRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, template.getId().toString())
        );
    }

    /**
     * {@code GET  /templates} : get all the templates.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of templates in body.
     */
    @GetMapping("")
    public List<Template> getAllTemplates(
        @RequestParam(name = "filter", required = false) String filter,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        if ("contract-is-null".equals(filter)) {
            log.debug("REST request to get all Templates where contract is null");
            return StreamSupport.stream(templateRepository.findAll().spliterator(), false)
                .filter(template -> template.getContract() == null)
                .toList();
        }
        log.debug("REST request to get all Templates");
        if (eagerload) {
            return templateRepository.findAllWithEagerRelationships();
        } else {
            return templateRepository.findAll();
        }
    }

    /**
     * {@code GET  /templates/:id} : get the "id" template.
     *
     * @param id the id of the template to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the template, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Template> getTemplate(@PathVariable("id") Long id) {
        log.debug("REST request to get Template : {}", id);
        Optional<Template> template = templateRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(template);
    }

    /**
     * {@code DELETE  /templates/:id} : delete the "id" template.
     *
     * @param id the id of the template to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(@PathVariable("id") Long id) {
        log.debug("REST request to delete Template : {}", id);
        templateRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
