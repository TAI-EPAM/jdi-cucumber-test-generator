package com.epam.test_generator.services;


import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyListOf;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.dto.SuitRowNumberUpdateDTO;
import com.epam.test_generator.dto.SuitUpdateDTO;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.epam.test_generator.transformers.SuitTransformer;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.bind.MethodArgumentNotValidException;

@RunWith(MockitoJUnitRunner.class)
public class SuitServiceTest {

    private static final long SIMPLE_PROJECT_ID = 0L;
    private static final long SIMPLE_SUIT_ID = 1L;
    private static final long INVALID_PROJECT_ID = -1L;
    @InjectMocks
    private SuitService suitService;
    @Mock
    private CaseVersionDAO caseVersionDAO;
    @Mock
    private ProjectService projectService;

    @Mock
    private CascadeUpdateService cascadeUpdateService;

    @Mock
    private SuitDAO suitDAO;
    @Mock
    private SuitTransformer suitTransformer;
    private List<Suit> expectedSuitList;
    private List<SuitDTO> expectedSuitDTOList;

    private Project expectedProject;
    private Suit expectedSuit;
    private SuitDTO expectedSuitDTO;

    @Before
    public void setUp() {
        expectedSuitList = new ArrayList<>();
        expectedSuit = new Suit(SIMPLE_SUIT_ID, "suit1", "desc1");
        expectedSuitList.add(expectedSuit);

        expectedSuitDTOList = new ArrayList<>();
        expectedSuitDTO = new SuitDTO(SIMPLE_SUIT_ID, "suit1", "desc1");
        expectedSuitDTOList.add(expectedSuitDTO);

        expectedProject = new Project();
        List<Suit> suits = new ArrayList<>();
        suits.add(expectedSuit);
        expectedProject.setSuits(suits);
    }

    @Test
    public void get_Suits_Success() {
        when(suitDAO.findAll()).thenReturn(expectedSuitList);
        when(suitTransformer.toDtoList(anyListOf(Suit.class))).thenReturn(expectedSuitDTOList);

        List<SuitDTO> actual = suitService.getSuits();
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
        when(projectService.getProjectByProjectId(anyLong())).thenReturn(expectedProject);
        when(suitDAO.findOne(anyLong())).thenReturn(expectedSuit);
        when(suitTransformer.toDto(any())).thenReturn(expectedSuitDTO);

        SuitDTO actual = suitService.getSuitDTO(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);

        assertEquals(expectedSuitDTO, actual);
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

        expectedSuitDTO.setId(null);
        SuitDTO actualAddedSuitDTO = suitService.addSuit(1L, expectedSuitDTO);
        assertEquals(expectedSuitDTO, actualAddedSuitDTO);

        verify(caseVersionDAO).save(eq(expectedSuit.getCases()));
        verify(suitTransformer).toDto(eq(expectedSuit));
    }

    @Test
    public void update_Suit_Success() throws MethodArgumentNotValidException {
        when(cascadeUpdateService.cascadeSuitCasesUpdate(SIMPLE_PROJECT_ID,
            SIMPLE_SUIT_ID, expectedSuitDTO)).thenReturn(anyList());
        when(projectService.getProjectByProjectId(SIMPLE_PROJECT_ID)).thenReturn(expectedProject);
        when(suitDAO.findOne(anyLong())).thenReturn(expectedSuit);
        when(suitDAO.save(expectedSuit)).thenReturn(expectedSuit);
        when(suitTransformer.toDto(expectedSuit)).thenReturn(expectedSuitDTO);

        SuitUpdateDTO expectedUpdatedSuitDTOwithFailedStepIds =
            new SuitUpdateDTO(expectedSuitDTO, Collections.emptyList());
        SuitUpdateDTO actualUpdatedSuitDTOwithFailedStepIds =
            suitService.updateSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, expectedSuitDTO);

        assertEquals(expectedUpdatedSuitDTOwithFailedStepIds, actualUpdatedSuitDTOwithFailedStepIds);
        verify(cascadeUpdateService).cascadeSuitCasesUpdate(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID,
            expectedSuitDTO);
        verify(projectService).getProjectByProjectId(eq(SIMPLE_PROJECT_ID));
        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(suitDAO).save(eq(expectedSuit));
        verify(suitTransformer).toDto(eq(expectedSuit));
    }

    @Test(expected = NotFoundException.class)
    public void update_Suit_NotFoundException() throws MethodArgumentNotValidException {
        when(cascadeUpdateService.cascadeSuitCasesUpdate(SIMPLE_PROJECT_ID,
            SIMPLE_SUIT_ID, expectedSuitDTO)).thenReturn(null);
        when(suitDAO.findOne(anyLong())).thenReturn(null);

        suitService.updateSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, new SuitDTO());
    }

    @Test(expected = NotFoundException.class)
    public void updateSuitInProject_InvalidProjectId_NotFound() throws MethodArgumentNotValidException {
        when(cascadeUpdateService.cascadeSuitCasesUpdate(SIMPLE_PROJECT_ID,
            SIMPLE_SUIT_ID, expectedSuitDTO)).thenReturn(null);
        when(projectService.getProjectByProjectId(SIMPLE_PROJECT_ID)).thenReturn(expectedProject);
        when(suitTransformer.fromDto(any())).thenReturn(expectedSuit);
        when(suitDAO.findOne(anyLong())).thenReturn(expectedSuit);
        suitService.updateSuit(INVALID_PROJECT_ID, SIMPLE_SUIT_ID, expectedSuitDTO);
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
        when(projectService.getProjectByProjectId(SIMPLE_PROJECT_ID)).thenReturn(expectedProject);
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
        final SuitRowNumberUpdateDTO update = new SuitRowNumberUpdateDTO(5L, 2);
        final SuitRowNumberUpdateDTO update1 = new SuitRowNumberUpdateDTO(4L, 1);

        final List<SuitRowNumberUpdateDTO> rowNumbers = new ArrayList<>(
            Arrays.asList(update, update1)
        );

        final List<Suit> allProjectsSuits = Lists.newArrayList(new Suit(
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
            ),
            new Suit(
                6L,
                "name3",
                "description3",
                new ArrayList<>(),
                1,
                new HashSet<>(),
                3
            ));

        expectedProject.setSuits(allProjectsSuits);

        when(projectService.getProjectByProjectId(anyLong())).thenReturn(expectedProject);

        final List<SuitRowNumberUpdateDTO> actualUpdatedSuitDTOs = suitService
            .updateSuitRowNumber(SIMPLE_PROJECT_ID, rowNumbers);

        assertThat(1, is(equalTo(allProjectsSuits.get(0).getRowNumber())));
        assertThat(2, is(equalTo(allProjectsSuits.get(1).getRowNumber())));
        assertEquals(rowNumbers, actualUpdatedSuitDTOs);

        final List<Suit> expectedUpdatedSuits = Lists
            .newArrayList(allProjectsSuits.get(0), allProjectsSuits.get(1));

        verify(suitDAO).save(eq(expectedUpdatedSuits));
    }

    @Test(expected = BadRequestException.class)
    public void updateSuitRowNumber_throwsException_thereIsNoSuchSuitInProjectWithSuchId() {
        final SuitRowNumberUpdateDTO update = new SuitRowNumberUpdateDTO(5L, 2);
        final SuitRowNumberUpdateDTO update1 = new SuitRowNumberUpdateDTO(4L, 1);
        final SuitRowNumberUpdateDTO update2 = new SuitRowNumberUpdateDTO(7L, 3);

        final List<SuitRowNumberUpdateDTO> rowNumbers = new ArrayList<>(
                Arrays.asList(update, update1, update2)
        );

        final List<Suit> allProjectsSuits = Lists.newArrayList(new Suit(
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
                ),
                new Suit(
                        6L,
                        "name3",
                        "description3",
                        new ArrayList<>(),
                        1,
                        new HashSet<>(),
                        3
                ));

        expectedProject.setSuits(allProjectsSuits);
        when(projectService.getProjectByProjectId(anyLong())).thenReturn(expectedProject);
        suitService.updateSuitRowNumber(SIMPLE_PROJECT_ID, rowNumbers);
    }


    @Test(expected = BadRequestException.class)
    public void updateSuitRowNumber_throwsException_ThereIsNoSuchSuitWithSuchId() {

        final List<SuitRowNumberUpdateDTO> rowNumbers = Lists.newArrayList(
            new SuitRowNumberUpdateDTO(5L, 2),
            new SuitRowNumberUpdateDTO(4L, 1)
        );

        when(suitDAO.findByIdInOrderById(Sets.newHashSet(5L, 4L))).thenReturn(new ArrayList<>());
        when(projectService.getProjectByProjectId(anyLong())).thenReturn(expectedProject);

        suitService.updateSuitRowNumber(SIMPLE_PROJECT_ID, rowNumbers);
    }

    @Test(expected = BadRequestException.class)
    public void updateSuitRowNumber_throwsException_IfIdOrRowNumberIsNull() {
        final List<SuitRowNumberUpdateDTO> rowNumbers = Lists.newArrayList(
            new SuitRowNumberUpdateDTO(null, null),
            new SuitRowNumberUpdateDTO(null, null),
            new SuitRowNumberUpdateDTO(null, null)
        );

        suitService.updateSuitRowNumber(SIMPLE_PROJECT_ID, rowNumbers);
    }

    @Test(expected = BadRequestException.class)
    public void updateSuitRowNumber_throwsException_DuplicateRowNumber() {
        final List<SuitRowNumberUpdateDTO> rowNumbers = Lists.newArrayList(
            new SuitRowNumberUpdateDTO(5L, 2),
            new SuitRowNumberUpdateDTO(4L, 1)
        );

        final List<Suit> retrievedListOfSuits = Collections.singletonList(new Suit(5L,
            "name",
            "description",
            new ArrayList<>(),
            3,
            new HashSet<>(),
            2));

        when(suitDAO.findByIdInOrderById(Sets.newHashSet(5L, 4L)))
            .thenReturn(retrievedListOfSuits);
        when(projectService.getProjectByProjectId(anyLong())).thenReturn(expectedProject);

        suitService.updateSuitRowNumber(SIMPLE_PROJECT_ID, rowNumbers);
    }

    @Test(expected = NotFoundException.class)
    public void getSuitsFromProject_inValidId_NotFound() {
        when(projectService.getProjectByProjectId(SIMPLE_PROJECT_ID)).thenReturn(expectedProject);
        suitService.getSuitsFromProject(INVALID_PROJECT_ID);
    }
}