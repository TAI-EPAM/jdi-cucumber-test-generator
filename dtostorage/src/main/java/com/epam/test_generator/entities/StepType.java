package com.epam.test_generator.entities;


/**
 * This enum represents type of step essence. StepType is a set of possible values that describe steps' classes.
 * Each step can be related to one of these possible classes based on it's action description or input parameters.
 */
public enum StepType {

    GIVEN("Given"),
    WHEN("When"),
    THEN("Then"),
    AND("And"),
    BUT("But"),
    ANY("*");

    private String typeName;

    StepType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return typeName;
    }
}
