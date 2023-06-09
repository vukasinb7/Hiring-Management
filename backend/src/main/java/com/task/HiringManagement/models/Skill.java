package com.task.HiringManagement.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "skills")
@EqualsAndHashCode
public class Skill {
    @Column(name = "skill_name")
    private String name;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
}
