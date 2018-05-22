package com.epam.test_generator.controllers.version.caze;

import com.epam.test_generator.controllers.version.caze.response.CaseVersionDTO;
import com.epam.test_generator.pojo.CaseVersion;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CaseVersionTransformer {

    @Autowired
    private PropertyDifferenceTransformer propertyDifferenceTransformer;

    public CaseVersionDTO toDto(CaseVersion caseVersion) {
        return new CaseVersionDTO(
            caseVersion.getCommitId(),
            String.valueOf(caseVersion.getUpdatedDate().toInstant().getEpochSecond()),
            caseVersion.getAuthor(),
            propertyDifferenceTransformer.toListDto(caseVersion.getPropertyDifferences())
        );
    }

    public List<CaseVersionDTO> toListDto(List<CaseVersion> caseVersions) {
        return caseVersions
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
}
