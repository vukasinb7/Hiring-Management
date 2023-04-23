package com.task.HiringManagement.repositories;

import com.task.HiringManagement.models.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidateRepository extends JpaRepository<Candidate,Long> {
}
