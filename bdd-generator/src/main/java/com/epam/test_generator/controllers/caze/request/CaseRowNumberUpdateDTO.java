package com.epam.test_generator.controllers.caze.request;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CaseRowNumberUpdateDTO {
    @NotNull
    private Long id;

    @NotNull
    @Min(1)
    private Integer rowNumber;

    public CaseRowNumberUpdateDTO(Long id, Integer rowNumber) {
        this.id = id;
        this.rowNumber = rowNumber;
    }

    public CaseRowNumberUpdateDTO() {
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
