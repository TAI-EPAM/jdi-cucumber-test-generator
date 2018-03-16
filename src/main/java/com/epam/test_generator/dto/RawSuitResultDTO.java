package com.epam.test_generator.dto;

import java.util.List;
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
        if (!(o instanceof RawSuitResultDTO)) {
            return false;
        }

        final RawSuitResultDTO rawSuitResultDTO = (RawSuitResultDTO) o;

        return (id != null ? id.equals(rawSuitResultDTO.id) : rawSuitResultDTO.id == null)
            && (caseResultDTOList != null ? caseResultDTOList
            .equals(rawSuitResultDTO.caseResultDTOList)
            : rawSuitResultDTO.caseResultDTOList == null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (caseResultDTOList != null ? caseResultDTOList.hashCode() : 0);
        return result;
    }
}

