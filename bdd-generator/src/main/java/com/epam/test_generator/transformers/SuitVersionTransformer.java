package com.epam.test_generator.transformers;

import com.epam.test_generator.controllers.version.caze.PropertyDifferenceTransformer;
import com.epam.test_generator.dto.SuitVersionDTO;
import com.epam.test_generator.pojo.SuitVersion;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SuitVersionTransformer{

    @Autowired
    PropertyDifferenceTransformer propertyDifferenceTransformer;

    public SuitVersionDTO toDto(SuitVersion suitVersion) {
        return new SuitVersionDTO(suitVersion.getCommitId(),
            //TODO: correct date convertation
            suitVersion.getUpdatedDate().toString(),
            suitVersion.getAuthor(),
            propertyDifferenceTransformer.toListDto(suitVersion.getPropertyDifferences()));
    }

    public List<SuitVersionDTO> toDtoList(List<SuitVersion> suitVersions) {
        return suitVersions.stream().map(this::toDto).collect(Collectors.toList());
    }
}


