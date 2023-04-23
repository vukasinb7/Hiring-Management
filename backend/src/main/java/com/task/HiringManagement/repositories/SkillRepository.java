package com.task.HiringManagement.repositories;

import com.task.HiringManagement.models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill,Long> {

    @Query(value = "SELECT skill from Skill skill WHERE lower(:skillName)=lower(skill.name)")
    Optional<Skill> findByName(@Param("skillName") String skillName);
}
