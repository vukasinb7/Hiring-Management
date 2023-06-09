package com.task.HiringManagement.services;

import com.task.HiringManagement.dtos.PostCandidateDTO;
import com.task.HiringManagement.dtos.PostSkillListDTO;
import com.task.HiringManagement.exceptions.BadRequestException;
import com.task.HiringManagement.exceptions.NotFoundException;
import com.task.HiringManagement.models.Candidate;
import com.task.HiringManagement.models.Skill;
import com.task.HiringManagement.repositories.CandidateRepository;
import com.task.HiringManagement.repositories.SkillRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CandidateService implements ICandidateService {

    private final CandidateRepository candidateRepository;
    private final SkillRepository skillRepository;

    private ModelMapper modelMapper;

    @Autowired
    public CandidateService(CandidateRepository candidateRepository,SkillRepository skillRepository,ModelMapper modelMapper){
        this.candidateRepository=candidateRepository;
        this.skillRepository=skillRepository;
        this.modelMapper=modelMapper;
    }

    @Override
    public Candidate get(Long id){
        Optional<Candidate> candidate = candidateRepository.findById(id);
        if (candidate.isEmpty())
            throw new NotFoundException("Candidate not found!");
        return  candidate.get();
    }
    @Override
    public Candidate insert(PostCandidateDTO candidateDTO){
        Candidate candidate= modelMapper.map(candidateDTO,Candidate.class);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        candidate.setBirth(LocalDate.parse(candidateDTO.getBirth(),formatter).atStartOfDay());
        if (candidateRepository.findCandidateByEmail(candidate.getEmail()).isPresent())
            throw new BadRequestException("Candidate with same email already exists");
        List<Long> skillIds= candidateDTO.getSkillIds().stream().distinct().toList();
        List<Skill> skills= parseSkills(skillIds);

        candidate.setSkills(skills);
        return candidateRepository.save(candidate);
    }

    @Override
    public void delete(Long id){
        get(id);
        candidateRepository.deleteById(id);
    }

    @Override
    public Page<Candidate> searchByName(String name, PageRequest pageRequest){
        return candidateRepository.findCandidateByNameContainingIgnoreCase(name, pageRequest);
    }

    @Override
    public Page<Candidate> searchBySkills(List<String> skills, PageRequest pageRequest){
        return candidateRepository.findCandidateBySkillsContaing(skills.stream().map(Long::parseLong).toList(),skills.size(),pageRequest);
    }
    @Override
    public Page<Candidate> searchComplex(List<String> skills, String name, PageRequest pageRequest){
        return candidateRepository.findCandidateComplex(skills.stream().map(Long::parseLong).toList(),name,skills.size(),pageRequest);

    }

    @Override
    public Candidate updatePersonalInformations(PostCandidateDTO candidateDTO, Long id){
        Candidate candidate=get(id);
        Optional<Candidate> candidateSameMail=candidateRepository.findCandidateByEmail(candidateDTO.getEmail());
        if (candidateSameMail.isPresent() && candidateSameMail.get().getId()!=id)
            throw new BadRequestException("Email is taken");

        candidate.setName(candidateDTO.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        candidate.setBirth(LocalDate.parse(candidateDTO.getBirth(),formatter).atStartOfDay());
        candidate.setEmail(candidateDTO.getEmail());
        candidate.setContactNumber(candidateDTO.getContactNumber());
        return candidateRepository.save(candidate);
    }
    @Override
    public Candidate addSkills(PostSkillListDTO skillIds, Long id){
        Candidate candidate=get(id);
        List<Skill> updatedSkills=candidate.getSkills();
        updatedSkills.addAll(parseSkills(skillIds.getSkillIds()));
        candidate.setSkills(updatedSkills);
        return candidateRepository.save(candidate);
    }

    @Override
    public Candidate removeSkills(PostSkillListDTO skillIds, Long id){
        Candidate candidate=get(id);
        List<Skill> updatedSkills=candidate.getSkills();
        updatedSkills.removeAll(parseSkills(skillIds.getSkillIds()));
        candidate.setSkills(updatedSkills);
        return candidateRepository.save(candidate);
    }

    @Override
    public Candidate updateSkills(PostSkillListDTO skillIds, Long id){
        Candidate candidate=get(id);
        List<Skill> updatedSkills=parseSkills(skillIds.getSkillIds());
        candidate.setSkills(updatedSkills);
        return candidateRepository.save(candidate);
    }

    @Override
    public Page<Candidate> getAll(PageRequest pageRequest){
        return candidateRepository.findAll(pageRequest);
    }

    private List<Skill> parseSkills(List<Long> skillIds){
        List<Skill> skills=new ArrayList<>();
        for (Long skilID:skillIds) {
            Optional<Skill> skill=skillRepository.findById(skilID);
            if (skill.isEmpty())
                throw new NotFoundException(String.format("Skill with id:{%s} not found", skilID));
            skills.add(skill.get());
        }
        return skills;
    }
}
