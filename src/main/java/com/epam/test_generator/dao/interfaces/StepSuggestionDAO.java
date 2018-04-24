package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.entities.StepSuggestion;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StepSuggestionDAO extends JpaRepository<StepSuggestion, Long> {

    List<StepSuggestion> findByContentIgnoreCaseContaining(String searchString,
                                                           Pageable numberOfReturnedResults);
}
