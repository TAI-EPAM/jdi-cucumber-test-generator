package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.entities.StepSuggestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectStepSuggestionDAO extends JpaRepository<StepSuggestion, Long> {
}
