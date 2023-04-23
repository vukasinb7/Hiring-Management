package com.task.HiringManagement.services;

import com.task.HiringManagement.models.Skill;
import com.task.HiringManagement.repositories.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkillService implements ISkillService{

    private final SkillRepository skillRepository;

    @Autowired
    public SkillService(SkillRepository skillRepository){
        this.skillRepository=skillRepository;
    }
}
