package com.task.HiringManagement.services;

import com.task.HiringManagement.repositories.CandidateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CandidateService implements ICandidateService {

    private final CandidateRepository candidateRepository;

    @Autowired
    public CandidateService(CandidateRepository candidateRepository){
        this.candidateRepository=candidateRepository;
    }
}
