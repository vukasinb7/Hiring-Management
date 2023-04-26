package com.task.HiringManagement.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "candidates")
public class Candidate {
    @Column(name="full_name",nullable = false)
    private String name;
    @Column(name="birth",nullable = false)
    private LocalDateTime birth;
    @Column(name="contact_number",nullable = false)
    private String contactNumber;
    @Column(name="email",nullable = false)
    private String email;
    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.REFRESH)
    @JoinTable(name = "candidates_skills",joinColumns = @JoinColumn(name = "candidate_id",referencedColumnName = "id"),inverseJoinColumns = @JoinColumn(name = "skill_id",referencedColumnName = "id"))
    private List<Skill> skills;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

}
