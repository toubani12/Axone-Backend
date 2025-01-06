package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.Candidate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface CandidateRepositoryWithBagRelationships {
    Optional<Candidate> fetchBagRelationships(Optional<Candidate> candidate);

    List<Candidate> fetchBagRelationships(List<Candidate> candidates);

    Page<Candidate> fetchBagRelationships(Page<Candidate> candidates);
}
