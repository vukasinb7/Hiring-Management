package com.task.HiringManagement.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.HiringManagement.dtos.PostSkillDTO;
import com.task.HiringManagement.models.Skill;
import com.task.HiringManagement.services.SkillService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = SkillController.class)
public class SkillControllerTest {
    @MockBean
    private SkillService skillService;
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("Get Skill - Case: Positive")
    public void getSkillPositive() throws Exception {
        Skill skillExp= new Skill("Java",1L);

        Mockito.when(skillService.get(1L)).thenReturn(skillExp);

        mockMvc.perform(get("/api/skill/1"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("Java")));
    }
    @Test
    @DisplayName("Get Skill - Case: Not a number")
    public void getSkill400Format() throws Exception {
        mockMvc.perform(get("/api/skill/aa")).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Add Skill - Case: Positive")
    public void createSkillPositive() throws Exception {
        PostSkillDTO skillDTO= new PostSkillDTO("Java");
        Skill skillExp= new Skill("Java",1L);
        Mockito.when(skillService.insert(skillDTO)).thenReturn(skillExp);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/skill")
                        .content(objectMapper.writeValueAsString(skillDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("Java")));
    }

    @Test
    @DisplayName("Update Skill - Case: Positive")
    public void updateSkillPositive() throws Exception {
        PostSkillDTO skillDTO= new PostSkillDTO("Java");
        Skill skillExp= new Skill("Java",1L);
        Mockito.when(skillService.update(skillDTO,1L)).thenReturn(skillExp);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/skill/1")
                        .content(objectMapper.writeValueAsString(skillDTO))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(1)))
                .andExpect(jsonPath("$.name", Matchers.is("Java")));
    }

    @Test
    @DisplayName("Update Skill - Case: Not a number")
    public void UpdateSkill400Format() throws Exception {
        mockMvc.perform(put("/api/skill/aa")).andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Delete Skill - Case: Positive")
    public void deleteSkillPositive() throws Exception {

        doNothing().when(skillService).delete(1L);

        mockMvc.perform(delete("/api/skill/1"))
                .andExpect(status().is(204));
    }
    @Test
    @DisplayName("Delete Skill - Case: Not a number")
    public void deleteSkill400Format() throws Exception {
        mockMvc.perform(delete("/api/skill/aa")).andExpect(status().isBadRequest());
    }
}
