package com.epam.test_generator.entities.api;

public interface ProjectTrait {

    void setActive(boolean active);

    boolean isActive();

    Long getId();

    default void deactivate() {
        setActive(false);
    }

    default void activate(){
        setActive(true);
    }
}
