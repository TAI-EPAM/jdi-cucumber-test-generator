package com.epam.test_generator.dto;

import com.epam.test_generator.entities.Status;

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
        if (!(o instanceof RawStepResultDTO)) {
            return false;
        }

        RawStepResultDTO rawStepResultDTO = (RawStepResultDTO) o;

        return (id != null ? id.equals(rawStepResultDTO.id) : rawStepResultDTO.id == null)
            && (status != null ? status.equals(rawStepResultDTO.status)
            : rawStepResultDTO.status == null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }
}
