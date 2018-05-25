package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.entities.DefaultStepSuggestion;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DefaultStepSuggestionDAO extends JpaRepository<DefaultStepSuggestion, Long> {

    List<DefaultStepSuggestion> findByContentIgnoreCaseContaining(String searchString,
                                                                  Pageable numberOfReturnedResults);
}
