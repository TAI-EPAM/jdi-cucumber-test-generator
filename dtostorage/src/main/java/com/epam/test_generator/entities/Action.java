package com.epam.test_generator.entities;


/**
 * This class represent action type essence for cascade work with cases. It is used in case of work with multiple cases
 * at one period of time.
 */
public enum Action {
    CREATE("create"),
    UPDATE("update"),
    DELETE("delete");


    private String actionName;

    Action(String actionName) {
        this.actionName = actionName;
    }
}
