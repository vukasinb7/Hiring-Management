package com.task.HiringManagement.dtos;

import com.task.HiringManagement.models.Candidate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.Converter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.function.Function;

@Getter @Setter
@NoArgsConstructor
public class FilteredCandidatesDTO {
    private long totalCount;
    private List<GetCandidateDTO> candidates;
    public FilteredCandidatesDTO(Page<Candidate> candidates){

        this.candidates=candidates.map(new Function<Candidate,GetCandidateDTO>() {
            @Override
            public GetCandidateDTO apply(Candidate candidate) {
                return new GetCandidateDTO(candidate.getName(), candidate.getBirth(), candidate.getContactNumber(), candidate.getEmail(), candidate.getSkills(), candidate.getId());

            }
        }).stream().toList();
        this.totalCount=candidates.getTotalElements();
    }
}
