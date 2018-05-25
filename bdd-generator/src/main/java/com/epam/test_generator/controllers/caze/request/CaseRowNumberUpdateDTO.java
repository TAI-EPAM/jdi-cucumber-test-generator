package com.epam.test_generator.controllers.caze.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class CaseRowNumberUpdateDTO {
    @NotNull
    private Long id;

    @NotNull
    @Size(min = 1)
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
