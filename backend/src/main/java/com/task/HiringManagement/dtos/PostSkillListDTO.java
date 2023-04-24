package com.task.HiringManagement.dtos;

import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PostSkillListDTO {
    private List<Long> skillIds;
}
