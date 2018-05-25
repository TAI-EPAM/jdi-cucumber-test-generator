package com.epam.test_generator.state.machine;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.test_generator.config.StateMachineConfig;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Event;
import com.epam.test_generator.entities.Status;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;


@RunWith(MockitoJUnitRunner.class)
public class StateMachineAdapterTest {

    @InjectMocks
    private StateMachineAdapter stateMachineAdapter;

    @Mock
    private CaseStatePersister persister;

    @Mock
    private StateMachineFactory<Status, Event> stateMachineFactory;

    private StateMachine<Status, Event> stateMachine;

    private ApplicationContext applicationContext = new AnnotationConfigApplicationContext(
        StateMachineConfig.class);

    private Case cs;

    @Before
    public void setUp() {
        cs = new Case("name", "descr", new ArrayList<>(), 1, new HashSet<>(),
            Status.NOT_RUN, "comment");
    }

    @Test
    public void restore_Case_Success() throws Exception {
        stateMachineAdapter.restore(cs);

        verify(persister).restore(eq(stateMachine), eq(cs));
    }

    @Test
    public void persist_Case_Success() throws Exception {
        stateMachineAdapter.persist(stateMachine, cs);

        verify(persister).persist(eq(stateMachine), eq(cs));
    }

    @Test
    public void availableTransitions_StatusNotDone_Success() {
        when(stateMachineFactory.getStateMachine())
            .thenReturn(applicationContext.getBean(StateMachineFactory.class).getStateMachine());
        Status status = Status.NOT_DONE;

        List<Event> expectedEvents = Collections.singletonList(Event.CREATE);

        Assert.assertEquals(expectedEvents, stateMachineAdapter.availableTransitions(status));
    }

    @Test
    public void availableTransitions_StatusNotRun_Success() {
        when(stateMachineFactory.getStateMachine())
            .thenReturn(applicationContext.getBean(StateMachineFactory.class).getStateMachine());
        Status status = Status.NOT_RUN;

        List<Event> expectedEvents = Arrays.asList(Event.PASS, Event.FAIL, Event.SKIP);

        Assert.assertEquals(expectedEvents, stateMachineAdapter.availableTransitions(status));
    }

    @Test
    public void availableTransitions_StatusPassed_Success() {
        when(stateMachineFactory.getStateMachine())
            .thenReturn(applicationContext.getBean(StateMachineFactory.class).getStateMachine());
        Status status = Status.PASSED;

        List<Event> expectedEvents = Collections.singletonList(Event.EDIT);
        ;

        Assert.assertEquals(expectedEvents, stateMachineAdapter.availableTransitions(status));
    }

    @Test
    public void availableTransitions_StatusFailed_Success() {
        when(stateMachineFactory.getStateMachine())
            .thenReturn(applicationContext.getBean(StateMachineFactory.class).getStateMachine());
        Status status = Status.FAILED;

        List<Event> expectedEvents = Collections.singletonList(Event.EDIT);
        ;

        Assert.assertEquals(expectedEvents, stateMachineAdapter.availableTransitions(status));
    }

    @Test
    public void availableTransitions_StatusSkipped_Success() {
        when(stateMachineFactory.getStateMachine())
            .thenReturn(applicationContext.getBean(StateMachineFactory.class).getStateMachine());
        Status status = Status.SKIPPED;

        List<Event> expectedEvents = Collections.singletonList(Event.EDIT);

        Assert.assertEquals(expectedEvents, stateMachineAdapter.availableTransitions(status));
    }
}