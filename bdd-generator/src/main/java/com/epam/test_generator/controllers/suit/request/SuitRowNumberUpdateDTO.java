package com.epam.test_generator.controllers.suit.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * SuitRowNumberUpdateDTO is a data transfer object, that represents suitable struct for updating
 * Suit's rowNumbers by Suit's id.
 */
public class SuitRowNumberUpdateDTO {

    @NotNull
    private Long id;

    @NotNull
    @Min(1)
    private Integer rowNumber;

    public SuitRowNumberUpdateDTO(Long id, Integer rowNumber) {
        this.id = id;
        this.rowNumber = rowNumber;
    }

    public SuitRowNumberUpdateDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }
}
