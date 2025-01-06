package com.axone.hrsolution.web.rest;

import static com.axone.hrsolution.domain.TemplateAsserts.*;
import static com.axone.hrsolution.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.axone.hrsolution.IntegrationTest;
import com.axone.hrsolution.domain.Employer;
import com.axone.hrsolution.domain.Template;
import com.axone.hrsolution.domain.enumeration.TemplateContractType;
import com.axone.hrsolution.repository.TemplateRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TemplateResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class TemplateResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final TemplateContractType DEFAULT_TYPE = TemplateContractType.CDI;
    private static final TemplateContractType UPDATED_TYPE = TemplateContractType.CDD;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STANDARD = false;
    private static final Boolean UPDATED_STANDARD = true;

    private static final byte[] DEFAULT_DOC_LINK = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_DOC_LINK = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_DOC_LINK_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_DOC_LINK_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/templates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TemplateRepository templateRepository;

    @Mock
    private TemplateRepository templateRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTemplateMockMvc;

    private Template template;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Template createEntity(EntityManager em) {
        Template template = new Template()
            .label(DEFAULT_LABEL)
            .type(DEFAULT_TYPE)
            .description(DEFAULT_DESCRIPTION)
            .standard(DEFAULT_STANDARD)
            .docLink(DEFAULT_DOC_LINK)
            .docLinkContentType(DEFAULT_DOC_LINK_CONTENT_TYPE);
        // Add required entity
        Employer employer;
        if (TestUtil.findAll(em, Employer.class).isEmpty()) {
            employer = EmployerResourceIT.createEntity(em);
            em.persist(employer);
            em.flush();
        } else {
            employer = TestUtil.findAll(em, Employer.class).get(0);
        }
        template.setOwner(employer);
        return template;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Template createUpdatedEntity(EntityManager em) {
        Template template = new Template()
            .label(UPDATED_LABEL)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .standard(UPDATED_STANDARD)
            .docLink(UPDATED_DOC_LINK)
            .docLinkContentType(UPDATED_DOC_LINK_CONTENT_TYPE);
        // Add required entity
        Employer employer;
        if (TestUtil.findAll(em, Employer.class).isEmpty()) {
            employer = EmployerResourceIT.createUpdatedEntity(em);
            em.persist(employer);
            em.flush();
        } else {
            employer = TestUtil.findAll(em, Employer.class).get(0);
        }
        template.setOwner(employer);
        return template;
    }

    @BeforeEach
    public void initTest() {
        template = createEntity(em);
    }

    @Test
    @Transactional
    void createTemplate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Template
        var returnedTemplate = om.readValue(
            restTemplateMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(template)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            Template.class
        );

        // Validate the Template in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTemplateUpdatableFieldsEquals(returnedTemplate, getPersistedTemplate(returnedTemplate));
    }

    @Test
    @Transactional
    void createTemplateWithExistingId() throws Exception {
        // Create the Template with an existing ID
        template.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(template)))
            .andExpect(status().isBadRequest());

        // Validate the Template in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLabelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        template.setLabel(null);

        // Create the Template, which fails.

        restTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(template)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        template.setType(null);

        // Create the Template, which fails.

        restTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(template)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStandardIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        template.setStandard(null);

        // Create the Template, which fails.

        restTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(template)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTemplates() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        // Get all the templateList
        restTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(template.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].standard").value(hasItem(DEFAULT_STANDARD.booleanValue())))
            .andExpect(jsonPath("$.[*].docLinkContentType").value(hasItem(DEFAULT_DOC_LINK_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].docLink").value(hasItem(Base64.getEncoder().encodeToString(DEFAULT_DOC_LINK))));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTemplatesWithEagerRelationshipsIsEnabled() throws Exception {
        when(templateRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTemplateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(templateRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllTemplatesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(templateRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restTemplateMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(templateRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getTemplate() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        // Get the template
        restTemplateMockMvc
            .perform(get(ENTITY_API_URL_ID, template.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(template.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.standard").value(DEFAULT_STANDARD.booleanValue()))
            .andExpect(jsonPath("$.docLinkContentType").value(DEFAULT_DOC_LINK_CONTENT_TYPE))
            .andExpect(jsonPath("$.docLink").value(Base64.getEncoder().encodeToString(DEFAULT_DOC_LINK)));
    }

    @Test
    @Transactional
    void getNonExistingTemplate() throws Exception {
        // Get the template
        restTemplateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTemplate() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the template
        Template updatedTemplate = templateRepository.findById(template.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTemplate are not directly saved in db
        em.detach(updatedTemplate);
        updatedTemplate
            .label(UPDATED_LABEL)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .standard(UPDATED_STANDARD)
            .docLink(UPDATED_DOC_LINK)
            .docLinkContentType(UPDATED_DOC_LINK_CONTENT_TYPE);

        restTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTemplate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTemplate))
            )
            .andExpect(status().isOk());

        // Validate the Template in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTemplateToMatchAllProperties(updatedTemplate);
    }

    @Test
    @Transactional
    void putNonExistingTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        template.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, template.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(template))
            )
            .andExpect(status().isBadRequest());

        // Validate the Template in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        template.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(template))
            )
            .andExpect(status().isBadRequest());

        // Validate the Template in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        template.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(template)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Template in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTemplateWithPatch() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the template using partial update
        Template partialUpdatedTemplate = new Template();
        partialUpdatedTemplate.setId(template.getId());

        partialUpdatedTemplate.description(UPDATED_DESCRIPTION).standard(UPDATED_STANDARD);

        restTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTemplate))
            )
            .andExpect(status().isOk());

        // Validate the Template in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTemplateUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTemplate, template), getPersistedTemplate(template));
    }

    @Test
    @Transactional
    void fullUpdateTemplateWithPatch() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the template using partial update
        Template partialUpdatedTemplate = new Template();
        partialUpdatedTemplate.setId(template.getId());

        partialUpdatedTemplate
            .label(UPDATED_LABEL)
            .type(UPDATED_TYPE)
            .description(UPDATED_DESCRIPTION)
            .standard(UPDATED_STANDARD)
            .docLink(UPDATED_DOC_LINK)
            .docLinkContentType(UPDATED_DOC_LINK_CONTENT_TYPE);

        restTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTemplate))
            )
            .andExpect(status().isOk());

        // Validate the Template in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTemplateUpdatableFieldsEquals(partialUpdatedTemplate, getPersistedTemplate(partialUpdatedTemplate));
    }

    @Test
    @Transactional
    void patchNonExistingTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        template.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, template.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(template))
            )
            .andExpect(status().isBadRequest());

        // Validate the Template in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        template.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(template))
            )
            .andExpect(status().isBadRequest());

        // Validate the Template in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        template.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTemplateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(template)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Template in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTemplate() throws Exception {
        // Initialize the database
        templateRepository.saveAndFlush(template);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the template
        restTemplateMockMvc
            .perform(delete(ENTITY_API_URL_ID, template.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return templateRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected Template getPersistedTemplate(Template template) {
        return templateRepository.findById(template.getId()).orElseThrow();
    }

    protected void assertPersistedTemplateToMatchAllProperties(Template expectedTemplate) {
        assertTemplateAllPropertiesEquals(expectedTemplate, getPersistedTemplate(expectedTemplate));
    }

    protected void assertPersistedTemplateToMatchUpdatableProperties(Template expectedTemplate) {
        assertTemplateAllUpdatablePropertiesEquals(expectedTemplate, getPersistedTemplate(expectedTemplate));
    }
}
