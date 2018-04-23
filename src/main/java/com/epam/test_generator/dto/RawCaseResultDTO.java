package com.epam.test_generator.dto;

import com.epam.test_generator.entities.Status;
import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
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
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RawCaseResultDTO that = (RawCaseResultDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(duration, that.duration) &&
            status == that.status &&
            Objects.equals(steps, that.steps);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, duration, status, steps);
    }
}
