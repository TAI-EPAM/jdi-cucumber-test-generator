package com.epam.test_generator.dao.impl;

import static java.util.stream.Collectors.groupingBy;

import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.pojo.CaseVersion;
import com.epam.test_generator.pojo.PropertyDifference;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import org.javers.core.Javers;
import org.javers.core.commit.CommitId;
import org.javers.core.commit.CommitMetadata;
import org.javers.core.diff.Change;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.diff.changetype.container.CollectionChange;
import org.javers.core.diff.changetype.container.ContainerElementChange;
import org.javers.core.diff.changetype.container.ElementValueChange;
import org.javers.core.diff.changetype.container.ValueAdded;
import org.javers.core.diff.changetype.container.ValueRemoved;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.javers.spring.auditable.AuthorProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CaseVersionDAOImpl implements CaseVersionDAO {

    @Autowired
    private Javers javers;

    @Autowired
    private AuthorProvider authorProvider;

    @Override
    public List<CaseVersion> findAll(Long caseId) {

        List<Change> changes = javers.findChanges(
            QueryBuilder.byInstanceId(caseId, Case.class).withNewObjectChanges().build());

        return getCaseVersions(changes);
    }

    @Override
    public Case findByCommitId(Long caseId, String commitId) {

        Optional<Shadow<Object>> caseShadows = javers
            .findShadows(QueryBuilder.byInstanceId(caseId, Case.class)
                .withCommitId(CommitId.valueOf(commitId)).build()).stream().findAny();

        return caseShadows.map(objectShadow -> (Case) objectShadow.get()).orElse(null);
    }

    @Override
    public void save(Case caze) {
        javers.commit(authorProvider.provide(), caze);
    }

    @Override
    public void save(Iterable<Case> cases) {
        if (cases == null) {
            return;
        }

        for (Case caze : cases) {
            save(caze);
        }
    }

    @Override
    public void delete(Case caze) {
        javers.commitShallowDelete(authorProvider.provide(), caze);
    }

    @Override
    public void delete(Iterable<Case> cases) {
        if (cases == null) {
            return;
        }

        for (Case caze : cases) {
            delete(caze);
        }
    }

    /**
     * Group list of {@link Change} by commitIds and map each group to {@link CaseVersion}
     *
     * @param changes list of {@link Change} objects that represents property changes.
     * @return list {@link CaseVersion} that represent changes made by each commit
     */
    private List<CaseVersion> getCaseVersions(List<Change> changes) {
        TreeMap<CommitMetadata, List<Change>> changesWithSameCommitId =
            groupByCommitId(changes);

        List<CaseVersion> caseVersions = new ArrayList<>(changesWithSameCommitId.size());
        changesWithSameCommitId.forEach(
            (commitMetadata, commitChanges) -> {

                String commitId = commitMetadata.getId().toString();
                Date updatedDate = Date.from(commitMetadata.getCommitDate().atZone(
                    ZoneId.systemDefault()).toInstant());
                String author = commitMetadata.getAuthor();
                List<PropertyDifference> propertyDifferences = getPropertyDifferences(
                    commitChanges);

                caseVersions.add(new CaseVersion(
                    commitId,
                    updatedDate,
                    author,
                    propertyDifferences
                ));
            }
        );
        return caseVersions;
    }

    /**
     * Maps list of {@link Change} to {@link PropertyDifference} list. Basically, there can be two
     * scenarios: element of changeList is a simple property value or collection. <br> For example:
     * {@link Case} object has simple fields "description" and "name" and collection-field "steps"
     * and "tags".
     *
     * @param changeList list of {@link Change} objects that represents property changes.
     * @return list of {@link PropertyDifference} created from {@link Change} list.
     */
    private List<PropertyDifference> getPropertyDifferences(List<Change> changeList) {
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
     * Group and sort changes by commitId.
     *
     * @param changes list of {@link Change} objects that represents property changes.
     * @return grouped and sorted {@link TreeMap}
     */
    private TreeMap<CommitMetadata, List<Change>> groupByCommitId(List<Change> changes) {

        Map<CommitMetadata, List<Change>> changesByCommitId = changes.stream()
            .filter(change -> change.getCommitMetadata().isPresent())
            .collect(groupingBy(change -> change.getCommitMetadata().get()));

        TreeMap<CommitMetadata, List<Change>> sortedChangesByCommitId =
            new TreeMap<>(Comparator.comparing(CommitMetadata::getId));
        sortedChangesByCommitId.putAll(changesByCommitId);

        return sortedChangesByCommitId;
    }

    /**
     * Returns differences for collection-field. Basically, collection-field for {@link Case}
     * objects is list of {@link Step} objects and set of {@link Tag}. There can be three states of
     * collection-field element: "element changed", "element added" and "element removed".
     *
     * @param change - {@link CollectionChange} for container-field.
     * @return list of {@link PropertyDifference} objects for every element of container-property
     */
    private List<PropertyDifference> getCollectionPropertiesDifferences(CollectionChange change) {

        List<PropertyDifference> propertyDifferences = new LinkedList<>();

        for (ContainerElementChange containerElementChange : change.getChanges()) {
            if (containerElementChange instanceof ElementValueChange) {
                propertyDifferences.add(new PropertyDifference(
                    change.getPropertyName(),
                    ((ElementValueChange) containerElementChange).getLeftValue(),
                    ((ElementValueChange) containerElementChange).getRightValue()
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
