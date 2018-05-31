package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.StepSuggestion;
import com.epam.test_generator.entities.StepType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectStepSuggestionDAO extends JpaRepository<StepSuggestion, Long> {
    Page<StepSuggestion> findByProjectAndType(Project project, StepType type, Pageable pageable);

    Page<StepSuggestion> findByProject(Project project, Pageable pageable);
}
