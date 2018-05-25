package com.epam.test_generator.pojo;

import com.epam.test_generator.entities.Case;
import java.util.Objects;

/**
 * Stores information about {@link Case} properties values: previous and current. Basically is used
 * by {@link CaseVersion} objects which has a list of {@link PropertyDifference} for every property
 * of {@link Case} that they represents.
 */

public class PropertyDifference {

    private String propertyName;
    private Object oldValue;
    private Object newValue;

    public PropertyDifference(String propertyName, Object oldValue, Object newValue) {
        this.propertyName = propertyName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public PropertyDifference() {

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
        this.oldValue = oldValue;
    }

    public Object getNewValue() {
        return newValue;
    }

    public void setNewValue(Object newValue) {
        this.newValue = newValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PropertyDifference that = (PropertyDifference) o;
        return Objects.equals(propertyName, that.propertyName) &&
            Objects.equals(oldValue, that.oldValue) &&
            Objects.equals(newValue, that.newValue);
    }

    @Override
    public int hashCode() {

        return Objects.hash(propertyName, oldValue, newValue);
    }
}
