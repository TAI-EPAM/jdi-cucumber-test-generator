package com.epam.test_generator.entities.api;

import com.epam.test_generator.entities.Suit;
import java.util.List;

public interface SuitProjectTrait {

    List<Suit> getSuits();

    String getName();

    default void addSuit(Suit suit) {
        getSuits().add(suit);
    }

    default void addSuits(List<Suit> suits) {
        getSuits().addAll(suits);
    }

    default boolean hasSuit(Suit suit) {
        return getSuits().contains(suit);
    }

    default boolean removeSuit(Suit suit) {
        return getSuits().remove(suit);
    }

}
