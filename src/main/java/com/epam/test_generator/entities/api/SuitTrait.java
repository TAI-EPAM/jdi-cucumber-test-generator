package com.epam.test_generator.entities.api;

import com.epam.test_generator.entities.Case;
import com.epam.test_generator.services.exceptions.BadRequestException;
import java.util.List;
import java.util.Objects;

public interface SuitTrait {


    List<Case> getCases();


    default boolean hasCase(Case aCase) {
         return getCases().contains(aCase);
    }

    default boolean removeCase(Case aCase){
        return getCases().remove(aCase);
    }
}
