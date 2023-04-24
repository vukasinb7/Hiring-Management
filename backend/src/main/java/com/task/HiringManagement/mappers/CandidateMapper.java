package com.task.HiringManagement.mappers;

import com.task.HiringManagement.dtos.GetCandidateDTO;
import com.task.HiringManagement.dtos.GetSkillDTO;
import com.task.HiringManagement.dtos.PostCandidateDTO;
import com.task.HiringManagement.models.Candidate;
import com.task.HiringManagement.models.Skill;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CandidateMapper {

    private static ModelMapper modelMapper;

    @Autowired
    public CandidateMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public static Candidate fromPostDTOtoModel(PostCandidateDTO dto) {
        return modelMapper.map(dto, Candidate.class);
    }

    public static PostCandidateDTO fromModeltoPostDTO(Candidate candidate) {
        return modelMapper.map(candidate, PostCandidateDTO.class);
    }

    public static Candidate fromGetDTOtoModel(GetCandidateDTO dto) {
        return modelMapper.map(dto, Candidate.class);
    }

    public static GetCandidateDTO fromModeltoGetDTO(Candidate candidate) {
        return modelMapper.map(candidate, GetCandidateDTO.class);
    }

}
