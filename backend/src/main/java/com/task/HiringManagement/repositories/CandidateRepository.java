package com.task.HiringManagement.repositories;

import com.task.HiringManagement.models.Candidate;
import com.task.HiringManagement.models.Skill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface CandidateRepository extends JpaRepository<Candidate,Long> {
    Optional<Candidate> findCandidateByEmail(String email);
    Page<Candidate> findCandidateByNameContainingIgnoreCase(String name, PageRequest pageable);
    @Query(value = "SELECT c.id,c.full_name,c.contact_number,c.birth,c.email FROM candidates c inner join  candidates_skills cs on c.id=cs.candidate_id  where cs.skill_id in (:ids) group by c.id having count(c.id)=(:id_size) ",nativeQuery = true)
    Page<Candidate> findCandidateBySkillsContaing(@Param("ids") List<Long> skills,@Param("id_size") Integer idSize, PageRequest pageable);

    @Query(value = "SELECT candidate FROM Candidate candidate")
    Page<Candidate> findAll(PageRequest pageRequest);
}
