package com.epam.test_generator.entities.api;

import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
