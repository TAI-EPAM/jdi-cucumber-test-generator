package com.epam.test_generator.entities;


public enum Action {
    CREATE("create"),
    UPDATE("update"),
    DELETE("delete");


    private String actionName;

    Action(String actionName) {
        this.actionName = actionName;
    }
}
