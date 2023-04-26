package com.task.HiringManagement.controllers;

import com.task.HiringManagement.dtos.*;
import com.task.HiringManagement.models.Candidate;
import com.task.HiringManagement.models.Skill;
import com.task.HiringManagement.services.ICandidateService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidate")
public class CandidateController {
    private final ICandidateService candidateService;
    private ModelMapper modelMapper;

    @Autowired
    public CandidateController(ICandidateService candidateService,ModelMapper modelMapper)
    {
        this.candidateService=candidateService;
        this.modelMapper=modelMapper;
    }
    @Operation(summary = "Create Candidate")
    @PostMapping(
            value = "",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GetCandidateDTO> createCandidate(@RequestBody @Valid PostCandidateDTO candidateDTO){
        Candidate candidate=candidateService.insert(candidateDTO);
        return new ResponseEntity<>(modelMapper.map(candidate,GetCandidateDTO.class), HttpStatus.OK);
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

    @Operation(summary = "Get All Candidates")
    @GetMapping(
            value = "/all",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<FilteredCandidatesDTO> getAllCandidates(@Min(value=0, message = "Page must be 0 or greater")
                                                                 @NotNull(message = "Field (page) is required")
                                                                 @RequestParam int page,
                                                                 @Positive(message = "Size must be positive")
                                                                 @NotNull(message = "Field (size) is required")
                                                                 @RequestParam int size){
        Page<Candidate> candidates = candidateService.getAll(PageRequest.of(page, size));
        return new ResponseEntity<>(new FilteredCandidatesDTO(candidates), HttpStatus.OK);
    }

    @Operation(summary = "Get Candidate")
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<GetCandidateDTO> getCandidateByID(@NotNull @Positive @PathVariable("id") Long id){
       Candidate candidate = candidateService.get(id);
        return new ResponseEntity<>(modelMapper.map(candidate,GetCandidateDTO.class), HttpStatus.OK);
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
                                                                   @RequestParam List<String> skills){
        Page<Candidate> candidates = candidateService.searchBySkills(skills, PageRequest.of(page, size));
        return new ResponseEntity<>(new FilteredCandidatesDTO(candidates), HttpStatus.OK);
    }

    @Operation(summary = "Search Candidate By Skills")
    @GetMapping(
            value = "/search",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    ResponseEntity<FilteredCandidatesDTO> searchCandidatesComplex(@Min(value=0, message = "Page must be 0 or greater")
                                                                   @NotNull(message = "Field (page) is required")
                                                                   @RequestParam int page,
                                                                   @Positive(message = "Size must be positive")
                                                                   @NotNull(message = "Field (size) is required")
                                                                   @RequestParam int size,
                                                                   @RequestParam List<String> skills,
                                                                   @RequestParam String name){
        Page<Candidate> candidates = candidateService.searchComplex(skills,name,PageRequest.of(page, size));
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
        return new ResponseEntity<>(modelMapper.map(candidate,GetCandidateDTO.class), HttpStatus.OK);
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
        return new ResponseEntity<>(modelMapper.map(candidate,GetCandidateDTO.class), HttpStatus.OK);
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
        return new ResponseEntity<>(modelMapper.map(candidate,GetCandidateDTO.class), HttpStatus.OK);
    }

    @Operation(summary = "Update Skills Candidate")
    @PutMapping(
            value = "/skills/update/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<GetCandidateDTO> updateSkills(@RequestBody @Valid PostSkillListDTO skillListDTO, @NotNull(message = "Field (id) is required")
    @Positive(message = "Id must be positive")
    @PathVariable Long id){
        Candidate candidate= candidateService.updateSkills(skillListDTO,id);
        return new ResponseEntity<>(modelMapper.map(candidate,GetCandidateDTO.class), HttpStatus.OK);
    }

}
