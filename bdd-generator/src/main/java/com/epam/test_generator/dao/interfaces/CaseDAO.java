package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.entities.Case;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CaseDAO extends JpaRepository<Case, Long> {
    Case findByJiraKey(String jiraKey);
    List<Case> findByIdInOrderById(Collection<Long> ids);
}