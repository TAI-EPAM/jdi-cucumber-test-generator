package com.epam.test_generator.dao;

import com.epam.test_generator.DatabaseConfigForTests;
import com.epam.test_generator.dao.interfaces.StepDAO;
import com.epam.test_generator.entities.Step;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={DatabaseConfigForTests.class})
@Transactional
public class StepDAOTest {

    @Autowired
    StepDAO stepDAO;

    @Test
    public void testCreateAndRetrieve() {
        Step originalStep = new Step();
        originalStep.setDescription("Step description");
        originalStep.setRowNumber(5);
        originalStep.setType(1);
        long id = stepDAO.save(originalStep).getId();

        Step newStep = new Step();
        newStep.setDescription("Step description");
        newStep.setRowNumber(5);
        newStep.setType(1);
        newStep.setId(id);

        Assert.assertEquals(newStep, stepDAO.findOne(id));
    }

    @Test
    public void testUpdateDescription() {
        Step originalStep = new Step();
        originalStep.setDescription("Step description");
        originalStep.setRowNumber(5);
        originalStep.setType(1);
        stepDAO.save(originalStep);
        originalStep.setDescription("Modified description");
        long id = stepDAO.save(originalStep).getId();

        Step newStep = new Step();
        newStep.setId(id);
        newStep.setDescription("Modified description");
        newStep.setRowNumber(5);
        newStep.setType(1);

        Assert.assertEquals(newStep, stepDAO.findOne(id));
    }

    @Test
    public void testUpdateRowNumber() {
        Step originalStep = new Step();
        originalStep.setDescription("Step description");
        originalStep.setRowNumber(5);
        originalStep.setType(1);
        stepDAO.save(originalStep);
        originalStep.setRowNumber(3);
        long id = stepDAO.save(originalStep).getId();

        Step newStep = new Step();
        newStep.setId(id);
        newStep.setDescription("Step description");
        newStep.setRowNumber(3);
        newStep.setType(1);

        Assert.assertEquals(newStep, stepDAO.findOne(id));
    }

    @Test
    public void testUpdateType() {
        Step originalStep = new Step();
        originalStep.setDescription("Step description");
        originalStep.setRowNumber(5);
        originalStep.setType(1);
        stepDAO.save(originalStep);
        originalStep.setType(2);
        long id = stepDAO.save(originalStep).getId();

        Step newStep = new Step();
        newStep.setId(id);
        newStep.setDescription("Step description");
        newStep.setRowNumber(5);
        newStep.setType(2);

        Assert.assertEquals(newStep, stepDAO.findOne(id));
    }

    @Test
    public void testRemoveById() {
        Step step = new Step();
        step.setDescription("Step description");
        step.setRowNumber(5);
        step.setType(1);
        long id = stepDAO.save(step).getId();

        stepDAO.delete(id);

        Assert.assertTrue(!stepDAO.exists(id));
    }

    @Test
    public void testRemove() {
        Step step = new Step();
        step.setDescription("Step description");
        step.setRowNumber(5);
        step.setType(1);
        long id = stepDAO.save(step).getId();

        stepDAO.delete(step);

        Assert.assertTrue(!stepDAO.exists(id));
    }

    @Test
    public void testAddList() {
        Step step1 = new Step();
        step1.setDescription("Step1 description");
        step1.setRowNumber(5);
        step1.setType(1);

        Step step2 = new Step();
        step2.setDescription("Step2 description");
        step2.setRowNumber(6);
        step2.setType(1);

        Step step3 = new Step();
        step3.setDescription("Step3 description");
        step3.setRowNumber(7);
        step3.setType(2);

        ArrayList<Step> steps = new ArrayList<>();
        steps.add(step1);
        steps.add(step2);
        steps.add(step3);

        List<Long> ids = stepDAO.save(steps).stream().map(Step::getId).collect(Collectors.toList());

        Step step4 = new Step();
        step4.setId(ids.get(0));
        step4.setDescription("Step1 description");
        step4.setRowNumber(5);
        step4.setType(1);

        Step step5 = new Step();
        step5.setId(ids.get(1));
        step5.setDescription("Step2 description");
        step5.setRowNumber(6);
        step5.setType(1);

        Step step6 = new Step();
        step6.setId(ids.get(2));
        step6.setDescription("Step3 description");
        step6.setRowNumber(7);
        step6.setType(2);

        ArrayList<Step> newSteps = new ArrayList<>();
        newSteps.add(step4);
        newSteps.add(step5);
        newSteps.add(step6);

        Assert.assertTrue(newSteps.equals(stepDAO.findAll()));
    }

    @Test
    public void testRemoveList() {
        Step step1 = new Step();
        step1.setDescription("Step1 description");
        step1.setRowNumber(5);
        step1.setType(1);

        Step step2 = new Step();
        step2.setDescription("Step2 description");
        step2.setRowNumber(6);
        step2.setType(1);

        Step step3 = new Step();
        step3.setDescription("Step3 description");
        step3.setRowNumber(7);
        step3.setType(2);

        Step[] steps = {step1, step2, step3};

        stepDAO.save(Arrays.asList(steps));

        stepDAO.delete(Arrays.asList(steps));

        Assert.assertTrue(stepDAO.findAll().isEmpty());
    }
}
