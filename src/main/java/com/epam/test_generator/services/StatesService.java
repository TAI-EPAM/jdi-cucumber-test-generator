package com.epam.test_generator.services;

import com.epam.test_generator.entities.Event;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.state.machine.StateMachineAdapter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatesService {

    @Autowired
    private StateMachineAdapter stateMachineAdapter;

    /**
     * @return List of transitions applicable to given state
     */
    public List<Event> availableTransitions(Status status) {

        return stateMachineAdapter.availableTransitions(status);
    }
}
