package com.epam.test_generator.dao.impl;

import com.epam.test_generator.dao.JaversChangedDataExtractor;
import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.pojo.CaseVersion;
import com.epam.test_generator.pojo.PropertyDifference;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import org.javers.core.Javers;
import org.javers.core.commit.CommitId;
import org.javers.core.commit.CommitMetadata;
import org.javers.core.diff.Change;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.javers.spring.auditable.AuthorProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class CaseVersionDAOImpl implements CaseVersionDAO {

    @Autowired
    @Qualifier(value = "javersConfigForCase")
    private Javers javers;

    @Autowired
    private AuthorProvider authorProvider;

    @Autowired
    private JaversChangedDataExtractor javersChangedDataExtractor;

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
        TreeMap<CommitMetadata, List<Change>> changesWithSameCommitId = javersChangedDataExtractor
            .groupByCommitId(changes);

        List<CaseVersion> caseVersions = new ArrayList<>(changesWithSameCommitId.size());
        changesWithSameCommitId.forEach(
            (commitMetadata, commitChanges) -> {

                String commitId = commitMetadata.getId().toString();
                Date updatedDate = Date.from(commitMetadata.getCommitDate().atZone(
                    ZoneId.systemDefault()).toInstant());
                String author = commitMetadata.getAuthor();
                List<PropertyDifference> propertyDifferences = javersChangedDataExtractor
                    .getPropertyDifferences(commitChanges);

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
}
