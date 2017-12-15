package com.epam.test_generator.entities;

public enum Event {
    CREATE("Create"),
    PASS("Pass"),
    FAIL("Fail"),
    SKIP("Skip"),
    EDIT("Edit");

    private String eventName;

    Event(String eventName) {
        this.eventName = eventName;
    }
}