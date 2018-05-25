package com.epam.test_generator.dao;

import static java.util.stream.Collectors.groupingBy;

import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.pojo.PropertyDifference;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.javers.core.commit.CommitMetadata;
import org.javers.core.diff.Change;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.diff.changetype.container.CollectionChange;
import org.javers.core.diff.changetype.container.ContainerElementChange;
import org.javers.core.diff.changetype.container.ElementValueChange;
import org.javers.core.diff.changetype.container.ValueAdded;
import org.javers.core.diff.changetype.container.ValueRemoved;
import org.springframework.stereotype.Component;

@Component
public class JaversChangedDataExtractor {
    /**
     * Group and sort changes by commitId.
     *
     * @param changes list of {@link Change} objects that represents property changes.
     * @return grouped and sorted {@link TreeMap}
     */
    public TreeMap<CommitMetadata, List<Change>> groupByCommitId(List<Change> changes) {

        Map<CommitMetadata, List<Change>> changesByCommitId = changes.stream()
            .filter(change -> change.getCommitMetadata().isPresent())
            .collect(groupingBy(change -> change.getCommitMetadata().get()));

        TreeMap<CommitMetadata, List<Change>> sortedChangesByCommitId =
            new TreeMap<>(Comparator.comparing(CommitMetadata::getId));
        sortedChangesByCommitId.putAll(changesByCommitId);

        return sortedChangesByCommitId;
    }

    /**
     * Maps list of {@link Change} to {@link PropertyDifference} list. Basically, there can be two
     * scenarios: element of changeList is a simple property value or collection. <br> For example:
     * {@link Suit} or {@link Case} object has simple fields "description" and "name" and
     * collection-field "cases" or "steps" and "tags".
     *
     * @param changeList list of {@link Change} objects that represents property changes.
     * @return list of {@link PropertyDifference} created from {@link Change} list.
     */
    public List<PropertyDifference> getPropertyDifferences(List<Change> changeList) {
        List<PropertyDifference> propertyDifferences = new ArrayList<>();
        for (Change change : changeList) {
            if (change instanceof ValueChange) {
                propertyDifferences.add(
                    getValuePropertyDifference((ValueChange) change));
            } else if (change instanceof CollectionChange) {
                propertyDifferences.addAll(
                    getCollectionPropertiesDifferences((CollectionChange) change));
            }
        }
        return propertyDifferences;
    }

    /**
     * Returns differences for collection-field. Basically, collection-field for {@link Suit} or
     * {@link Case} objects is list of {@link Case} or {@link Step} objects and set of {@link Tag}.
     * There can be three states of collection-field element: "element changed", "element added" and
     * "element removed".
     *
     * @param change - {@link CollectionChange} for container-field.
     * @return list of {@link PropertyDifference} objects for every element of container-property
     */
    private List<PropertyDifference> getCollectionPropertiesDifferences(CollectionChange change) {

        List<PropertyDifference> propertyDifferences = new LinkedList<>();

        for (ContainerElementChange containerElementChange : change.getChanges()) {
            if (containerElementChange instanceof ElementValueChange) {
                ElementValueChange elementValueChange = (ElementValueChange) containerElementChange;
                propertyDifferences.add(new PropertyDifference(
                    change.getPropertyName(),
                    elementValueChange.getLeftValue(),
                    elementValueChange.getRightValue()
                ));
            } else if (containerElementChange instanceof ValueAdded) {
                propertyDifferences.add(new PropertyDifference(
                    change.getPropertyName(),
                    null,
                    ((ValueAdded) containerElementChange).getAddedValue()
                ));
            } else if (containerElementChange instanceof ValueRemoved) {
                propertyDifferences.add(new PropertyDifference(
                    change.getPropertyName(),
                    ((ValueRemoved) containerElementChange).getRemovedValue(),
                    null
                ));
            }
        }

        return propertyDifferences;
    }


    /**
     * Creates {@link PropertyDifference} object from specified {@link ValueChange} object.
     *
     * @param change - {@link ValueChange} object .
     * @return {@link PropertyDifference} for specified {@link ValueChange} object.
     */
    private PropertyDifference getValuePropertyDifference(ValueChange change) {
        PropertyDifference propertyDifference;
        propertyDifference = new PropertyDifference(
            change.getPropertyName(),
            change.getLeft(),
            change.getRight()
        );
        return propertyDifference;
    }
}
