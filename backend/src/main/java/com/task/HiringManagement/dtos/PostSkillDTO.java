package com.task.HiringManagement.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostSkillDTO {
    @NotBlank(message = "Field (name) is required")
    @Size(max = 100,message = "Field name cannot be longer than 100 characters")
    private String name;
}
