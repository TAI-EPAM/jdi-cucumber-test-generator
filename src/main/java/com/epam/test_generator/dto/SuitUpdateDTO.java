package com.epam.test_generator.dto;

import com.epam.test_generator.entities.Status;
import java.util.List;

/**
 *  This class is needed as the return value in updateSuit method.
 *  It contains two field. The first is {@link SuitDTO} and the second is
 *  {@link List<Long>} (in fact id of {@link StepDTO} with FAILED {@link Status} which belong this
 *  suit or case)
 */
public class SuitUpdateDTO {
    private SuitDTO updatedSuitDto;
    private List<Long> failedStepIds;

    public SuitUpdateDTO(SuitDTO updatedSuitDto, List<Long> failedStepIds) {
        this.updatedSuitDto = updatedSuitDto;
        this.failedStepIds = failedStepIds;
    }

    public SuitDTO getUpdatedSuitDto() {
        return updatedSuitDto;
    }

    public void setUpdatedSuitDto(SuitDTO updatedSuitDto) {
        this.updatedSuitDto = updatedSuitDto;
    }

    public List<Long> getFailedStepIds() {
        return failedStepIds;
    }

    public void setFailedStepIds(List<Long> failedStepIds) {
        this.failedStepIds = failedStepIds;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SuitUpdateDTO that = (SuitUpdateDTO) o;

        if (getUpdatedSuitDto() != null ? !getUpdatedSuitDto().equals(that.getUpdatedSuitDto())
            : that.getUpdatedSuitDto() != null) {
            return false;
        }
        return getFailedStepIds() != null ? getFailedStepIds().equals(that.getFailedStepIds())
            : that.getFailedStepIds() == null;
    }

    @Override
    public int hashCode() {
        int result = getUpdatedSuitDto() != null ? getUpdatedSuitDto().hashCode() : 0;
        result = 31 * result + (getFailedStepIds() != null ? getFailedStepIds().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SuitUpdateDTO{" +
            "updatedSuitDto=" + updatedSuitDto +
            ", failedStepIds=" + failedStepIds +
            '}';
    }
}
