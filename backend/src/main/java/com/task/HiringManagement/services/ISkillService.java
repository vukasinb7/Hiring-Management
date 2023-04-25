package com.task.HiringManagement.services;

import com.task.HiringManagement.dtos.PostSkillDTO;
import com.task.HiringManagement.models.Skill;

import java.util.List;

public interface ISkillService {
    Skill get(Long id);

    List<Skill> getAll();

    Skill insert(PostSkillDTO skillDTO);

    Skill update(PostSkillDTO skillDTO, Long id);

    void delete(Long id);
}
