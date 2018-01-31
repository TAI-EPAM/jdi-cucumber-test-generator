package com.epam.test_generator.services;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyListOf;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.epam.test_generator.transformers.SuitTransformer;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SuitServiceTest {

    private static final long SIMPLE_PROJECT_ID = 0L;
    private static final long SIMPLE_SUIT_ID = 1L;
    @InjectMocks
    private SuitService suitService;
    @Mock
    private CaseVersionDAO caseVersionDAO;
    @Mock
    private ProjectService projectService;
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
    public void getSuitsTest() {
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
    public void getSuitDTOTest() {
        when(projectService.getProjectByProjectId(anyLong())).thenReturn(expectedProject);
        when(suitDAO.findOne(anyLong())).thenReturn(expectedSuit);
        when(suitTransformer.toDto(any())).thenReturn(expectedSuitDTO);

        SuitDTO actual = suitService.getSuitDTO(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);

        assertEquals(expectedSuitDTO, actual);
    }

    @Test(expected = NotFoundException.class)
    public void getSuitTest_expectNotFoundException() {
        when(suitDAO.findOne(anyLong())).thenReturn(null);

        suitService.getSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);
    }

    @Test
    public void addSuitTest() {
        when(projectService.getProjectByProjectId(anyLong())).thenReturn(expectedProject);
        when(suitDAO.save(any(Suit.class))).thenReturn(expectedSuit);

        expectedSuitDTO.setId(null);
        Long actual = suitService.addSuit(1L, expectedSuitDTO);
        assertEquals(expectedSuit.getId(), actual);
        verify(caseVersionDAO).save(eq(expectedSuit.getCases()));

    }

    @Test
    public void updateSuitTest() {
        when(projectService.getProjectByProjectId(anyLong())).thenReturn(expectedProject);
        when(suitDAO.findOne(anyLong())).thenReturn(expectedSuit);
        when(suitDAO.save(any(Suit.class))).thenAnswer(invocationOnMock -> {
            expectedSuitDTO.setName("new name");

            return null;
        });

        SuitDTO newSuitDTO = new SuitDTO(SIMPLE_SUIT_ID, "new name", "desc1");
        suitService.updateSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, newSuitDTO);
        assertEquals(expectedSuitDTO.getName(), newSuitDTO.getName());
    }

    @Test(expected = NotFoundException.class)
    public void updateSuitTest_expectNotFoundException() {
        when(suitDAO.findOne(anyLong())).thenReturn(null);

        suitService.updateSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID, new SuitDTO());
    }

    @Test(expected = NotFoundException.class)
    public void updateSuitInProject_InvalidProjectId_NotFound() {
        when(projectService.getProjectByProjectId(SIMPLE_PROJECT_ID)).thenReturn(expectedProject);
        when(suitTransformer.fromDto(any())).thenReturn(expectedSuit);
        when(suitDAO.findOne(anyLong())).thenReturn(expectedSuit);
        suitService.updateSuit(777L, SIMPLE_SUIT_ID, expectedSuitDTO);
    }

    @Test
    public void removeSuitTest() {
        when(projectService.getProjectByProjectId(anyLong())).thenReturn(expectedProject);
        when(suitDAO.findOne(anyLong())).thenReturn(expectedSuit);

        suitService.removeSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);

        verify(suitDAO).delete(anyLong());
        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));

        verify(caseVersionDAO).delete(eq(expectedSuit.getCases()));
    }

    @Test(expected = NotFoundException.class)
    public void removeSuitTest_expectNotFoundException() {
        when(suitDAO.findOne(anyLong())).thenReturn(null);

        suitService.removeSuit(SIMPLE_PROJECT_ID, SIMPLE_SUIT_ID);
    }

    @Test(expected = NotFoundException.class)
    public void removeSuitFromProject_InvalidProjectId_NotFound() {
        when(projectService.getProjectByProjectId(SIMPLE_PROJECT_ID)).thenReturn(expectedProject);
        when(suitDAO.findOne(anyLong())).thenReturn(expectedSuit);
        when(suitTransformer.toDto(any())).thenReturn(expectedSuitDTO);
        suitService.removeSuit(777L, SIMPLE_SUIT_ID);
    }

    @Test
    public void getSuitsFromProject_ValidId_Success() {
        when(projectService.getProjectByProjectId(SIMPLE_PROJECT_ID)).thenReturn(expectedProject);
        List<SuitDTO> suits = suitService.getSuitsFromProject(SIMPLE_PROJECT_ID);
        assertNotNull(suits);
    }

    @Test(expected = NotFoundException.class)
    public void getSuitsFromProject_inValidId_NotFound() {
        when(projectService.getProjectByProjectId(SIMPLE_PROJECT_ID)).thenReturn(expectedProject);
        suitService.getSuitsFromProject(777L);
    }
}