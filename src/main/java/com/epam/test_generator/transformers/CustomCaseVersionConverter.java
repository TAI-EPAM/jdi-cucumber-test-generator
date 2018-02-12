package com.epam.test_generator.transformers;

import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.dto.TagDTO;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.Tag;
import org.dozer.CustomConverter;
import org.springframework.stereotype.Component;

@Component
public class CustomCaseVersionConverter implements CustomConverter {

    /**
     *  The converter uses only for {@link CaseVersionTransformer} Dozer configuration
     *
     *  We had to create custom converter and realize own convert method
     *  because by default the Dozer creating empty objects when try to convert
     *  nested objects to object of {@link com.epam.test_generator.dto.PropertyDifferenceDTO}
     */

    @Override
    public Object convert(Object existingDestinationFieldValue, Object sourceFieldValue,
                          Class<?> destinationClass, Class<?> sourceClass) {
        if (sourceFieldValue == null) {
            return null;
        }

        if (sourceFieldValue instanceof Step) {
            Step source = (Step) sourceFieldValue;
            return new StepDTO(source.getId(), source.getRowNumber(), source.getDescription(),
                source.getType(), "", Status.NOT_RUN);
        } else if (sourceFieldValue instanceof Tag) {
            Tag source = (Tag) sourceFieldValue;
            TagDTO tag = new TagDTO();
            tag.setId(source.getId());
            tag.setName(source.getName());
            return tag;
        } else {
            return sourceFieldValue.toString();
        }
    }
}
