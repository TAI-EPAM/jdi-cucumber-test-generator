package com.epam.test_generator.dto;

import java.util.List;
import java.util.Objects;
import javax.validation.constraints.NotNull;

public class RawSuitResultDTO {

    @NotNull
    private Long id;

    @NotNull
    private List<RawCaseResultDTO> caseResultDTOList;

    public RawSuitResultDTO() {
    }

    public RawSuitResultDTO(long id, List<RawCaseResultDTO> caseResultDTOList) {
        this.id = id;
        this.caseResultDTOList = caseResultDTOList;
    }

    public long getId() {
        return id;
    }

    public List<RawCaseResultDTO> getCaseResultDTOList() {
        return caseResultDTOList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RawSuitResultDTO that = (RawSuitResultDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(caseResultDTOList, that.caseResultDTOList);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, caseResultDTOList);
    }
}

