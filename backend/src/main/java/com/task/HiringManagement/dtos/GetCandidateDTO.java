package com.task.HiringManagement.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.task.HiringManagement.models.Skill;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GetCandidateDTO {
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime birth;
    private String contactNumber;
    private String email;
    private List<Skill> skills;
    public Long id;
}
