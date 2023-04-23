package com.task.HiringManagement.mappers;


import com.task.HiringManagement.dtos.GetSkillDTO;
import com.task.HiringManagement.dtos.PostSkillDTO;
import com.task.HiringManagement.models.Skill;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SkillMapper {

    private static ModelMapper modelMapper;

    @Autowired
    public SkillMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public static Skill fromPostDTOtoModel(PostSkillDTO dto) {
        return modelMapper.map(dto, Skill.class);
    }

    public static PostSkillDTO fromModeltoPostDTO(Skill note) {
        return modelMapper.map(note, PostSkillDTO.class);
    }

    public static Skill fromGetDTOtoModel(GetSkillDTO dto) {
        return modelMapper.map(dto, Skill.class);
    }

    public static GetSkillDTO fromModeltoGetDTO(Skill note) {
        return modelMapper.map(note, GetSkillDTO.class);
    }
}
