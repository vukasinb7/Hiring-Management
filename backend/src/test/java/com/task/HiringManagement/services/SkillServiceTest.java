package com.task.HiringManagement.services;

import com.task.HiringManagement.dtos.PostSkillDTO;
import com.task.HiringManagement.exceptions.BadRequestException;
import com.task.HiringManagement.exceptions.NotFoundException;
import com.task.HiringManagement.models.Skill;
import com.task.HiringManagement.repositories.SkillRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
public class SkillServiceTest {
    @Mock
    private SkillRepository skillRepository;

    @Mock
    private ModelMapper modelMapper;


    @InjectMocks
    private SkillService skillService;


    @Test
    @DisplayName("Get Skill - Case: Positive")
    public void getSkillPositive(){
        Skill skillExp= new Skill("Java",1L);
        Mockito.when(skillRepository.findById(1L)).thenReturn(Optional.of(skillExp));
        Skill skill=skillService.get(1L);
        Assertions.assertNotNull(skill);
        Assertions.assertEquals(skill.getId(),skillExp.getId());
        Assertions.assertEquals(skill.getName(),skillExp.getName());
        Mockito.verify(skillRepository,Mockito.times(1)).findById(1L);
    }

    @Test
    @DisplayName("Get Skill - Case: Non-existing Skill")
    public void getSkill404(){
        Mockito.when(skillRepository.findById(1L)).thenThrow(NotFoundException.class);
        Assertions.assertThrows(NotFoundException.class,()->{skillService.get(1L);},"Skill not found");
    }

    @Test
    @DisplayName("Add Skill - Case: Positive")
    public void addSkillPositive(){
        PostSkillDTO skillDTO= new PostSkillDTO("Java");
        Skill skillExp= new Skill("Java",1L);
        Mockito.when(skillRepository.findByName("Java")).thenReturn(Optional.empty());
        Mockito.when(skillRepository.save(skillExp)).thenReturn(skillExp);
        Mockito.when(modelMapper.map(skillDTO,Skill.class)).thenReturn(skillExp);
        Skill skill=skillService.insert(skillDTO);
        Assertions.assertNotNull(skill);
        Assertions.assertEquals(skill.getName(),skillExp.getName());
        Assertions.assertEquals(skill.getId(),skillExp.getId());
        Mockito.verify(skillRepository,Mockito.times(1)).findByName("Java");
        Mockito.verify(skillRepository,Mockito.times(1)).save(skillExp);
        Mockito.verify(modelMapper,Mockito.times(1)).map(skillDTO,Skill.class);
    }

    @Test
    @DisplayName("Add Skill - Case: Name Already Exists")
    public void addSkill400(){
        PostSkillDTO skillDTO= new PostSkillDTO("Java");
        Skill skillExp= new Skill("Java",1L);
        Mockito.when(skillRepository.findByName("Java")).thenReturn(Optional.of(skillExp));
        Mockito.when(modelMapper.map(skillDTO,Skill.class)).thenReturn(skillExp);
        Assertions.assertThrows(BadRequestException.class,()->{skillService.insert(skillDTO);},"Skill with given name already exists");
    }

    @Test
    @DisplayName("Update Skill - Case: Positive")
    public void updateSkillPositive(){
        PostSkillDTO skillDTO= new PostSkillDTO("Java");
        Skill skillExp= new Skill("Java",1L);
        Skill skillBeforeUpdate=new Skill("Python",1L);
        Mockito.when(skillRepository.findByName("Java")).thenReturn(Optional.empty());
        Mockito.when(skillRepository.findById(1L)).thenReturn(Optional.of(skillBeforeUpdate));
        Mockito.when(skillRepository.save(skillExp)).thenReturn(skillExp);
        Skill skill=skillService.update(skillDTO,1L);
        Assertions.assertNotNull(skill);
        Assertions.assertEquals(skill.getName(),skillExp.getName());
        Assertions.assertEquals(skill.getId(),skillExp.getId());
        Mockito.verify(skillRepository,Mockito.times(1)).findByName("Java");
        Mockito.verify(skillRepository,Mockito.times(1)).save(skillExp);
    }

    @Test
    @DisplayName("Update Skill - Case: Overwrite Same Name Positive")
    public void updateSkillPositiveSameName(){
        PostSkillDTO skillDTO= new PostSkillDTO("Java");
        Skill skillExp= new Skill("Java",1L);
        Skill skillBeforeUpdate=new Skill("Java",1L);
        Mockito.when(skillRepository.findByName("Java")).thenReturn(Optional.of(skillBeforeUpdate));
        Mockito.when(skillRepository.findById(1L)).thenReturn(Optional.of(skillBeforeUpdate));
        Mockito.when(skillRepository.save(skillExp)).thenReturn(skillExp);
        Skill skill=skillService.update(skillDTO,1L);
        Assertions.assertNotNull(skill);
        Assertions.assertEquals(skill.getName(),skillExp.getName());
        Assertions.assertEquals(skill.getId(),skillExp.getId());
        Mockito.verify(skillRepository,Mockito.times(1)).findByName("Java");
        Mockito.verify(skillRepository,Mockito.times(1)).save(skillExp);
    }

    @Test
    @DisplayName("Update Skill - Case: Non-Existing Skill")
    public void updateSkill404(){
        PostSkillDTO skillDTO= new PostSkillDTO("Java");
        Mockito.when(skillRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class,()->{skillService.update(skillDTO,1L);},"Skill not found");
    }

    @Test
    @DisplayName("Update Skill - Case: Skill name is taken")
    public void updateSkill400(){
        PostSkillDTO skillDTO= new PostSkillDTO("Java");
        Skill skillBeforeUpdate=new Skill("Python",1L);
        Skill skillSameName=new Skill("Java",2L);
        Mockito.when(skillRepository.findByName("Java")).thenReturn(Optional.of(skillSameName));
        Mockito.when(skillRepository.findById(1L)).thenReturn(Optional.of(skillBeforeUpdate));
        Assertions.assertThrows(BadRequestException.class,()->{skillService.update(skillDTO,1L);},"Skill name is taken");

    }

    @Test
    @DisplayName("Delete Skill - Case: Positive")
    public void deleteSkillPositive(){
        Skill skillExp= new Skill("Java",1L);
        doNothing().when(skillRepository).deleteById(1L);
        Mockito.when(skillRepository.findById(1L)).thenReturn(Optional.of(skillExp));
        skillService.delete(1L);
        Mockito.verify(skillRepository,Mockito.times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Delete Skill - Case: Non-Existing Skill")
    public void deleteSkill404(){
        Mockito.when(skillRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class,()->{skillService.delete(1L);},"Skill not found");
    }
}
