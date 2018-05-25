package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.entities.Suit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface SuitDAO extends JpaRepository<Suit, Long> {
    List<Suit> findByIdInOrderById(Collection<Long> ids);
    Suit findByJiraKey(String jiraKey);
}