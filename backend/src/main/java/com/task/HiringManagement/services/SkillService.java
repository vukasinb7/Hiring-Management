package com.task.HiringManagement.services;

import com.task.HiringManagement.dtos.PostSkillDTO;
import com.task.HiringManagement.exceptions.BadRequestException;
import com.task.HiringManagement.exceptions.NotFoundException;
import com.task.HiringManagement.models.Skill;
import com.task.HiringManagement.repositories.SkillRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SkillService implements ISkillService{

    private final SkillRepository skillRepository;
    private ModelMapper modelMapper;

    @Autowired
    public SkillService(SkillRepository skillRepository, ModelMapper modelMapper){
        this.skillRepository=skillRepository;
        this.modelMapper=modelMapper;
    }

    @Override
    public Skill get(Long id){
        Optional<Skill> skill = skillRepository.findById(id);
        if (skill.isEmpty())
            throw new NotFoundException("Skill not found");
        return  skill.get();
    }

    @Override
    public Skill insert(PostSkillDTO skillDTO){
        Skill skill= modelMapper.map(skillDTO,Skill.class);
        if (skillRepository.findByName(skill.getName()).isPresent())
            throw new BadRequestException("Skill with given name already exists");
        return skillRepository.save(skill);
    }

    @Override
    public Skill update(PostSkillDTO skillDTO, Long id){
        Skill skill= get(id);
        Optional<Skill> existingSkill=skillRepository.findByName(skillDTO.getName());
        if (existingSkill.isPresent() && existingSkill.get().getId()!=id)
            throw new BadRequestException("Skill name is taken");
        skill.setName(skillDTO.getName());
        return skillRepository.save(skill);
    }

    @Override
    public void delete(Long id){
        get(id);
        skillRepository.deleteById(id);
    }
}
