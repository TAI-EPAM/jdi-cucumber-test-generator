package com.epam.test_generator.dto;

import com.epam.test_generator.entities.Status;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import javax.validation.constraints.NotNull;

public class RawCaseResultDTO {

    @NotNull
    private Long id;

    @NotNull
    private Long duration;

    @NotNull
    private Status status;

    @NotNull
    private List<RawStepResultDTO> steps;


    public RawCaseResultDTO(long id, long duration,
                            Status status,
                            List<RawStepResultDTO> steps) {
        this.id = id;
        this.duration = duration;
        this.status = status;
        this.steps = steps;
    }

    public RawCaseResultDTO() {
    }

    public long getId() {
        return id;
    }

    public List<RawStepResultDTO> getSteps() {
        return steps;
    }

    public long getDuration() {
        return duration;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RawCaseResultDTO)) {
            return false;
        }

        final RawCaseResultDTO rawCaseResultDTO = (RawCaseResultDTO) o;

        return (id != null ? id.equals(rawCaseResultDTO.id) : rawCaseResultDTO.id == null)
            && (duration != null ? duration.equals(rawCaseResultDTO.duration)
            : rawCaseResultDTO.duration == null)
            && (status != null ? status.equals(rawCaseResultDTO.status)
            : rawCaseResultDTO.status == null)
            && (steps != null ? steps.equals(rawCaseResultDTO.steps)
            : rawCaseResultDTO.steps == null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (steps != null ? steps.hashCode() : 0);
        return result;
    }
}
