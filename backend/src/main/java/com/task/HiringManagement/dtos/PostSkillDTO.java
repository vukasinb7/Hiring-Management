package com.task.HiringManagement.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PostSkillDTO {
    @NotBlank(message = "Field (name) is required")
    @Size(max = 100,message = "Field name cannot be longer than 100 characters")
    private String name;
}
