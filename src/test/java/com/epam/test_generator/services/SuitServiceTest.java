package com.epam.test_generator.services;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyListOf;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.test_generator.controllers.version.caze.response.PropertyDifferenceDTO;
import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.ProjectDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dao.interfaces.SuitVersionDAO;
import com.epam.test_generator.controllers.suit.request.SuitCreateDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.dto.SuitRowNumberUpdateDTO;
import com.epam.test_generator.controllers.suit.request.SuitUpdateDTO;
import com.epam.test_generator.dto.SuitVersionDTO;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.pojo.PropertyDifference;
import com.epam.test_generator.pojo.SuitVersion;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.epam.test_generator.transformers.SuitTransformer;
import com.epam.test_generator.transformers.SuitVersionTransformer;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RunWith(MockitoJUnitRunner.class)
public class SuitServiceTest {

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
    private ProjectDAO projectDAO;

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
        expectedSuitList = new ArrayList<>();
        expectedSuit = new Suit(SIMPLE_SUIT_ID, "suit1", "desc1");
        expectedSuitList.add(expectedSuit);

        expectedSuitDTOList = new ArrayList<>();
        expectedSuitDTO = new SuitDTO(SIMPLE_SUIT_ID, "suit1", "desc1");
        expectedSuitDTOList.add(expectedSuitDTO);

        suitCreateDTO = new SuitCreateDTO("suit1", "desc1");

        suitUpdateDTO = new SuitUpdateDTO();
        suitUpdateDTO.setName("new suit 1");
        suitUpdateDTO.setDescription("new desc 1");

        expectedProject = new Project();
        List<Suit> suits = new ArrayList<>();
        suits.add(expectedSuit);
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
        when(suitTransformer.toDtoList(anyListOf(Suit.class))).thenReturn(expectedSuitDTOList);

        List<SuitDTO> actual = suitService.getSuitsDTO();
        assertEquals(expectedSuitDTOList, actual);
    }

    @Test
    public void getSuit_ValidId_Success() {
        when(projectService.getProjectByProjectId(SIMPLE_PROJECT_ID)).thenReturn(expectedProject);
        when(suitDAO.findOne(anyLong())).thenReturn(expectedSuit);

        Suit actual = suitService.getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);

        assertEquals(expectedSuit, actual);
        assertNotNull(actual);
    }

    @Test
    public void get_SuitDTO_Success() {
        when(projectService.getProjectByProjectId(SIMPLE_PROJECT_ID)).thenReturn(expectedProject);
        when(suitDAO.findOne(anyLong())).thenReturn(expectedSuit);
        when(suitTransformer.toDto(expectedSuit)).thenReturn(expectedSuitDTO);

        assertEquals(
            suitService.getSuitDTO(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID),
            expectedSuitDTO);
    }

    @Test(expected = NotFoundException.class)
    public void get_Suit_NotFoundException() {
        when(suitDAO.findOne(anyLong())).thenReturn(null);
        suitService.getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);
    }

    @Test
    public void add_Suit_Success() {
        when(projectService.getProjectByProjectId(anyLong())).thenReturn(expectedProject);
        when(suitDAO.save(any(Suit.class))).thenReturn(expectedSuit);
        when(suitTransformer.toDto(expectedSuit)).thenReturn(expectedSuitDTO);
        SuitDTO actualAddedSuitDTO = suitService.addSuit(1L, suitCreateDTO);
        assertEquals(expectedSuitDTO, actualAddedSuitDTO);
        verify(suitVersionDAO).save(eq(expectedSuit));
        verify(caseVersionDAO).save(eq(expectedSuit.getCases()));
        verify(suitTransformer).toDto(eq(expectedSuit));
    }

    @Test
    public void update_Suit_Success() throws MethodArgumentNotValidException {
        when(projectService.getProjectByProjectId(SIMPLE_PROJECT_ID)).thenReturn(expectedProject);
        when(suitDAO.findOne(anyLong())).thenReturn(expectedSuit);
        when(suitDAO.save(expectedSuit)).thenReturn(expectedSuit);
        when(suitTransformer.toDto(expectedSuit)).thenReturn(expectedSuitDTO);

        suitService.updateSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, suitUpdateDTO);

        verify(projectService).getProjectByProjectId(eq(SIMPLE_PROJECT_ID));
        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(suitDAO).save(eq(expectedSuit));
        verify(suitVersionDAO).save(eq(expectedSuit));
        verify(suitTransformer).toDto(eq(expectedSuit));
    }

    @Test(expected = NotFoundException.class)
    public void update_Suit_NotFoundException() throws MethodArgumentNotValidException {
        when(suitDAO.findOne(anyLong())).thenReturn(null);
        suitService.updateSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, new SuitUpdateDTO());
    }

    @Test(expected = NotFoundException.class)
    public void updateSuitInProject_InvalidProjectId_NotFound()
        throws MethodArgumentNotValidException {
        when(projectService.getProjectByProjectId(INVALID_PROJECT_ID)).thenThrow
            (NotFoundException.class);
        when(suitTransformer.fromDto(any())).thenReturn(expectedSuit);
        when(suitDAO.findOne(anyLong())).thenReturn(expectedSuit);
        suitService.updateSuit(INVALID_PROJECT_ID, SIMPLE_SUIT_ID, suitUpdateDTO);
    }

    @Test
    public void remove_Suit_Success() {
        when(projectService.getProjectByProjectId(anyLong())).thenReturn(expectedProject);
        when(suitDAO.findOne(anyLong())).thenReturn(expectedSuit);
        when(suitTransformer.toDto(expectedSuit)).thenReturn(expectedSuitDTO);

        SuitDTO actualRemovedSuitDTO = suitService.removeSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);

        assertEquals(expectedSuitDTO, actualRemovedSuitDTO);

        verify(suitDAO).delete(anyLong());
        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(suitVersionDAO).delete(eq(expectedSuit));
        verify(caseVersionDAO).delete(eq(expectedSuit.getCases()));
        verify(suitTransformer).toDto(eq(expectedSuit));
    }

    @Test(expected = NotFoundException.class)
    public void remove_Suit_NotFoundException() {
        when(suitDAO.findOne(anyLong())).thenReturn(null);

        suitService.removeSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);
    }

    @Test(expected = NotFoundException.class)
    public void removeSuitFromProject_InvalidProjectId_NotFound() {
        when(projectService.getProjectByProjectId(INVALID_PROJECT_ID)).thenThrow
            (NotFoundException.class);
        when(suitDAO.findOne(anyLong())).thenReturn(expectedSuit);
        when(suitTransformer.toDto(any())).thenReturn(expectedSuitDTO);
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

        verify(suitDAO).save(eq(suits));
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
        when(suitDAO.findOne(anyLong())).thenReturn(expectedSuit);
        when(suitVersionDAO.findAll(anyLong())).thenReturn(suitVersions);
        when(suitVersionTransformer.toDtoList(anyList())).thenReturn(expectedSuitVersions);

        List<SuitVersionDTO> suitVersionDTOs = suitService
            .getSuitVersions(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);

        assertEquals(expectedSuitVersions, suitVersionDTOs);

        verify(projectService).getProjectByProjectId(eq(SIMPLE_PROJECT_ID));
        verify(suitDAO).findOne(SIMPLE_SUIT_ID);
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
        when(suitDAO.findOne(anyLong())).thenReturn(null);

        suitService.getSuitVersions(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);
    }

    @Test(expected = BadRequestException.class)
    public void getSuitVersions_SuitDoesNotBelongToProject_BadRequestException() {
        when(projectService.getProjectByProjectId(anyLong())).thenReturn(expectedProject);
        when(suitDAO.findOne(anyLong())).thenReturn(new Suit());

        suitService.getSuitVersions(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);
    }

    @Test
    public void restoreSuit_SimpleSuit_Restored() {
        when(projectService.getProjectByProjectId(anyLong())).thenReturn(expectedProject);
        when(suitDAO.findOne(anyLong())).thenReturn(expectedSuit);
        when(suitVersionDAO.findByCommitId(anyLong(), anyString())).thenReturn(expectedSuit);
        when(suitDAO.save(expectedSuit)).thenReturn(expectedSuit);
        when(suitTransformer.toDto(expectedSuit)).thenReturn(expectedSuitDTO);

        SuitDTO actualRestoreSuitDTO = suitService
            .restoreSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_COMMIT_ID);
        assertEquals(expectedSuitDTO, actualRestoreSuitDTO);

        verify(projectService).getProjectByProjectId(eq(SIMPLE_PROJECT_ID));
        verify(suitDAO).findOne(SIMPLE_SUIT_ID);
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
        when(suitDAO.findOne(anyLong())).thenReturn(null);

        suitService.restoreSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_COMMIT_ID);
    }

    @Test(expected = BadRequestException.class)
    public void restoreSuit_SuitDoesNotBelongToProject_BadRequestException() {
        when(projectService.getProjectByProjectId(anyLong())).thenReturn(expectedProject);
        when(suitDAO.findOne(anyLong())).thenReturn(new Suit());

        suitService.restoreSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_COMMIT_ID);
    }

    @Test(expected = NotFoundException.class)
    public void restoreSuit_NullSuitToRestore_NotFoundException() {
        when(projectService.getProjectByProjectId(anyLong())).thenReturn(expectedProject);
        when(suitDAO.findOne(anyLong())).thenReturn(expectedSuit);
        when(suitVersionDAO.findByCommitId(anyLong(), anyString())).thenReturn(null);

        suitService.restoreSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, SIMPLE_COMMIT_ID);
    }

}