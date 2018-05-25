package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.StepType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DatabaseConfigForTests.class})
@Transactional
public class StepDAOTest {

    @Autowired
    StepDAO stepDAO;

    @Test
    public void createAndRetrieve_Step_Success() {
        Step originalStep = retrieveStep();
        long id = stepDAO.save(originalStep).getId();

        Step newStep = retrieveStep();
        newStep.setId(id);

        Assert.assertEquals(newStep, stepDAO.findById(id).orElse(null));
    }

    @Test
    public void updateDescription_Step_Success() {
        Step originalStep = retrieveStep();
        stepDAO.save(originalStep);
        originalStep.setDescription("Modified description");
        long id = stepDAO.save(originalStep).getId();

        Step newStep = retrieveStep();
        newStep.setId(id);
        newStep.setDescription("Modified description");
        Assert.assertEquals(newStep, stepDAO.findById(id).orElse(null));
    }

    @Test
    public void updateRowNumber_Step_Success() {
        Step originalStep = retrieveStep();

        stepDAO.save(originalStep);
        originalStep.setRowNumber(3);
        long id = stepDAO.save(originalStep).getId();

        Step newStep = retrieveStep();
        newStep.setId(id);
        newStep.setRowNumber(3);

        Assert.assertEquals(newStep, stepDAO.findById(id).orElse(null));
    }

    @Test
    public void updateType_Step_Success() {
        Step originalStep = retrieveStep();
        stepDAO.save(originalStep);
        originalStep.setType(StepType.THEN);
        long id = stepDAO.save(originalStep).getId();

        Step newStep = retrieveStep();
        newStep.setId(id);
        newStep.setType(StepType.THEN);

        Assert.assertEquals(newStep, stepDAO.findById(id).orElse(null));
    }

    @Test
    public void removeById_Step_Success() {
        Step step = retrieveStep();
        long id = stepDAO.save(step).getId();

        stepDAO.delete(step);

        Assert.assertFalse(stepDAO.existsById(id));
    }

    @Test
    public void remove_Step_Success() {
        Step step = retrieveStep();
        long id = stepDAO.save(step).getId();

        stepDAO.delete(step);

        Assert.assertFalse(stepDAO.existsById(id));
    }

    @Test
    public void addList_Steps_Success() {
        List<Step> steps = retrieveStepList();

        List<Long> ids = stepDAO.saveAll(steps).stream().map(Step::getId).collect(Collectors.toList
            ());

        List<Step> newSteps = retrieveStepList();
        newSteps.get(0).setId(ids.get(0));
        newSteps.get(1).setId(ids.get(1));
        newSteps.get(2).setId(ids.get(2));

        Assert.assertEquals(newSteps, stepDAO.findAll());
    }

    @Test
    public void removeList_Steps_Success() {
        List<Step> steps = retrieveStepList();

        stepDAO.saveAll(steps);

        stepDAO.deleteAll(steps);

        Assert.assertTrue(stepDAO.findAll().isEmpty());
    }

    private Step retrieveStep() {
        Step step = new Step();
        step.setDescription("Step description");
        step.setRowNumber(5);
        step.setType(StepType.WHEN);

        return step;
    }

    private List<Step> retrieveStepList() {
        Step step1 = new Step();
        step1.setDescription("Step1 description");
        step1.setRowNumber(5);
        step1.setType(StepType.WHEN);

        Step step2 = new Step();
        step2.setDescription("Step2 description");
        step2.setRowNumber(6);
        step2.setType(StepType.WHEN);

        Step step3 = new Step();
        step3.setDescription("Step3 description");
        step3.setRowNumber(7);
        step3.setType(StepType.WHEN);

        ArrayList<Step> steps = new ArrayList<>();
        steps.add(step1);
        steps.add(step2);
        steps.add(step3);

        return steps;
    }
}
