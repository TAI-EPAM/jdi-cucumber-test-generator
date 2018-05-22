package com.epam.test_generator.controllers.version.caze;

import com.epam.test_generator.controllers.caze.CaseTransformer;
import com.epam.test_generator.controllers.step.StepTransformer;
import com.epam.test_generator.controllers.tag.TagTransformer;
import com.epam.test_generator.controllers.version.caze.response.PropertyDifferenceDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.pojo.PropertyDifference;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PropertyDifferenceTransformer {

    @Autowired
    private StepTransformer stepTransformer;
    @Autowired
    private TagTransformer tagTransformer;
    @Autowired
    private CaseTransformer caseTransformer;

    public PropertyDifferenceDTO toDto(PropertyDifference prop) {
        return new PropertyDifferenceDTO(
            prop.getPropertyName(),
            convert(prop.getOldValue()),
            convert(prop.getNewValue())
        );
    }

    public List<PropertyDifferenceDTO> toListDto(List<PropertyDifference> propertyDifferenceList) {
        return propertyDifferenceList
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    private Object convert(Object sourceFieldValue) {
        if (sourceFieldValue == null) {
            return null;
        }

        if (sourceFieldValue instanceof Step) {
            Step source = (Step) sourceFieldValue;
            return stepTransformer.toDto(source);
        } else if (sourceFieldValue instanceof Tag) {
            Tag source = (Tag) sourceFieldValue;
            return tagTransformer.toDto(source);
        } else if (sourceFieldValue instanceof Case) {
            Case source = (Case) sourceFieldValue;
            return caseTransformer.toDto(source);
        } else if (sourceFieldValue instanceof ZonedDateTime) {
            return String.valueOf(((ZonedDateTime) sourceFieldValue).toInstant().getEpochSecond());
        } else {
            return sourceFieldValue.toString();
        }
    }

}
