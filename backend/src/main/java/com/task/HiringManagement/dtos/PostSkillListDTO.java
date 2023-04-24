package com.task.HiringManagement.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class PostSkillListDTO {
    private List<Long> skillIds;
}
