package com.epam.test_generator.state.machine;

import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Event;
import com.epam.test_generator.entities.Status;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Component;

/**
 * Class that defines behavior of State Machines bind to case entity, such as persisting states to
 * case and performing events
 */
@Component
@WithStateMachine
public class StateMachineAdapter {

    /**
     * Factory that produces State Machines with settings from {@link org.springframework.statemachine.config.StateMachineConfig}
     */
    @Autowired
    private StateMachineFactory<Status, Event> stateMachineFactory;

    /**
     * Persists and restores state from class {@code <T>}
     */
    @Autowired
    private StateMachinePersister<Status, Event, Case> persister;

    public StateMachine<Status, Event> restore(Case contextObject) throws Exception {
        StateMachine<Status, Event> stateMachine = stateMachineFactory.getStateMachine();
        return persister.restore(stateMachine, contextObject);
    }

    public void persist(StateMachine<Status, Event> stateMachine, Case order) throws Exception {
        persister.persist(stateMachine, order);
    }

    /**
     * @return List of transitions applicable to given state
     */
    public List<Event> availableTransitions(Status state) {
        StateMachine<Status, Event> stateMachine = stateMachineFactory.getStateMachine();

        return stateMachine.getTransitions().stream()
            .filter(transition -> state.equals(transition.getSource().getId()))
            .map(transition -> transition.getTrigger().getEvent()).collect(
                Collectors.toList());
    }
}
