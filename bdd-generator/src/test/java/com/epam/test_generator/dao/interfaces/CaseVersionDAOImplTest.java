package com.epam.test_generator.dao.interfaces;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.epam.test_generator.dao.JaversChangedDataExtractor;
import com.epam.test_generator.dao.impl.CaseVersionDAOImpl;
import com.epam.test_generator.pojo.CaseVersion;
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
public class CaseVersionDAOImplTest {

    private static final long SIMPLE_CASE_ID = 1L;

    @Mock
    private Javers javers;

    @Mock
    private GlobalId globalId;

    @Mock
    private JaversChangedDataExtractor javersChangedDataExtractor;

    @InjectMocks
    private CaseVersionDAOImpl caseVersionDAO;

    private List<Change> changes;

    private List<String> expectedCaseVersionIds;

    private TreeMap<CommitMetadata, List<Change>> treeOfChanges;

    @Before
    public void setUp() {
        LocalDateTime now = LocalDateTime.now();
        CommitMetadata commitId1_5 = new CommitMetadata("author", Maps.newHashMap(), now,
            CommitId.valueOf("1.5"));
        CommitMetadata commitId1_3 = new CommitMetadata("author", Maps.newHashMap(), now,
            CommitId.valueOf("1.3"));
        CommitMetadata commitId6_2 = new CommitMetadata("author", Maps.newHashMap(), now,
            CommitId.valueOf("6.2"));
        CommitMetadata commitId0_1 = new CommitMetadata("author", Maps.newHashMap(), now,
            CommitId.valueOf("0.1"));

        changes = Lists.newArrayList(
            new ValueChange(globalId, "prop1", null, null, Optional.of(commitId1_5)),
            new ValueChange(globalId, "prop2", null, null, Optional.of(commitId1_3)),
            new ValueChange(globalId, "prop3", null, null, Optional.of(commitId6_2)),
            new ValueChange(globalId, "prop4", null, null, Optional.of(commitId0_1))
        );

        treeOfChanges = new TreeMap<>(Comparator.comparing(CommitMetadata::getId));
        treeOfChanges.putAll(changes.stream()
            .collect(Collectors.groupingBy(change -> change.getCommitMetadata().get())));

        expectedCaseVersionIds = Lists.newArrayList("0.1", "1.3", "1.5", "6.2");
    }

    @Test
    public void find_SimpleChangeList_ReturnSortedCaseVersionsByCommitId() {
        when(javers.findChanges(any())).thenReturn(changes);
        when(javersChangedDataExtractor.groupByCommitId(changes)).thenReturn(treeOfChanges);

        List<CaseVersion> caseVersions = caseVersionDAO.findAll(SIMPLE_CASE_ID);

        assertEquals(expectedCaseVersionIds,
            caseVersions.stream()
                .map(CaseVersion::getCommitId)
                .collect(Collectors.toList()));
    }
}
