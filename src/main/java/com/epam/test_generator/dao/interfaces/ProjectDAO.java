package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectDAO extends JpaRepository<Project, Long> {
    List<Project> findByUsers(User user);
    Project findByJiraKey(String jiraKey);
}
