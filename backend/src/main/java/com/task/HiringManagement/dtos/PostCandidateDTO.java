package com.task.HiringManagement.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PostCandidateDTO {

    @NotBlank(message = "Field (name) is required!")
    @Size(max = 50, message = "Field (name) cannot be longer than 50 characters!")
    private String name;


    @Past(message = "Date of birth must be in past")
    private LocalDateTime birth;

    @NotBlank(message = "Field (contactNumber) is required!")
    @Pattern(regexp = "^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$",message = "Phone number is not valid")
    private String contactNumber;

    @NotBlank(message = "Field (email) is required!")
    @Email
    private String email;


    private List<Long> skillIds;
}
