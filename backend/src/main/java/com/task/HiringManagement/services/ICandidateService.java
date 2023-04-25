package com.task.HiringManagement.services;

import com.task.HiringManagement.dtos.PostCandidateDTO;
import com.task.HiringManagement.dtos.PostSkillListDTO;
import com.task.HiringManagement.models.Candidate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.awt.print.Pageable;
import java.util.List;

public interface ICandidateService {
    Candidate get(Long id);

    Candidate insert(PostCandidateDTO candidateDTO);

    void delete(Long id);

    Page<Candidate> searchByName(String name, PageRequest pageable);

    Page<Candidate> searchBySkills(PostSkillListDTO skillListDTO, PageRequest pageRequest);

    Candidate updatePersonalInformations(PostCandidateDTO candidateDTO, Long id);

    Candidate addSkills(PostSkillListDTO skillIds, Long id);

    Candidate removeSkills(PostSkillListDTO skillIds, Long id);

    Page<Candidate> getAll(PageRequest pageRequest);
}
