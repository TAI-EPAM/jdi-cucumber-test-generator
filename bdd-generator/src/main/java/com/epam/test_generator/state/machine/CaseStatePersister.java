package com.epam.test_generator.state.machine;

import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Event;
import com.epam.test_generator.entities.Status;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.annotation.WithStateMachine;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

/**
 * Defines how persisting works with {@link Case} as class that stores {@link Status} (State Machine
 * state)
 */
@Component
@WithStateMachine
public class CaseStatePersister implements StateMachinePersister<Status, Event, Case> {

    /**
     * Saves current state of stateMachine to case
     *
     * @param stateMachine to get state from
     * @param contextObj to store given state
     */
    @Override
    public void persist(StateMachine<Status, Event> stateMachine, Case contextObj) {
        contextObj.setStatus(stateMachine.getState().getId());
    }

    /**
     * Restore state from case
     *
     * @param stateMachine to update state
     * @param contextObj that stores state
     * @return state machine with state from case
     */
    @Override
    public StateMachine<Status, Event> restore(StateMachine<Status, Event> stateMachine,
                                               Case contextObj) {
        stateMachine.getStateMachineAccessor().
            doWithRegion(access -> access.resetStateMachine(
                new DefaultStateMachineContext<>(contextObj.getStatus(), null,
                    null, null)));

        return stateMachine;
    }
}
