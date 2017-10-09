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
import java.util.Arrays;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes={DatabaseConfigForTests.class})
@Transactional
public class StepDAOTest {

    @Autowired
    StepDAO stepDAO;

    @Test
    public void testCreateAndRetrieve() {
        Step step = new Step();
        step.setDescription("Step description");
        step.setRowNumber(5);
        step.setType(1);
        long id = stepDAO.save(step).getId();

        Assert.assertEquals(step, stepDAO.findOne(id));
    }

    @Test
    public void testUpdateDescription() {
        Step step = new Step();
        step.setDescription("Step description");
        step.setRowNumber(5);
        step.setType(1);
        stepDAO.save(step);
        step.setDescription("Modified description");
        long id = stepDAO.save(step).getId();

        Assert.assertEquals(step, stepDAO.findOne(id));
    }

    @Test
    public void testUpdateRowNumber() {
        Step step = new Step();
        step.setDescription("Step description");
        step.setRowNumber(5);
        step.setType(1);
        stepDAO.save(step);
        step.setRowNumber(3);
        long id = stepDAO.save(step).getId();

        Assert.assertEquals(step, stepDAO.findOne(id));
    }

    @Test
    public void testUpdateType() {
        Step step = new Step();
        step.setDescription("Step description");
        step.setRowNumber(5);
        step.setType(1);
        stepDAO.save(step);
        step.setType(2);
        long id = stepDAO.save(step).getId();

        Assert.assertEquals(step, stepDAO.findOne(id));
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

        Step[] steps = {step1, step2, step3};

        stepDAO.save(Arrays.asList(steps));

        Assert.assertEquals(stepDAO.findAll(), Arrays.asList(steps));
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
