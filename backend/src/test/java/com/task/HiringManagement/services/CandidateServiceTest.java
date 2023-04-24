package com.task.HiringManagement.services;

import com.task.HiringManagement.dtos.PostCandidateDTO;
import com.task.HiringManagement.dtos.PostSkillListDTO;
import com.task.HiringManagement.exceptions.BadRequestException;
import com.task.HiringManagement.exceptions.NotFoundException;
import com.task.HiringManagement.models.Candidate;
import com.task.HiringManagement.models.Skill;
import com.task.HiringManagement.repositories.CandidateRepository;
import com.task.HiringManagement.repositories.SkillRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class CandidateServiceTest {
    @Mock
    private SkillRepository skillRepository;
    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private ModelMapper modelMapper;


    @InjectMocks
    private CandidateService candidateService;

    @Test
    @DisplayName("Get Candidate - Case: Positive")
    public void getCandidatePositive(){
        Candidate candidateExp= new Candidate("Vukasin Bogdanovic", LocalDateTime.of(2001,12,7,0,0),"0653614028","vukasin@email.com",new ArrayList<>(),1L);
        Mockito.when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidateExp));
        Candidate candidate=candidateService.get(1L);
        Assertions.assertNotNull(candidate);
        Assertions.assertEquals(candidate.getId(),candidate.getId());
        Assertions.assertEquals(candidate.getName(),candidate.getName());
        Assertions.assertEquals(candidate.getContactNumber(),candidate.getContactNumber());
        Assertions.assertEquals(candidate.getEmail(),candidate.getEmail());
        Mockito.verify(candidateRepository,Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get Candidate - Case: Non-existing Candidate")
    public void getCandidate404(){
        Mockito.when(candidateRepository.findById(1L)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class,()->{candidateService.get(1L);},"Candidate not found");
    }

    @Test
    @DisplayName("Delete Candidate. - Case: Positive")
    public void deleteCandidatePositive(){
        Candidate candidateExp= new Candidate("Vukasin Bogdanovic", LocalDateTime.of(2001,12,7,0,0),"0653614028","vukasin@email.com",new ArrayList<>(),1L);
        doNothing().when(candidateRepository).deleteById(1L);
        Mockito.when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidateExp));
        candidateService.delete(1L);
        Mockito.verify(candidateRepository,Mockito.times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Delete Skill - Case: Non-Existing Skill")
    public void deleteSkill404(){
        Mockito.when(candidateRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class,()->{candidateService.delete(1L);},"Candidate not found");
    }

    @Test
    @DisplayName("Add Skills To Candidate - Case: Positive")
    public void addSkillsToCandidatePositive(){
        PostSkillListDTO skillListDTO=new PostSkillListDTO(Arrays.asList(1L,2L,3L));
        Skill skill1=new Skill("Java",1L);
        Skill skill2=new Skill("Go",2L);
        Skill skill3=new Skill("Python",3L);
        Candidate candidateExp= new Candidate("Vukasin Bogdanovic", LocalDateTime.of(2001,12,7,0,0),"0653614028","vukasin@email.com",new ArrayList<>(),1L);
        Mockito.when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidateExp));
        Mockito.when(candidateRepository.save(candidateExp)).thenReturn(candidateExp);
        Mockito.when(skillRepository.findById(1L)).thenReturn(Optional.of(skill1));
        Mockito.when(skillRepository.findById(2L)).thenReturn(Optional.of(skill2));
        Mockito.when(skillRepository.findById(3L)).thenReturn(Optional.of(skill3));
        Candidate candidate=candidateService.addSkills(skillListDTO,1L);
        Assertions.assertTrue(candidate.getSkills().contains(skill1));
        Assertions.assertTrue(candidate.getSkills().contains(skill2));
        Assertions.assertTrue(candidate.getSkills().contains(skill3));
        Assertions.assertEquals(candidate.getSkills().size(),skillListDTO.getSkillIds().size());
        Mockito.verify(candidateRepository,Mockito.times(1)).findById(1L);
        Mockito.verify(skillRepository,Mockito.times(1)).findById(1L);
        Mockito.verify(skillRepository,Mockito.times(1)).findById(2L);
        Mockito.verify(skillRepository,Mockito.times(1)).findById(3L);
    }

    @Test
    @DisplayName("Add Skills To Candidate - Case: Non-Existing Skill")
    public void addSkillsToCandidate404(){
        PostSkillListDTO skillListDTO=new PostSkillListDTO(Arrays.asList(1L,2L,3L));
        Skill skill1=new Skill("Java",1L);
        Skill skill2=new Skill("Go",2L);
        Skill skill3=new Skill("Python",3L);
        Candidate candidateExp= new Candidate("Vukasin Bogdanovic", LocalDateTime.of(2001,12,7,0,0),"0653614028","vukasin@email.com",new ArrayList<>(),1L);
        Mockito.when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidateExp));
        Mockito.when(skillRepository.findById(1L)).thenReturn(Optional.of(skill1));
        Mockito.when(skillRepository.findById(2L)).thenReturn(Optional.of(skill2));
        Mockito.when(skillRepository.findById(3L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class,()->{candidateService.addSkills(skillListDTO,1L);});
    }

    @Test
    @DisplayName("Remove Skills From Candidate - Case: Positive")
    public void removeSkillsFromCandidatePositive(){
        PostSkillListDTO skillListDTO=new PostSkillListDTO(Arrays.asList(1L,2L,3L));
        Skill skill1=new Skill("Java",1L);
        Skill skill2=new Skill("Go",2L);
        Skill skill3=new Skill("Python",3L);
        List<Skill> skills=new ArrayList<>();
        skills.add(skill1);
        skills.add(skill2);
        skills.add(skill3);
        Candidate candidateExp= new Candidate("Vukasin Bogdanovic", LocalDateTime.of(2001,12,7,0,0),"0653614028","vukasin@email.com",skills,1L);
        Mockito.when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidateExp));
        Mockito.when(candidateRepository.save(candidateExp)).thenReturn(candidateExp);
        Mockito.when(skillRepository.findById(1L)).thenReturn(Optional.of(skill1));
        Mockito.when(skillRepository.findById(2L)).thenReturn(Optional.of(skill2));
        Mockito.when(skillRepository.findById(3L)).thenReturn(Optional.of(skill3));
        Candidate candidate=candidateService.removeSkills(skillListDTO,1L);
        Assertions.assertTrue(!candidate.getSkills().contains(skill1));
        Assertions.assertTrue(!candidate.getSkills().contains(skill2));
        Assertions.assertTrue(!candidate.getSkills().contains(skill3));
        Assertions.assertEquals(candidate.getSkills().size(),0);
        Mockito.verify(candidateRepository,Mockito.times(1)).findById(1L);
        Mockito.verify(skillRepository,Mockito.times(1)).findById(1L);
        Mockito.verify(skillRepository,Mockito.times(1)).findById(2L);
        Mockito.verify(skillRepository,Mockito.times(1)).findById(3L);
    }

    @Test
    @DisplayName("Remove Skills From Candidate - Case: Non-Existing Skill")
    public void removeSkillsFromCandidate404(){
        PostSkillListDTO skillListDTO=new PostSkillListDTO(Arrays.asList(1L,2L,3L));
        Skill skill1=new Skill("Java",1L);
        Skill skill2=new Skill("Go",2L);
        Skill skill3=new Skill("Python",3L);
        List<Skill> skills=new ArrayList<>();
        skills.add(skill1);
        skills.add(skill2);
        skills.add(skill3);
        Candidate candidateExp= new Candidate("Vukasin Bogdanovic", LocalDateTime.of(2001,12,7,0,0),"0653614028","vukasin@email.com",skills,1L);
        Mockito.when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidateExp));
        Mockito.when(skillRepository.findById(1L)).thenReturn(Optional.of(skill1));
        Mockito.when(skillRepository.findById(2L)).thenReturn(Optional.of(skill2));
        Mockito.when(skillRepository.findById(3L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class,()->{candidateService.removeSkills(skillListDTO,1L);});
    }

    @Test
    @DisplayName("Insert Candidate - Case: Positive")
    public void addCandidatePositive(){
        PostSkillListDTO skillListDTO=new PostSkillListDTO(Arrays.asList(1L,2L,3L));
        Skill skill1=new Skill("Java",1L);
        Skill skill2=new Skill("Go",2L);
        Skill skill3=new Skill("Python",3L);
        List<Skill> skills=new ArrayList<>();
        skills.add(skill1);
        skills.add(skill2);
        skills.add(skill3);
        PostCandidateDTO candidateDTO= new PostCandidateDTO("Vukasin Bogdanovic",LocalDateTime.of(2001,12,7,0,0),"0653614028","vukasin@email.com",Arrays.asList(1L,2L,3L));
        Candidate candidateExp= new Candidate("Vukasin Bogdanovic", LocalDateTime.of(2001,12,7,0,0),"0653614028","vukasin@email.com",skills,1L);
        Mockito.when(candidateRepository.findCandidateByEmail("vukasin@email.com")).thenReturn(Optional.empty());
        Mockito.when(candidateRepository.save(candidateExp)).thenReturn(candidateExp);
        Mockito.when(modelMapper.map(candidateDTO,Candidate.class)).thenReturn(candidateExp);
        Mockito.when(skillRepository.findById(1L)).thenReturn(Optional.of(skill1));
        Mockito.when(skillRepository.findById(2L)).thenReturn(Optional.of(skill2));
        Mockito.when(skillRepository.findById(3L)).thenReturn(Optional.of(skill3));
        Candidate candidate=candidateService.insert(candidateDTO);
        Assertions.assertEquals(candidate.getName(),candidateExp.getName());
        Assertions.assertEquals(candidate.getEmail(),candidateExp.getEmail());
        Assertions.assertEquals(candidate.getBirth(),candidateExp.getBirth());
        Assertions.assertEquals(candidate.getContactNumber(),candidateExp.getContactNumber());
        Assertions.assertTrue(candidate.getSkills().contains(skill1));
        Assertions.assertTrue(candidate.getSkills().contains(skill2));
        Assertions.assertTrue(candidate.getSkills().contains(skill3));
        Assertions.assertEquals(candidate.getSkills().size(),skillListDTO.getSkillIds().size());
        Mockito.verify(candidateRepository,Mockito.times(1)).findCandidateByEmail("vukasin@email.com");
        Mockito.verify(skillRepository,Mockito.times(1)).findById(1L);
        Mockito.verify(skillRepository,Mockito.times(1)).findById(2L);
        Mockito.verify(skillRepository,Mockito.times(1)).findById(3L);
    }

    @Test
    @DisplayName("Insert Candidate - Case: Email Taken")
    public void addCandidate400(){
        Skill skill1=new Skill("Java",1L);
        Skill skill2=new Skill("Go",2L);
        Skill skill3=new Skill("Python",3L);
        List<Skill> skills=new ArrayList<>();
        skills.add(skill1);
        skills.add(skill2);
        skills.add(skill3);
        PostCandidateDTO candidateDTO= new PostCandidateDTO("Vukasin Bogdanovic",LocalDateTime.of(2001,12,7,0,0),"0653614028","vukasin@email.com",Arrays.asList(1L,2L,3L));
        Candidate candidateExp= new Candidate("Vukasin Bogdanovic", LocalDateTime.of(2001,12,7,0,0),"0653614028","vukasin@email.com",skills,1L);
        Candidate candidateSameEmail= new Candidate("Vujadin Bogdanovic", LocalDateTime.of(2000,12,7,0,0),"065123456","vukasin@email.com",new ArrayList<>(),1L);
        Mockito.when(candidateRepository.findCandidateByEmail("vukasin@email.com")).thenReturn(Optional.of(candidateSameEmail));
        Mockito.when(modelMapper.map(candidateDTO,Candidate.class)).thenReturn(candidateExp);
        Assertions.assertThrows(BadRequestException.class,()->{candidateService.insert(candidateDTO);},"Candidate with same email already exists");
    }

    @Test
    @DisplayName("Insert Candidate - Case: Non-Existing Skill")
    public void addCandidate404(){
        Skill skill1=new Skill("Java",1L);
        Skill skill2=new Skill("Go",2L);
        Skill skill3=new Skill("Python",3L);
        List<Skill> skills=new ArrayList<>();
        skills.add(skill1);
        skills.add(skill2);
        skills.add(skill3);
        PostCandidateDTO candidateDTO= new PostCandidateDTO("Vukasin Bogdanovic",LocalDateTime.of(2001,12,7,0,0),"0653614028","vukasin@email.com",Arrays.asList(1L,2L,3L));
        Candidate candidateExp= new Candidate("Vukasin Bogdanovic", LocalDateTime.of(2001,12,7,0,0),"0653614028","vukasin@email.com",skills,1L);
        Mockito.when(candidateRepository.findCandidateByEmail("vukasin@email.com")).thenReturn(Optional.empty());
        Mockito.when(modelMapper.map(candidateDTO,Candidate.class)).thenReturn(candidateExp);
        Mockito.when(skillRepository.findById(1L)).thenReturn(Optional.of(skill1));
        Mockito.when(skillRepository.findById(2L)).thenReturn(Optional.of(skill2));
        Mockito.when(skillRepository.findById(3L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class,()->{candidateService.insert(candidateDTO);});
    }

    @Test
    @DisplayName("Update Candidate - Case: Positive")
    public void updateCandidatePositive(){
        PostCandidateDTO candidateDTO= new PostCandidateDTO("Vukasin Bogdanovic",LocalDateTime.of(2001,12,7,0,0),"0653614028","vukasin@email.com",new ArrayList<>());
        Candidate candidateExp= new Candidate("Stefan Jovanovic", LocalDateTime.of(2000,1,17,0,0),"065614028","stefan@email.com",new ArrayList<>(),1L);
        Mockito.when(candidateRepository.findCandidateByEmail("vukasin@email.com")).thenReturn(Optional.empty());
        Mockito.when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidateExp));
        Mockito.when(candidateRepository.save(candidateExp)).thenReturn(candidateExp);
        Candidate candidate=candidateService.updatePersonalInformations(candidateDTO,1L);
        Assertions.assertEquals(candidate.getName(),candidateExp.getName());
        Assertions.assertEquals(candidate.getEmail(),candidateExp.getEmail());
        Assertions.assertEquals(candidate.getBirth(),candidateExp.getBirth());
        Assertions.assertEquals(candidate.getContactNumber(),candidateExp.getContactNumber());
        Mockito.verify(candidateRepository,Mockito.times(1)).findCandidateByEmail("vukasin@email.com");
    }

    @Test
    @DisplayName("Update Candidate - Case: Positive Overwrite same email")
    public void updateCandidatePositiveSameEmail(){
        PostCandidateDTO candidateDTO= new PostCandidateDTO("Vukasin Bogdanovic",LocalDateTime.of(2001,12,7,0,0),"0653614028","stefan@email.com",new ArrayList<>());
        Candidate candidateExp= new Candidate("Stefan Jovanovic", LocalDateTime.of(2000,1,17,0,0),"065614028","stefan@email.com",new ArrayList<>(),1L);
        Mockito.when(candidateRepository.findCandidateByEmail("stefan@email.com")).thenReturn(Optional.of(candidateExp));
        Mockito.when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidateExp));
        Mockito.when(candidateRepository.save(candidateExp)).thenReturn(candidateExp);
        Candidate candidate=candidateService.updatePersonalInformations(candidateDTO,1L);
        Assertions.assertEquals(candidate.getName(),candidateExp.getName());
        Assertions.assertEquals(candidate.getEmail(),candidateExp.getEmail());
        Assertions.assertEquals(candidate.getBirth(),candidateExp.getBirth());
        Assertions.assertEquals(candidate.getContactNumber(),candidateExp.getContactNumber());
        Mockito.verify(candidateRepository,Mockito.times(1)).findCandidateByEmail("stefan@email.com");
    }

    @Test
    @DisplayName("Update Candidate - Case: Non-Existing Candidate")
    public void updateCandidate404(){
        PostCandidateDTO candidateDTO= new PostCandidateDTO("Vukasin Bogdanovic",LocalDateTime.of(2001,12,7,0,0),"0653614028","vukasin@email.com",new ArrayList<>());
        Candidate candidateExp= new Candidate("Stefan Jovanovic", LocalDateTime.of(2000,1,17,0,0),"065614028","stefan@email.com",new ArrayList<>(),1L);
        Mockito.when(candidateRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class,()->{candidateService.updatePersonalInformations(candidateDTO,1L);},"Candidate not found");

    }

    @Test
    @DisplayName("Update Candidate - Case: Email Is Taken")
    public void updateCandidate400(){
        PostCandidateDTO candidateDTO= new PostCandidateDTO("Vukasin Bogdanovic",LocalDateTime.of(2001,12,7,0,0),"0653614028","vukasin@email.com",new ArrayList<>());
        Candidate candidateExp= new Candidate("Stefan Jovanovic", LocalDateTime.of(2000,1,17,0,0),"065614028","stefan@email.com",new ArrayList<>(),1L);
        Candidate candidateSameEmail= new Candidate("Stefan Jovanovic", LocalDateTime.of(2000,1,17,0,0),"065614028","vukasin@email.com",new ArrayList<>(),2L);
        Mockito.when(candidateRepository.findCandidateByEmail("vukasin@email.com")).thenReturn(Optional.of(candidateSameEmail));
        Mockito.when(candidateRepository.findById(1L)).thenReturn(Optional.of(candidateExp));
        Assertions.assertThrows(BadRequestException.class,()->{candidateService.updatePersonalInformations(candidateDTO,1L);},"Email is taken");
    }
}
