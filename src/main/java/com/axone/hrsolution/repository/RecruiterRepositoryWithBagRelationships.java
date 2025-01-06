package com.axone.hrsolution.repository;

import com.axone.hrsolution.domain.Recruiter;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface RecruiterRepositoryWithBagRelationships {
    Optional<Recruiter> fetchBagRelationships(Optional<Recruiter> recruiter);

    List<Recruiter> fetchBagRelationships(List<Recruiter> recruiters);

    Page<Recruiter> fetchBagRelationships(Page<Recruiter> recruiters);
}
