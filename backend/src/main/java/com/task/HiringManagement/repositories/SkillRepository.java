package com.task.HiringManagement.repositories;

import com.task.HiringManagement.models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill,Long> {
}
