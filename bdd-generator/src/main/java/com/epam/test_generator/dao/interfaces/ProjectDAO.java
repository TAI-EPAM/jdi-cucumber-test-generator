package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.entities.User;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectDAO extends JpaRepository<Project, Long> {
    List<Project> findByUsers(User user);
    Project findByJiraKey(String jiraKey);

    /**
     * Get all tags which are used in this project at the moment by
     * collecting all tags form all suits and all cases of this project
     * @param projectId - id of project
     * @return set of all tags in a project
     */
    @Query(value ="select tag from(select * from (select suits_id "
        + "from project_suit where project_id=?1)  as suits "
        + "join suit_tags on suits.suits_id = "
        + "suit_tags.suit_id)"
        + " union "
        + "select tag from (select cases_id from (select "
        + "suits_id from project_suit where project_id=?1) as "
        + "project_suits join suit_case on  "
        + "project_suits.suits_id=suit_case.suit_id) as "
        + "suits_cases join case_tags on "
        + "suits_cases.cases_id=case_tags.case_id", nativeQuery = true)
    Set<Tag> getTagsByProjectId(Long projectId);
}
