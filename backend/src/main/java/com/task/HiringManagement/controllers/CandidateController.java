package com.task.HiringManagement.controllers;

import com.task.HiringManagement.dtos.*;
import com.task.HiringManagement.mappers.CandidateMapper;
import com.task.HiringManagement.mappers.SkillMapper;
import com.task.HiringManagement.models.Candidate;
import com.task.HiringManagement.models.Skill;
import com.task.HiringManagement.services.ICandidateService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/candidate")
public class CandidateController {
    private final ICandidateService candidateService;

    @Autowired
    public CandidateController(ICandidateService candidateService)
    {
        this.candidateService=candidateService;
    }
    @Operation(summary = "Create Candidate")
    @PostMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GetCandidateDTO> createCandidate(@RequestBody @Valid PostCandidateDTO candidateDTO){
        Candidate candidate=candidateService.insert(candidateDTO);
        return new ResponseEntity<>(CandidateMapper.fromModeltoGetDTO(candidate), HttpStatus.OK);
    }
    @Operation(summary = "Search Candidate By Name")
    @GetMapping(
            value = "/searchName",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<FilteredCandidatesDTO> searchCandidatesByName(@Min(value=0, message = "Page must be 0 or greater")
                                           @NotNull(message = "Field (page) is required")
                                           @RequestParam int page,
                                                                 @Positive(message = "Size must be positive")
                                           @NotNull(message = "Field (size) is required")
                                           @RequestParam int size,
                                                                 @RequestParam String name){
        Page<Candidate> candidates = candidateService.searchByName(name, PageRequest.of(page, size));
        return new ResponseEntity<>(new FilteredCandidatesDTO(candidates), HttpStatus.OK);
    }
    @Operation(summary = "Search Candidate By Skills")
    @GetMapping(
            value = "/searchSkills",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<FilteredCandidatesDTO> searchCandidatesBySkills(@Min(value=0, message = "Page must be 0 or greater")
                                                                 @NotNull(message = "Field (page) is required")
                                                                 @RequestParam int page,
                                                                   @Positive(message = "Size must be positive")
                                                                 @NotNull(message = "Field (size) is required")
                                                                 @RequestParam int size,
                                                                   @RequestBody PostSkillListDTO postSkillListDTO){
        Page<Candidate> candidates = candidateService.searchBySkills(postSkillListDTO, PageRequest.of(page, size));
        return new ResponseEntity<>(new FilteredCandidatesDTO(candidates), HttpStatus.OK);
    }
    @Operation(summary = "Delete Candidate")
    @DeleteMapping(
            value = "/{id}"
    )
    ResponseEntity<Void> deleteSkill(@PathVariable(name = "id")
                                     @NotNull(message = "Field (id) is required")
                                     @Positive(message = "Id must be positive") Long id){
        candidateService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @Operation(summary = "Update Candidate's Personal Information")
    @PutMapping(
            value = "/update/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GetCandidateDTO> updatePersonalInfo(@RequestBody @Valid PostCandidateDTO candidateDTO, @NotNull(message = "Field (id) is required")
    @Positive(message = "Id must be positive")
    @PathVariable Long id){
        Candidate candidate= candidateService.updatePersonalInformations(candidateDTO,id);
        return new ResponseEntity<>(CandidateMapper.fromModeltoGetDTO(candidate), HttpStatus.OK);
    }
    @Operation(summary = "Add Skills To Candidate")
    @PutMapping(
            value = "/skills/add/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GetCandidateDTO> addSkills(@RequestBody @Valid PostSkillListDTO skillListDTO, @NotNull(message = "Field (id) is required")
    @Positive(message = "Id must be positive")
    @PathVariable Long id){
        Candidate candidate= candidateService.addSkills(skillListDTO,id);
        return new ResponseEntity<>(CandidateMapper.fromModeltoGetDTO(candidate), HttpStatus.OK);
    }
    @Operation(summary = "Remove Skills From Candidate")
    @PutMapping(
            value = "/skills/remove/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GetCandidateDTO> removeSkills(@RequestBody @Valid PostSkillListDTO skillListDTO, @NotNull(message = "Field (id) is required")
    @Positive(message = "Id must be positive")
    @PathVariable Long id){
        Candidate candidate= candidateService.removeSkills(skillListDTO,id);
        return new ResponseEntity<>(CandidateMapper.fromModeltoGetDTO(candidate), HttpStatus.OK);
    }

}
