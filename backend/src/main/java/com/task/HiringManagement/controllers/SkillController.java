package com.task.HiringManagement.controllers;

import com.task.HiringManagement.dtos.GetSkillDTO;
import com.task.HiringManagement.dtos.PostSkillDTO;
import com.task.HiringManagement.models.Skill;
import com.task.HiringManagement.services.ISkillService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/skill")
public class SkillController {
    private final ISkillService skillService;
    private ModelMapper modelMapper;


    @Autowired
    public SkillController(ISkillService skillService,ModelMapper modelMapper){
        this.skillService=skillService;
        this.modelMapper=modelMapper;
    }
    @Operation(summary = "Create Skill")
    @PostMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GetSkillDTO> createSkill(@RequestBody @Valid PostSkillDTO skillDTO){
        Skill skill = skillService.insert(skillDTO);
        return new ResponseEntity<>(modelMapper.map(skill,GetSkillDTO.class), HttpStatus.OK);
    }
    @Operation(summary = "Get Skill")
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GetSkillDTO> getSkill(@NotNull(message = "Field (id) is required")
                                                        @Positive(message = "Id must be positive")
                                                        @PathVariable Long id){
        Skill skill= skillService.get(id);
        return new ResponseEntity<>(modelMapper.map(skill,GetSkillDTO.class), HttpStatus.OK);
    }

    @Operation(summary = "Get All Skills")
    @GetMapping(
            value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<GetSkillDTO>> getAll(){
        List<Skill> skills= skillService.getAll();
        List<GetSkillDTO> dtos = skills
                .stream()
                .map(skill -> modelMapper.map(skill, GetSkillDTO.class))
                .toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @Operation(summary = "Update Skill")
    @PutMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GetSkillDTO> updateSkill(@RequestBody @Valid PostSkillDTO skillDTO,@NotNull(message = "Field (id) is required")
                                                       @Positive(message = "Id must be positive")
                                                       @PathVariable Long id){
        Skill skill= skillService.update(skillDTO,id);
        return new ResponseEntity<>(modelMapper.map(skill,GetSkillDTO.class), HttpStatus.OK);
    }
    @Operation(summary = "Delete Skill")
    @DeleteMapping(
            value = "/{id}"
    )
    ResponseEntity<Void> deleteSkill(@PathVariable(name = "id")
                                        @NotNull(message = "Field (id) is required")
                                        @Positive(message = "Id must be positive") Long id){
        skillService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
