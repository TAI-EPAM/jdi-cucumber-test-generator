package com.epam.test_generator.dto;

import com.epam.test_generator.entities.Status;
import java.util.List;

/**
 *  This class is needed as the return value in updateCase method.
 *  It contains two field. The first is {@link CaseDTO} and the second is
 *  {@link List<Long>} (in fact id of {@link StepDTO} with FAILED {@link Status} which belong this
 *  suit or case)
 */
public class CaseUpdateDTO {
    private CaseDTO updatedCaseDto;
    private List<Long> failedStepIds;

    public CaseUpdateDTO(CaseDTO updatedCaseDto, List<Long> failedStepIds) {
        this.updatedCaseDto = updatedCaseDto;
        this.failedStepIds = failedStepIds;
    }

    public CaseDTO getUpdatedCaseDto() {
        return updatedCaseDto;
    }

    public void setUpdatedCaseDto(CaseDTO updatedCaseDto) {
        this.updatedCaseDto = updatedCaseDto;
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

        CaseUpdateDTO that = (CaseUpdateDTO) o;

        if (getUpdatedCaseDto() != null ? !getUpdatedCaseDto().equals(that.getUpdatedCaseDto())
            : that.getUpdatedCaseDto() != null) {
            return false;
        }
        return getFailedStepIds() != null ? getFailedStepIds().equals(that.getFailedStepIds())
            : that.getFailedStepIds() == null;
    }

    @Override
    public int hashCode() {
        int result = getUpdatedCaseDto() != null ? getUpdatedCaseDto().hashCode() : 0;
        result = 31 * result + (getFailedStepIds() != null ? getFailedStepIds().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CaseUpdateDTO{" +
            "updatedCaseDto=" + updatedCaseDto +
            ", failedStepIds=" + failedStepIds +
            '}';
    }
}
