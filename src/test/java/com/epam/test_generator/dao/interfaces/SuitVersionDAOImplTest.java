package com.epam.test_generator.dao.interfaces;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.epam.test_generator.dao.JaversChangedDataExtractor;
import com.epam.test_generator.dao.impl.SuitVersionDAOImpl;
import com.epam.test_generator.pojo.SuitVersion;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import org.javers.core.Javers;
import org.javers.core.commit.CommitId;
import org.javers.core.commit.CommitMetadata;
import org.javers.core.diff.Change;
import org.javers.core.diff.changetype.ValueChange;
import org.javers.core.metamodel.object.GlobalId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SuitVersionDAOImplTest {

    private static final long SIMPLE_SUIT_ID = 1L;

    @Mock
    private Javers javers;

    @Mock
    private GlobalId globalId;


    @Mock
    private JaversChangedDataExtractor javersChangedDataExtractor;

    @InjectMocks
    private SuitVersionDAOImpl suitVersionDAO;

    private List<Change> changes;

    private List<String> expectedSuitVersionIds;

    private TreeMap<CommitMetadata, List<Change>> treeOfChanges;

    @Before
    public void setUp() {
        LocalDateTime now = LocalDateTime.now();
        CommitMetadata commitId1_5 = new CommitMetadata("auth", Maps.newHashMap(), now,
            CommitId.valueOf("1.6"));
        CommitMetadata commitId1_3 = new CommitMetadata("auth", Maps.newHashMap(), now,
            CommitId.valueOf("1.2"));
        CommitMetadata commitId6_2 = new CommitMetadata("auth", Maps.newHashMap(), now,
            CommitId.valueOf("6.1"));
        CommitMetadata commitId0_1 = new CommitMetadata("auth", Maps.newHashMap(), now,
            CommitId.valueOf("0.2"));

        changes = Lists.newArrayList(
            new ValueChange(globalId, "prop1", null, null, Optional.of(commitId1_5)),
            new ValueChange(globalId, "prop2", null, null, Optional.of(commitId1_3)),
            new ValueChange(globalId, "prop3", null, null, Optional.of(commitId6_2)),
            new ValueChange(globalId, "prop4", null, null, Optional.of(commitId0_1))
        );

        treeOfChanges = new TreeMap<>(Comparator.comparing(CommitMetadata::getId));
        treeOfChanges.putAll(changes.stream()
            .collect(Collectors.groupingBy(change -> change.getCommitMetadata().get())));

        expectedSuitVersionIds = Lists.newArrayList("0.2", "1.2", "1.6", "6.1");
    }

    @Test
    public void find_SimpleChangeList_ReturnSortedSuitVersionsByCommitId() {
        when(javers.findChanges(any())).thenReturn(changes);
        when(javersChangedDataExtractor.groupByCommitId(changes)).thenReturn(treeOfChanges);

        List<SuitVersion> suitVersions = suitVersionDAO.findAll(SIMPLE_SUIT_ID);

        assertEquals(expectedSuitVersionIds,
            suitVersions.stream()
                .map(SuitVersion::getCommitId)
                .collect(Collectors.toList()));
    }
}
