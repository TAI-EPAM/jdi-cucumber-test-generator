package com.epam.test_generator.dto;

import com.epam.test_generator.entities.Status;
import java.util.Objects;

public class RawStepResultDTO {

    private Long id;

    private Status status;

    public RawStepResultDTO(long id, Status status) {
        this.id = id;
        this.status = status;
    }

    public RawStepResultDTO() {
    }

    public long getId() {
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
