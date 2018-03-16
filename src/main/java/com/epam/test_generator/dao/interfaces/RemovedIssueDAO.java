package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.entities.RemovedIssue;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * This DAO is used to save suits and cases that were deleted from the system and should be
 * closed in Jira after next synchronization from system to Jira.
 */
public interface RemovedIssueDAO extends JpaRepository<RemovedIssue, Long> {
}
