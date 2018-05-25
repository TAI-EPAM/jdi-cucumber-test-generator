package com.epam.test_generator.services;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.suit.SuitTransformer;
import com.epam.test_generator.controllers.suit.request.SuitCreateDTO;
import com.epam.test_generator.controllers.suit.request.SuitRowNumberUpdateDTO;
import com.epam.test_generator.controllers.suit.request.SuitUpdateDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.controllers.version.caze.response.PropertyDifferenceDTO;
import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dao.interfaces.SuitVersionDAO;
import com.epam.test_generator.dto.SuitVersionDTO;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.pojo.PropertyDifference;
import com.epam.test_generator.pojo.SuitVersion;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.epam.test_generator.transformers.SuitVersionTransformer;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SuitServiceTest {

    private static final String SUIT_NAME = "suit1";
    private static final String SUIT_DESC = "desc1";
    private static long SIMPLE_PROJECT_ID = 0L;
    private static long SIMPLE_SUIT_ID = 1L;
    private static String SIMPLE_COMMIT_ID = "1.5";
    private static long INVALID_PROJECT_ID = -1L;

    @InjectMocks
    private SuitService suitService;

    @Mock
    private SuitVersionDAO suitVersionDAO;

    @Mock
    private CaseVersionDAO caseVersionDAO;

    @Mock
    private ProjectService projectService;

    @Mock
    private SuitVersionTransformer suitVersionTransformer;

    @Mock
    private SuitDAO suitDAO;

    @Mock
    private SuitTransformer suitTransformer;

    private List<Suit> expectedSuitList;
    private List<SuitDTO> expectedSuitDTOList;

    private Project expectedProject;
    private Suit expectedSuit;
    private SuitDTO expectedSuitDTO;
    private SuitCreateDTO suitCreateDTO;
    private SuitUpdateDTO suitUpdateDTO;

    private List<SuitVersion> suitVersions;
    private List<SuitVersionDTO> expectedSuitVersions;

    @Before
    public void setUp() {
        expectedSuit = new Suit(SIMPLE_SUIT_ID, SUIT_NAME, SUIT_DESC);
        expectedSuit.setRowNumber(1);
        expectedSuitList = Lists.newArrayList(expectedSuit);

        expectedSuitDTO = new SuitDTO(SIMPLE_SUIT_ID, SUIT_NAME, SUIT_DESC);
        expectedSuitDTOList = Lists.newArrayList(expectedSuitDTO);

        suitCreateDTO = new SuitCreateDTO(SUIT_NAME, SUIT_DESC);

        suitUpdateDTO = new SuitUpdateDTO();
        suitUpdateDTO.setName("new suit 1");
        suitUpdateDTO.setDescription("new desc 1");

        expectedProject = new Project();
        List<Suit> suits = Lists.newArrayList(expectedSuit);
        expectedProject.setSuits(suits);

        suitVersions = new ArrayList<>();
        expectedSuitVersions = new ArrayList<>();

        CaseDTO caseDTO = new CaseDTO();
        PropertyDifference propertyDifference1 = new PropertyDifference("1", null, "3");
        PropertyDifference propertyDifference2 = new PropertyDifference("2", "1", "2");
        PropertyDifference propertyDifference3 = new PropertyDifference("3", null, caseDTO);
        PropertyDifference propertyDifference4 = new PropertyDifference("4", caseDTO, caseDTO);

        PropertyDifferenceDTO propertyDifferenceDTO1 =
            new PropertyDifferenceDTO("1", null, "3");
        PropertyDifferenceDTO propertyDifferenceDTO2 =
            new PropertyDifferenceDTO("2", "1", "2");
        PropertyDifferenceDTO propertyDifferenceDTO3 =
            new PropertyDifferenceDTO("1", null, caseDTO);
        PropertyDifferenceDTO propertyDifferenceDTO4 =
            new PropertyDifferenceDTO("2", caseDTO, caseDTO);

        suitVersions.add(new SuitVersion("1.3", new Date(), "author",
            Lists.newArrayList(propertyDifference1, propertyDifference2)));
        suitVersions.add(new SuitVersion("2.4", new Date(), "autho2",
            Lists.newArrayList()));
        suitVersions.add(new SuitVersion("3.5", new Date(), "author3",
            Lists.newArrayList(propertyDifference3, propertyDifference4)));
        suitVersions.add(new SuitVersion("4.6", new Date(), "autho4",
            Lists.newArrayList()));

        expectedSuitVersions.add(new SuitVersionDTO("1.3", "", "author",
            Lists.newArrayList(propertyDifferenceDTO1, propertyDifferenceDTO2)));
        expectedSuitVersions.add(new SuitVersionDTO("2.4", "", "autho2",
            Lists.newArrayList()));
        expectedSuitVersions.add(new SuitVersionDTO("3.5", "", "author3",
            Lists.newArrayList(propertyDifferenceDTO3, propertyDifferenceDTO4)));
        expectedSuitVersions.add(new SuitVersionDTO("4.6", "", "autho4",
            Lists.newArrayList()));
    }

    @Test
    public void get_Suits_Success() {
        when(suitDAO.findAll()).thenReturn(expectedSuitList);
        when(suitTransformer.toDtoList(anyList())).thenReturn(expectedSuitDTOList);

        List<SuitDTO> actual = suitService.getSuitsDTO();
        assertEquals(expectedSuitDTOList, actual);
    }

    @Test
    public void getSuit_ValidId_Success() {
        when(projectService.getProjectByProjectId(SIMPLE_PROJECT_ID)).thenReturn(expectedProject);
        when(suitDAO.findById(anyLong())).thenReturn(Optional.of(expectedSuit));

        Suit actual = suitService.getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);

        assertEquals(expectedSuit, actual);
        assertNotNull(actual);
    }

    @Test
    public void get_SuitDTO_Success() {
        when(projectService.getProjectByProjectId(SIMPLE_PROJECT_ID)).thenReturn(expectedProject);
        when(suitDAO.findById(anyLong())).thenReturn(Optional.of(expectedSuit));
        when(suitTransformer.toDto(expectedSuit)).thenReturn(expectedSuitDTO);

        assertEquals(
            suitService.getSuitDTO(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID),
            expectedSuitDTO);
    }

    @Test(expected = NotFoundException.class)
    public void get_Suit_NotFoundException() {
        when(suitDAO.findById(anyLong())).thenReturn(Optional.empty());
        suitService.getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);
    }

    @Test
    public void add_Suit_Success() {
        when(projectService.getProjectByProjectId(anyLong())).thenReturn(expectedProject);
        when(suitDAO.save(any(Suit.class))).thenReturn(expectedSuit);
        when(suitTransformer.fromDto(suitCreateDTO)).thenReturn(expectedSuit);
        when(suitTransformer.toDto(expectedSuit)).thenReturn(expectedSuitDTO);
        SuitDTO actualAddedSuitDTO = suitService.addSuit(1L, suitCreateDTO);
        assertEquals(expectedSuitDTO, actualAddedSuitDTO);
        verify(suitVersionDAO).save(eq(expectedSuit));
        verify(caseVersionDAO).save(eq(expectedSuit.getCases()));
        verify(suitTransformer).toDto(eq(expectedSuit));
    }

    @Test
    public void update_Suit_Success() {
        when(projectService.getProjectByProjectId(SIMPLE_PROJECT_ID)).thenReturn(expectedProject);
        when(suitDAO.findById(anyLong())).thenReturn(Optional.of(expectedSuit));
        when(suitDAO.save(expectedSuit)).thenReturn(expectedSuit);
        when(suitTransformer.toDto(expectedSuit)).thenReturn(expectedSuitDTO);

        suitService.updateSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, suitUpdateDTO);

        verify(projectService).getProjectByProjectId(eq(SIMPLE_PROJECT_ID));
        verify(suitDAO).findById(eq(SIMPLE_SUIT_ID));
        verify(suitDAO).save(eq(expectedSuit));
        verify(suitVersionDAO).save(eq(expectedSuit));
        verify(suitTransformer).toDto(eq(expectedSuit));
    }

    @Test(expected = NotFoundException.class)
    public void update_Suit_NotFoundException() {
        when(suitDAO.findById(anyLong())).thenReturn(Optional.empty());
        suitService.updateSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, new SuitUpdateDTO());
    }

    @Test(expected = NotFoundException.class)
    public void updateSuitInProject_InvalidProjectId_NotFound() {
        when(projectService.getProjectByProjectId(INVALID_PROJECT_ID)).thenThrow
            (NotFoundException.class);
        suitService.updateSuit(INVALID_PROJECT_ID, SIMPLE_SUIT_ID, suitUpdateDTO);
    }

    @Test
    public void remove_Suit_Success() {
        when(projectService.getProjectByProjectId(anyLong())).thenReturn(expectedProject);
        when(suitDAO.findById(anyLong())).thenReturn(Optional.of(expectedSuit));
        when(suitTransformer.toDto(expectedSuit)).thenReturn(expectedSuitDTO);

        SuitDTO actualRemovedSuitDTO = suitService.removeSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);

        assertEquals(expectedSuitDTO, actualRemovedSuitDTO);

        verify(suitDAO).delete(any());
        verify(suitDAO).findById(eq(SIMPLE_SUIT_ID));
        verify(suitVersionDAO).delete(eq(expectedSuit));
        verify(caseVersionDAO).delete(eq(expectedSuit.getCases()));
        verify(suitTransformer).toDto(eq(expectedSuit));
    }

    @Test(expected = NotFoundException.class)
    public void remove_Suit_NotFoundException() {
        when(suitDAO.findById(anyLong())).thenReturn(Optional.empty());

        suitService.removeSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);
    }

    @Test(expected = NotFoundException.class)
    public void removeSuitFromProject_InvalidProjectId_NotFound() {
        when(projectService.getProjectByProjectId(INVALID_PROJECT_ID))
            .thenThrow(NotFoundException.class);
        suitService.removeSuit(INVALID_PROJECT_ID, SIMPLE_SUIT_ID);
    }

    @Test
    public void getSuitsFromProject_ValidId_Success() {
        when(projectService.getProjectByProjectId(SIMPLE_PROJECT_ID)).thenReturn(expectedProject);
        List<SuitDTO> suits = suitService.getSuitsFromProject(SIMPLE_PROJECT_ID);
        assertNotNull(suits);
    }

    @Test
    public void updateSuitRowNumber_SimpleSuit_StatusOk() {
        SuitRowNumberUpdateDTO update = new SuitRowNumberUpdateDTO(5L, 2);
        SuitRowNumberUpdateDTO update1 = new SuitRowNumberUpdateDTO(4L, 1);

        List<SuitRowNumberUpdateDTO> rowNumbers = new ArrayList<>(
            Arrays.asList(update, update1)
        );

        List<Suit> suits = Lists.newArrayList(new Suit(
                4L,
                "name",
                "description",
                new ArrayList<>(),
                3,
                new HashSet<>(),
                2
            ),
            new Suit(
                5L,
                "name2",
                "description2",
                new ArrayList<>(),
                2,
                new HashSet<>(),
                1
            ));

        when(suitDAO.findByIdInOrderById(Sets.newHashSet(5L, 4L))).thenReturn(suits);

        List<SuitRowNumberUpdateDTO> actualUpdatedSuitDTOs = suitService
            .updateSuitRowNumber(rowNumbers);

        assertThat(1, is(equalTo(suits.get(0).getRowNumber())));
        assertThat(2, is(equalTo(suits.get(1).getRowNumber())));
        assertEquals(rowNumbers, actualUpdatedSuitDTOs);

        verify(suitDAO).saveAll(eq(suits));
        verify(suitVersionDAO).save(eq(suits));
    }


    @Test(expected = BadRequestException.class)
    public void updateSuitRowNumber_throwsException_ThereIsNoSuchSuitWithSuchId() {

        List<SuitRowNumberUpdateDTO> rowNumbers = Lists.newArrayList(
            new SuitRowNumberUpdateDTO(5L, 2),
            new SuitRowNumberUpdateDTO(4L, 1)
        );

        when(suitDAO.findByIdInOrderById(Sets.newHashSet(5L, 4L))).thenReturn(new ArrayList<>());
        suitService.updateSuitRowNumber(rowNumbers);
    }

    @Test(expected = BadRequestException.class)
    public void updateSuitRowNumber_throwsException_IfIdOrRowNumberIsNull() {
        List<SuitRowNumberUpdateDTO> rowNumbers = Lists.newArrayList(
            new SuitRowNumberUpdateDTO(null, null),
            new SuitRowNumberUpdateDTO(null, null),
            new SuitRowNumberUpdateDTO(null, null)
        );

        suitService.updateSuitRowNumber(rowNumbers);
    }

    @Test(expected = BadRequestException.class)
    public void updateSuitRowNumber_throwsException_DuplicateRowNumber() {
        List<SuitRowNumberUpdateDTO> rowNumbers = Lists.newArrayList(
            new SuitRowNumberUpdateDTO(5L, 2),
            new SuitRowNumberUpdateDTO(4L, 1)
        );

        List<Suit> retrievedListOfSuits = Collections.singletonList(new Suit(5L,
            "name",
            "description",
            new ArrayList<>(),
            3,
            new HashSet<>(),
            2));

        when(suitDAO.findByIdInOrderById(Sets.newHashSet(5L, 4L))).thenReturn(retrievedListOfSuits);
        suitService.updateSuitRowNumber(rowNumbers);
    }

    @Test(expected = NotFoundException.class)
    public void getSuitsFromProject_inValidId_NotFound() {
        when(projectService.getProjectByProjectId(INVALID_PROJECT_ID)).thenThrow
            (NotFoundException.class);
        suitService.getSuitsFromProject(INVALID_PROJECT_ID);
    }

    @Test
    public void getSuitVersions_SimpleSuit_ReturnExpectedSuitVersionDTOs() {
        when(projectService.getProjectByProjectId(anyLong())).thenReturn(expectedProject);
        when(suitDAO.findById(anyLong())).thenReturn(Optional.of(expectedSuit));
        when(suitVersionDAO.findAll(anyLong())).thenReturn(suitVersions);
        when(suitVersionTransformer.toDtoList(anyList())).thenReturn(expectedSuitVersions);

        List<SuitVersionDTO> suitVersionDTOs = suitService
            .getSuitVersions(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);

        assertEquals(expectedSuitVersions, suitVersionDTOs);

        verify(projectService).getProjectByProjectId(eq(SIMPLE_PROJECT_ID));
        verify(suitDAO).findById(SIMPLE_SUIT_ID);
        verify(suitVersionDAO).findAll(SIMPLE_SUIT_ID);
        verify(suitVersionTransformer).toDtoList(eq(suitVersions));
    }

    @Test(expected = NotFoundException.class)
    public void getSuitVersions_NullProject_NotFoundException() {
        doThrow(NotFoundException.class).when(projectService).getProjectByProjectId(anyLong());

        suitService.getSuitVersions(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);
    }

    @Test(expected = NotFoundException.class)
    public void getSuitVersions_NullSuit_NotFoundException() {
        when(projectService.getProjectByProjectId(anyLong())).thenReturn(expectedProject);
        when(suitDAO.findById(anyLong())).thenReturn(Optional.empty());

        suitService.getSuitVersions(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);
    }

    @Test(expected = BadRequestException.class)
    public void getSuitVersions_SuitDoesNotBelongToProject_BadRequestException() {
        when(projectService.getProjectByProjectId(anyLong())).thenReturn(expectedProject);
        when(suitDAO.findById(anyLong())).thenReturn(Optional.of(new Suit()));

        suitService.getSuitVersions(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);
    }

    @Test
    public void restoreSuit_SimpleSuit_Restored() {
        when(projectService.getProjectByProjectId(anyLong())).thenReturn(expectedProject);
        when(suitDAO.findById(anyLong())).thenReturn(Optional.of(expectedSuit));
        when(suitVersionDAO.findByCommitId(anyLong(), anyString())).thenReturn(expectedSuit);
        when(suitDAO.save(expectedSuit)).thenReturn(expectedSuit);
        when(suitTransformer.toDto(expectedSuit)).thenReturn(expectedSuitDTO);

        SuitDTO actualRestoreSuitDTO = suitService
            .restoreSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_COMMIT_ID);
        assertEquals(expectedSuitDTO, actualRestoreSuitDTO);

        verify(projectService).getProjectByProjectId(eq(SIMPLE_PROJECT_ID));
        verify(suitDAO).findById(SIMPLE_SUIT_ID);
        verify(suitVersionDAO).findByCommitId(SIMPLE_SUIT_ID, SIMPLE_COMMIT_ID);
        verify(suitDAO).save(eq(expectedSuit));
        verify(suitVersionDAO).save(eq(expectedSuit));
        verify(suitTransformer).toDto(eq(expectedSuit));
    }

    @Test(expected = NotFoundException.class)
    public void restoreSuit_NullProject_NotFoundException() {
        doThrow(NotFoundException.class).when(projectService).getProjectByProjectId(anyLong());

        suitService.restoreSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_COMMIT_ID);
    }

    @Test(expected = NotFoundException.class)
    public void restoreSuitBySuitVersionId_NullSuit_NotFoundException() {
        when(suitDAO.findById(anyLong())).thenReturn(Optional.empty());

        suitService.restoreSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_COMMIT_ID);
    }

    @Test(expected = BadRequestException.class)
    public void restoreSuit_SuitDoesNotBelongToProject_BadRequestException() {
        when(projectService.getProjectByProjectId(anyLong())).thenReturn(expectedProject);
        when(suitDAO.findById(anyLong())).thenReturn(Optional.of(new Suit()));

        suitService.restoreSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_COMMIT_ID);
    }

    @Test(expected = NotFoundException.class)
    public void restoreSuit_NullSuitToRestore_NotFoundException() {
        when(projectService.getProjectByProjectId(anyLong())).thenReturn(expectedProject);
        when(suitDAO.findById(anyLong())).thenReturn(Optional.of(expectedSuit));
        when(suitVersionDAO.findByCommitId(anyLong(), anyString())).thenReturn(null);

        suitService.restoreSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_COMMIT_ID);
    }

}