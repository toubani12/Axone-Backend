package com.axone.hrsolution.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Interview.
 */
@Entity
@Table(name = "interview")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Interview implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

    @NotNull
    @Column(name = "entry_link", nullable = false)
    private String entryLink;

    @NotNull
    @Column(name = "invitation_link", nullable = false)
    private String invitationLink;

    @Column(name = "interview_result")
    private Boolean interviewResult;

    @Column(name = "rate")
    private Float rate;

    @Column(name = "started_at")
    private LocalDate startedAt;

    @Column(name = "ended_at")
    private LocalDate endedAt;

    @Column(name = "comments")
    private String comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "techCV", "interviewResults", "candidateResumes", "domains", "applications", "contract", "ndaStatuses" },
        allowSetters = true
    )
    private Candidate attendee;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(
        value = {
            "contracts",
            "interviews",
            "contractTypes",
            "contractTemplates",
            "criteria",
            "domains",
            "employer",
            "recruiters",
            "candidates",
            "request",
        },
        allowSetters = true
    )
    private Application application;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Interview id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return this.label;
    }

    public Interview label(String label) {
        this.setLabel(label);
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getEntryLink() {
        return this.entryLink;
    }

    public Interview entryLink(String entryLink) {
        this.setEntryLink(entryLink);
        return this;
    }

    public void setEntryLink(String entryLink) {
        this.entryLink = entryLink;
    }

    public String getInvitationLink() {
        return this.invitationLink;
    }

    public Interview invitationLink(String invitationLink) {
        this.setInvitationLink(invitationLink);
        return this;
    }

    public void setInvitationLink(String invitationLink) {
        this.invitationLink = invitationLink;
    }

    public Boolean getInterviewResult() {
        return this.interviewResult;
    }

    public Interview interviewResult(Boolean interviewResult) {
        this.setInterviewResult(interviewResult);
        return this;
    }

    public void setInterviewResult(Boolean interviewResult) {
        this.interviewResult = interviewResult;
    }

    public Float getRate() {
        return this.rate;
    }

    public Interview rate(Float rate) {
        this.setRate(rate);
        return this;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public LocalDate getStartedAt() {
        return this.startedAt;
    }

    public Interview startedAt(LocalDate startedAt) {
        this.setStartedAt(startedAt);
        return this;
    }

    public void setStartedAt(LocalDate startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDate getEndedAt() {
        return this.endedAt;
    }

    public Interview endedAt(LocalDate endedAt) {
        this.setEndedAt(endedAt);
        return this;
    }

    public void setEndedAt(LocalDate endedAt) {
        this.endedAt = endedAt;
    }

    public String getComments() {
        return this.comments;
    }

    public Interview comments(String comments) {
        this.setComments(comments);
        return this;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public Candidate getAttendee() {
        return this.attendee;
    }

    public void setAttendee(Candidate candidate) {
        this.attendee = candidate;
    }

    public Interview attendee(Candidate candidate) {
        this.setAttendee(candidate);
        return this;
    }

    public Application getApplication() {
        return this.application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public Interview application(Application application) {
        this.setApplication(application);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Interview)) {
            return false;
        }
        return getId() != null && getId().equals(((Interview) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Interview{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", entryLink='" + getEntryLink() + "'" +
            ", invitationLink='" + getInvitationLink() + "'" +
            ", interviewResult='" + getInterviewResult() + "'" +
            ", rate=" + getRate() +
            ", startedAt='" + getStartedAt() + "'" +
            ", endedAt='" + getEndedAt() + "'" +
            ", comments='" + getComments() + "'" +
            "}";
    }
}
