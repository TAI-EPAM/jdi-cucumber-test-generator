package com.epam.test_generator.controllers.version.caze;

import com.epam.test_generator.controllers.version.caze.response.PropertyDifferenceDTO;
import com.epam.test_generator.pojo.PropertyDifference;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class PropertyDifferenceTransformer {

    public PropertyDifferenceDTO toDto(PropertyDifference prop) {
        return new PropertyDifferenceDTO(
            prop.getPropertyName(),
            prop.getOldValue(),
            prop.getNewValue()
        );
    }

    public List<PropertyDifferenceDTO> toListDto(List<PropertyDifference> propertyDifferenceList) {
        return propertyDifferenceList
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
}
