package com.epam.test_generator.dao.interfaces;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.results.TestResult;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.transaction.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {DatabaseConfigForTests.class})
@Transactional
public class TestResultDAOTest {

    @Autowired
    private TestResultDAO testResultDAO;

    @Autowired
    private ProjectDAO projectDAO;

    private Project currentProject;

    private List<TestResult> testResults;

    private TestResult extraTestResult;


    @Before
    public void setUp() {
        currentProject = projectDAO.save(new Project());
        Project anotherProject = projectDAO.save(new Project());

        extraTestResult = newTestResult();
        extraTestResult.setProject(anotherProject);

        testResults = Stream.generate(this::newTestResult).limit(10).collect(Collectors.toList());
    }

    @Test
    public void save() {
        List<TestResult> save = testResultDAO.saveAll(testResults);
        assertThat(save.size(), is(equalTo(10)));
    }

    @Test
    public void findAllByProjectIdOrderByDateDesc() {
        List<TestResult> save = testResultDAO.saveAll(testResults);
        assertThat(save.size(), is(equalTo(10)));

        Long projectId = save.get(0).getProject().getId();
        assertThat(projectId, is(notNullValue()));


        testResultDAO.save(extraTestResult);


        List<TestResult> all = testResultDAO.findAll();
        assertThat(all.size(), is(equalTo(11)));


        List<TestResult> allByProjectIdOrderByDateDesc =
            testResultDAO.findAllByProjectIdOrderByDateDesc(projectId);
        assertThat(allByProjectIdOrderByDateDesc.size(), is(equalTo(10)));

    }

    @Test
    public void findAllByProjectIdAndDateBetweenOrderByDateDesc() {
        List<TestResult> save = testResultDAO.saveAll(testResults);
        assertThat(save.size(), is(equalTo(10)));

        Long projectId = save.get(0).getProject().getId();
        assertThat(projectId, is(notNullValue()));

        TestResult testWithDateBefore =
            createTestResultOf(ZonedDateTime.parse("2018-03-01T00:00Z"), currentProject);
        TestResult testWithDateAfter =
            createTestResultOf(ZonedDateTime.parse("2018-01-01T00:00Z"), currentProject);

        testResultDAO.save(testWithDateBefore);
        testResultDAO.save(testWithDateAfter);

        List<TestResult> all = testResultDAO.findAll();

        assertThat(all.size(), is(equalTo(12)));

        ZonedDateTime beforeDate = testWithDateBefore.getDate();
        ZonedDateTime afterDate = testWithDateAfter.getDate();

        List<TestResult> allByProjectIdAndDateIsBetween = testResultDAO
            .findAllByProjectIdAndDateAfterAndDateBefore(projectId, afterDate, beforeDate);

        assertThat(allByProjectIdAndDateIsBetween.size(), is(equalTo(10)));

    }

    private TestResult createTestResultOf(ZonedDateTime date, Project project) {

        TestResult result = newTestResult();
        result.setProject(project);
        result.setDate(date);
        return result;
    }


    private TestResult newTestResult() {

        TestResult testResult = new TestResult();
        testResult.setDate(ZonedDateTime.parse("2018-02-24T00:00Z"));
        testResult.setDuration(0);
        testResult.setAmountOfPassed(1);
        testResult.setAmountOfSkipped(0);
        testResult.setAmountOfFailed(0);
        testResult.setExecutedBy("test@test.com");
        testResult.setStatus(Status.PASSED);
        testResult.setProject(currentProject);

        return testResult;
    }
}