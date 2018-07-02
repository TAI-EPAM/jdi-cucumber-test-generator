package com.epam.test_generator.dto;

import com.epam.test_generator.entities.Status;
import java.util.Objects;
import javax.validation.constraints.NotNull;

public class RawStepResultDTO {

    @NotNull
    private Long id;

    @NotNull
    private Status status;

    public RawStepResultDTO(long id, Status status) {
        this.id = id;
        this.status = status;
    }

    public RawStepResultDTO() {
    }

    public Long getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RawStepResultDTO that = (RawStepResultDTO) o;
        return Objects.equals(id, that.id) &&
            status == that.status;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, status);
    }
}
