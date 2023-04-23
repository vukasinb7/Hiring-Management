package com.task.HiringManagement.controllers;

import com.task.HiringManagement.services.ICandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/candidate")
public class CandidateController {
    private final ICandidateService candidateService;

    @Autowired
    public CandidateController(ICandidateService candidateService)
    {
        this.candidateService=candidateService;
    }
}
