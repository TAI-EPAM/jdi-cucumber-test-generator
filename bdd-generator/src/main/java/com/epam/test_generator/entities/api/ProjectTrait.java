package com.epam.test_generator.entities.api;

import com.epam.test_generator.entities.Suit;
import java.util.List;

public interface ProjectTrait {

    void setActive(boolean active);

    boolean isActive();

    Long getId();

    List<Suit> getSuits();

    default void deactivate() {
        setActive(false);
    }

    default void activate(){
        setActive(true);
    }

}
