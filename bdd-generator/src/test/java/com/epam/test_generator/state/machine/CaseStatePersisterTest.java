package com.epam.test_generator.state.machine;

import static org.mockito.Mockito.when;

import com.epam.test_generator.config.StateMachineConfig;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Event;
import com.epam.test_generator.entities.Status;
import java.util.ArrayList;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.state.State;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {StateMachineConfig.class})
public class CaseStatePersisterTest {

    @Mock
    private StateMachine<Status, Event> stateMachine;

    @Mock
    private State<Status, Event> state;

    @Autowired
    private StateMachineFactory<Status, Event> stateMachineFactory;

    private Case cs;

    private CaseStatePersister persister;

    @Before
    public void onStartUp() {
        persister = new CaseStatePersister();
        cs = new Case("name", "descr", new ArrayList<>(), 1, new HashSet<>(),
            Status.NOT_RUN, "comment");
    }

    @Test
    public void persist_StateMachine_Success() {
        when(stateMachine.getState()).thenReturn(state);
        when(state.getId()).thenReturn(Status.PASSED);
        persister.persist(stateMachine, cs);

        Assert.assertEquals(cs.getStatus(), Status.PASSED);
    }

    @Test
    public void restore_StatusNotRun_Success() {
        StateMachine<Status, Event> stateMachine1 = stateMachineFactory.getStateMachine();

        cs.setStatus(Status.NOT_RUN);
        Status status = persister.restore(stateMachine1, cs).getState().getId();

        Assert.assertEquals(Status.NOT_RUN, status);
    }
}