package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.entities.results.TestResult;
import java.time.ZonedDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestResultDAO extends JpaRepository<TestResult, Long> {

    /**
     * @param projectId identifier of {@link com.epam.test_generator.entities.Project}
     * @return Find all test results by projectId and make descending order by date.
     */
    List<TestResult> findAllByProjectIdOrderByDateDesc(long projectId);

    /**
     * Note: because of "between" method doesn't work for some unknown reason,
     * we use "after" and "before" instead of this.
     *
     * @param projectId identifier of {@link com.epam.test_generator.entities.Project}
     * @param after Find all test results which are after than this.
     * @param before Find all test results which are before than this.
     * @return Find all test results by projectId and which dates are lean between after and before.
     */
    List<TestResult> findAllByProjectIdAndDateAfterAndDateBefore(long projectId, ZonedDateTime after, ZonedDateTime before);

}
