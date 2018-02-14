package com.epam.test_generator.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The PropertyDifferenceDTO uses only in one-way transformation from {@link
 * com.epam.test_generator.pojo.PropertyDifference} to ResponseEntity controller output.
 * This DTO is used for organisation history of cases' versions. It contains property that have been changed
 * (old and new value of the property). It is basically used in {@Link CaseVersionDTO}
 */
public class PropertyDifferenceDTO {

    private static final Logger logger = LoggerFactory.getLogger(PropertyDifferenceDTO.class);

    private String propertyName;
    private Object oldValue;        // By default can be String or StepDTO, TagDTO objects
    private Object newValue;        // By default can be String or StepDTO, TagDTO objects

    public PropertyDifferenceDTO() {

    }

    public PropertyDifferenceDTO(String propertyName, Object oldValue, Object newValue) {
        this.propertyName = propertyName;
        if (oldValue == null || oldValue instanceof String || oldValue instanceof StepDTO
            || oldValue instanceof TagDTO) {
            this.oldValue = oldValue;
        } else {
            logger.warn("Unknown conversion type: " + oldValue.getClass());
            throw new ClassCastException("Unknown conversion type: " + oldValue.getClass());
        }

        if (newValue == null || newValue instanceof String || newValue instanceof StepDTO
            || newValue instanceof TagDTO) {
            this.newValue = newValue;
        } else {
            logger.warn("Unknown conversion type: " + newValue.getClass());
            throw new ClassCastException("Unknown conversion type: " + newValue.getClass());
        }
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public Object getOldValue() {
        return oldValue;
    }

    public void setOldValue(Object oldValue) {
        if (oldValue == null ||oldValue instanceof String || oldValue instanceof StepDTO
            || oldValue instanceof TagDTO) {
            this.oldValue = oldValue;
        } else {
            logger.warn("Unknown conversion type: " + oldValue.getClass());
            throw new ClassCastException("Unknown conversion type: " + oldValue.getClass());
        }
    }

    public Object getNewValue() {
        return newValue;
    }

    public void setNewValue(Object newValue) {
        if (newValue == null || newValue instanceof String || newValue instanceof StepDTO
            || newValue instanceof TagDTO) {
            this.newValue = newValue;
        } else {
            logger.warn("Unknown conversion type: " + newValue.getClass());
            throw new ClassCastException("Unknown conversion type: " + newValue.getClass());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PropertyDifferenceDTO that = (PropertyDifferenceDTO) o;

        return (propertyName != null ? propertyName.equals(that.propertyName)
            : that.propertyName == null)
            && (oldValue != null ? oldValue.equals(that.oldValue) : that.oldValue == null)
            && (newValue != null ? newValue.equals(that.newValue) : that.newValue == null);
    }

    @Override
    public int hashCode() {
        int result = propertyName != null ? propertyName.hashCode() : 0;
        result = 31 * result + (oldValue != null ? oldValue.hashCode() : 0);
        result = 31 * result + (newValue != null ? newValue.hashCode() : 0);
        return result;
    }
}
