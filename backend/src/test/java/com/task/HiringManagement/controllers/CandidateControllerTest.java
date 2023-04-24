package com.task.HiringManagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.HiringManagement.dtos.PostCandidateDTO;
import com.task.HiringManagement.dtos.PostSkillDTO;
import com.task.HiringManagement.dtos.PostSkillListDTO;
import com.task.HiringManagement.models.Candidate;
import com.task.HiringManagement.models.Skill;
import com.task.HiringManagement.services.CandidateService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CandidateController.class)
public class CandidateControllerTest {
    @MockBean
    private CandidateService candidateService;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Add Candidate - Case: Positive")
    public void createCandidatePositive() throws Exception {
        Skill skill1=new Skill("Java",1L);
        Skill skill2=new Skill("Go",2L);
        Skill skill3=new Skill("Python",3L);
        List<Skill> skills=new ArrayList<>();
        skills.add(skill1);
        skills.add(skill2);
        skills.add(skill3);
        PostCandidateDTO candidateDTO= new PostCandidateDTO("Vukasin Bogdanovic", LocalDateTime.of(2001,12,7,0,0),"0653614028","vukasin@email.com",Arrays.asList(1L,2L,3L));
        Candidate candidateExp= new Candidate("Vukasin Bogdanovic", LocalDateTime.of(2001,12,7,0,0),"0653614028","vukasin@email.com",skills,1L);

        Mockito.when(candidateService.insert(candidateDTO)).thenReturn(candidateExp);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/candidate")
                        .content(objectMapper.writeValueAsString(candidateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("Vukasin Bogdanovic")));
    }

    @Test
    @DisplayName("Delete Candidate - Case: Positive")
    public void deleteCandidatePositive() throws Exception {

        doNothing().when(candidateService).delete(1L);

        mockMvc.perform(delete("/api/candidate/1"))
                .andExpect(status().is(204));
    }
    @Test
    @DisplayName("Delete Candidate - Case: Not a number")
    public void deleteCandidate400Format() throws Exception {
        mockMvc.perform(delete("/api/candidate/aa")).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Search Candidate By Name - Case: Positive")
    public void searchCandidateByNamePositive() throws Exception {
        Skill skill1=new Skill("Java",1L);
        Skill skill2=new Skill("Go",2L);
        Skill skill3=new Skill("Python",3L);
        List<Skill> skills=new ArrayList<>();
        skills.add(skill1);
        skills.add(skill2);
        skills.add(skill3);
        Candidate candidateExp= new Candidate("Vukasin Bogdanovic", LocalDateTime.of(2001,12,7,0,0),"0653614028","vukasin@email.com",skills,1L);
        Page<Candidate> page = new PageImpl<>(Arrays.asList(candidateExp));
        Mockito.when(candidateService.searchByName("vuk", PageRequest.of(0,5))).thenReturn(page);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/candidate/searchName?page=0&size=5&name=vuk")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount", Matchers.is(1)));
    }

    @Test
    @DisplayName("Search Candidate By Skills - Case: Positive")
    public void searchCandidateBySkillsPositive() throws Exception {
        PostSkillListDTO skillListDTO=new PostSkillListDTO(Arrays.asList(1L,2L,3L));
        Skill skill1=new Skill("Java",1L);
        Skill skill2=new Skill("Go",2L);
        Skill skill3=new Skill("Python",3L);
        List<Skill> skills=new ArrayList<>();
        skills.add(skill1);
        skills.add(skill2);
        skills.add(skill3);
        Candidate candidateExp= new Candidate("Vukasin Bogdanovic", LocalDateTime.of(2001,12,7,0,0),"0653614028","vukasin@email.com",skills,1L);
        Page<Candidate> page = new PageImpl<>(Arrays.asList(candidateExp));
        Mockito.when(candidateService.searchBySkills(skillListDTO, PageRequest.of(0,5))).thenReturn(page);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/candidate/searchSkills?page=0&size=5")
                        .content(objectMapper.writeValueAsString(skillListDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalCount", Matchers.is(1)));
    }

    @Test
    @DisplayName("Update Candidate - Case: Not a number")
    public void updateCandidate400Format() throws Exception {
        mockMvc.perform(delete("/api/candidate/aa")).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Add Skills To Candidate - Case: Positive")
    public void addSkillPositive() throws Exception {
        PostSkillListDTO skillListDTO=new PostSkillListDTO(Arrays.asList(1L,2L,3L));
        Skill skill1=new Skill("Java",1L);
        Skill skill2=new Skill("Go",2L);
        Skill skill3=new Skill("Python",3L);
        List<Skill> skills=new ArrayList<>();
        skills.add(skill1);
        skills.add(skill2);
        skills.add(skill3);
        Candidate candidateExp= new Candidate("Vukasin Bogdanovic", LocalDateTime.of(2001,12,7,0,0),"0653614028","vukasin@email.com",skills,1L);

        Mockito.when(candidateService.addSkills(skillListDTO,1L)).thenReturn(candidateExp);
        mockMvc.perform(put("/api/candidate/skills/add/1")
                        .content(objectMapper.writeValueAsString(skillListDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.skills[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$.skills[1].id", Matchers.is(2)))
                .andExpect(jsonPath("$.skills[2].id", Matchers.is(3)));
    }
    @Test
    @DisplayName("Add Skill To Candidate - Case: Not a number")
    public void AddSkillToCandidate400Format() throws Exception {
        mockMvc.perform(put("/api/candidate/skills/add/aa")).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Remove Skills From Candidate - Case: Positive")
    public void removeSkillPositive() throws Exception {
        PostSkillListDTO skillListDTO=new PostSkillListDTO(Arrays.asList(1L,2L,3L));
        Skill skill1=new Skill("Java",1L);
        Skill skill2=new Skill("Go",2L);
        Skill skill3=new Skill("Python",3L);
        Candidate candidateExp= new Candidate("Vukasin Bogdanovic", LocalDateTime.of(2001,12,7,0,0),"0653614028","vukasin@email.com",new ArrayList<>(),1L);

        Mockito.when(candidateService.removeSkills(skillListDTO,1L)).thenReturn(candidateExp);
        mockMvc.perform(put("/api/candidate/skills/remove/1")
                        .content(objectMapper.writeValueAsString(skillListDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.skills.length()", Matchers.is(0)));
    }

    @Test
    @DisplayName("Remove Skill From Candidate - Case: Not a number")
    public void RemoveSkillFromCandidate400Format() throws Exception {
        mockMvc.perform(put("/api/candidate/skills/remove/aa")).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Update Candidate - Case: Positive")
    public void updateCandidatePositive() throws Exception {
        Skill skill1=new Skill("Java",1L);
        Skill skill2=new Skill("Go",2L);
        Skill skill3=new Skill("Python",3L);
        List<Skill> skills=new ArrayList<>();
        skills.add(skill1);
        skills.add(skill2);
        skills.add(skill3);
        PostCandidateDTO candidateDTO= new PostCandidateDTO("Vukasin Bogdanovic", LocalDateTime.of(2001,12,7,0,0),"0653614028","vukasin@email.com",Arrays.asList(1L,2L,3L));
        Candidate candidateExp= new Candidate("Vukasin Bogdanovic", LocalDateTime.of(2001,12,7,0,0),"0653614028","vukasin@email.com",skills,1L);

        Mockito.when(candidateService.updatePersonalInformations(candidateDTO,1L)).thenReturn(candidateExp);
        mockMvc.perform(put("/api/candidate/update/1")
                        .content(objectMapper.writeValueAsString(candidateDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("Vukasin Bogdanovic")));
    }

}
