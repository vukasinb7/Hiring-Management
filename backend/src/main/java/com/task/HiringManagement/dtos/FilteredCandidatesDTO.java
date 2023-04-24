package com.task.HiringManagement.dtos;

import com.task.HiringManagement.mappers.CandidateMapper;
import com.task.HiringManagement.models.Candidate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class FilteredCandidatesDTO {
    private long totalCount;
    private List<GetCandidateDTO> candidates;
    public FilteredCandidatesDTO(Page<Candidate> candidates){
        this.candidates=candidates.stream().map(CandidateMapper::fromModeltoGetDTO).toList();
        this.totalCount=candidates.getTotalElements();
    }
}
