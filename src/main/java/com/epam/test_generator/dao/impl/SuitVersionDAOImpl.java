package com.epam.test_generator.dao.impl;

import com.epam.test_generator.dao.JaversChangedDataExtractor;
import com.epam.test_generator.dao.interfaces.SuitVersionDAO;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.pojo.PropertyDifference;
import com.epam.test_generator.pojo.SuitVersion;
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
public class SuitVersionDAOImpl implements SuitVersionDAO {

    @Autowired
    @Qualifier(value = "javersConfigForSuit")
    private Javers javers;

    @Autowired
    private AuthorProvider authorProvider;

    @Autowired
    private JaversChangedDataExtractor javersChangedDataExtractor;

    @Override
    public List<SuitVersion> findAll(Long suitId) {
        List<Change> changes = javers.findChanges(
            QueryBuilder.byInstanceId(suitId, Suit.class).withNewObjectChanges().build());

        return getSuitVersions(changes);
    }

    @Override
    public Suit findByCommitId(Long suitId, String commitId) {
        Optional<Shadow<Object>> suitShadows = javers
            .findShadows(QueryBuilder.byInstanceId(suitId, Suit.class)
                .withCommitId(CommitId.valueOf(commitId)).build()).stream().findAny();

        return suitShadows.map(objectShadow -> (Suit) objectShadow.get()).orElse(null);
    }

    @Override
    public void save(Suit suit) {
        javers.commit(authorProvider.provide(), suit);
    }

    @Override
    public void save(Iterable<Suit> suits) {
        if (suits == null) {
            return;
        }

        for (Suit suit : suits) {
            save(suit);
        }
    }

    @Override
    public void delete(Suit suit) {
        javers.commitShallowDelete(authorProvider.provide(), suit);
    }

    @Override
    public void delete(Iterable<Suit> suits) {
        if (suits == null) {
            return;
        }

        for (Suit suit : suits) {
            delete(suit);
        }
    }

    /**
     * Group list of {@link Change} by commitIds and map each group to {@link SuitVersion}
     *
     * @param changes list of {@link Change} objects that represents property changes.
     * @return list {@link SuitVersion} that represent changes made by each commit
     */
    private List<SuitVersion> getSuitVersions(List<Change> changes) {
        TreeMap<CommitMetadata, List<Change>> changesWithSameCommitId = javersChangedDataExtractor
            .groupByCommitId(changes);

        List<SuitVersion> suitVersions = new ArrayList<>(changesWithSameCommitId.size());
        changesWithSameCommitId.forEach(
            (commitMetadata, commitChanges) -> {

                String commitId = commitMetadata.getId().toString();
                Date updatedDate = Date.from(commitMetadata.getCommitDate().atZone(
                    ZoneId.systemDefault()).toInstant());
                String author = commitMetadata.getAuthor();
                List<PropertyDifference> propertyDifferences = javersChangedDataExtractor
                    .getPropertyDifferences(commitChanges);

                suitVersions.add(new SuitVersion(
                    commitId,
                    updatedDate,
                    author,
                    propertyDifferences
                ));
            }
        );
        return suitVersions;
    }
}
