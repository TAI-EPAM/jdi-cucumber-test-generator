package com.epam.test_generator.services;

import com.epam.test_generator.config.security.AuthenticatedUser;
import com.epam.test_generator.controllers.test.result.TestResultTransformer;
import com.epam.test_generator.dao.interfaces.TestResultDAO;
import com.epam.test_generator.dto.RawSuitResultDTO;
import com.epam.test_generator.controllers.test.result.response.TestResultDTO;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.results.TestResult;
import com.epam.test_generator.entities.factory.TestResultFactory;
import com.epam.test_generator.services.exceptions.BadRequestException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class TestResultService {

    @Autowired
    private TestResultFactory testResultFactory;

    @Autowired
    private TestResultDAO testResultDAO;

    @Autowired
    private TestResultTransformer testResultTransformer;

    /**
     * Get all test results by projectId.
     *
     * @param projectId identifier of {@link Project}
     * @return list of {@link TestResult}
     */
    public List<TestResultDTO> getTestResults(long projectId) {
        return testResultDAO.findAllByProjectIdOrderByDateDesc(projectId).stream()
            .map(testResultTransformer::toDto)
            .collect(Collectors.toList());
    }


    /**
     * Get limited amount of test results.
     *
     * @param projectId identifier of {@link Project}
     * @param offset quantity of elements to skip
     * @param limit required elements quantity.
     * @return list of {@link TestResult}
     */
    public List<TestResultDTO> getTestResults(long projectId, int offset, int limit) {
        return testResultDAO.findAllByProjectIdOrderByDateDesc(projectId).stream()
            .skip(offset)
            .limit(limit)
            .map(testResultTransformer::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Get test results which dates are lean between from and to.
     *
     * @param projectId identifier of {@link Project}
     * @param from every date of every element has to be after than this.
     * @param to every date of every element has to be before than this.
     * @return list of {@link TestResult}
     */
    public List<TestResultDTO> getTestResults(long projectId, ZonedDateTime from, ZonedDateTime to) {
        if (from == null || to == null) {
            throw new BadRequestException("Illegal argument");
        }
        return testResultDAO.findAllByProjectIdAndDateAfterAndDateBefore(projectId, from, to)
            .stream()
            .map(testResultTransformer::toDto)
            .collect(Collectors.toList());
    }

    /**
     * Parse all crucial information from List of {@link RawSuitResultDTO} and collect it into
     * {@link TestResult} struct class.
     *
     * @param projectId identifier of {@link Project}
     * @param suitResultDTOS List of {@link RawSuitResultDTO}.
     * @param authentication current user principals
     * @return {@link TestResult}
     */
    public TestResult saveResult(Long projectId, List<RawSuitResultDTO> suitResultDTOS,
                                 Authentication authentication) {

        String executedBy = getEmailFrom(authentication);

        TestResult testResult = testResultFactory.createTestResultFrom(projectId, executedBy,
            suitResultDTOS);

        return testResultDAO.save(testResult);
    }

    private String getEmailFrom(Authentication authentication) {
        return ((AuthenticatedUser) authentication.getPrincipal()).getEmail();
    }
}
