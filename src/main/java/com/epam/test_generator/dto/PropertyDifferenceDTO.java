package com.epam.test_generator.dto;

public class PropertyDifferenceDTO {

    private String propertyName;
    private String oldValue;
    private String newValue;

    public PropertyDifferenceDTO() {

    }

    public PropertyDifferenceDTO(String propertyName, String oldValue, String newValue) {
        this.propertyName = propertyName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    public String getOldValue() {
        return oldValue;
    }

    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
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
