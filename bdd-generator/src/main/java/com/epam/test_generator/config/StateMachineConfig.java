package com.epam.test_generator.config;

import com.epam.test_generator.entities.Event;
import com.epam.test_generator.entities.Status;
import java.util.EnumSet;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachineFactory
public class StateMachineConfig extends EnumStateMachineConfigurerAdapter<Status, Event> {

    @Override
    public void configure(StateMachineConfigurationConfigurer<Status, Event> config)
        throws Exception {
        config
            .withConfiguration()
            .autoStartup(true);
    }

    /**
     * Defines states that configurable State Machine can take
     *
     * @param states enum that represents list of states
     */
    @Override
    public void configure(StateMachineStateConfigurer<Status, Event> states) throws Exception {
        states.withStates()
            .initial(Status.NOT_DONE)
            .states(EnumSet.allOf(Status.class));
    }

    /**
     * Configures logic of State Machine: defines how transitions change state.
     *
     * @param transitions enum that represents list of transitions
     */
    @Override
    public void configure(StateMachineTransitionConfigurer<Status, Event> transitions)
        throws Exception {
        transitions.withExternal()
            .source(Status.NOT_DONE).target(Status.NOT_RUN)
            .event(Event.CREATE)
            .and()
            .withExternal()
            .source(Status.NOT_RUN).target(Status.PASSED)
            .event(Event.PASS)
            .and()
            .withExternal()
            .source(Status.NOT_RUN).target(Status.FAILED)
            .event(Event.FAIL)
            .and()
            .withExternal()
            .source(Status.NOT_RUN).target(Status.SKIPPED)
            .event(Event.SKIP)
            .and()
            .withExternal()
            .source(Status.PASSED).target(Status.NOT_RUN)
            .event(Event.EDIT)
            .and()
            .withExternal()
            .source(Status.FAILED).target(Status.NOT_RUN)
            .event(Event.EDIT)
            .and()
            .withExternal()
            .source(Status.SKIPPED).target(Status.NOT_RUN)
            .event(Event.EDIT);
    }
}
