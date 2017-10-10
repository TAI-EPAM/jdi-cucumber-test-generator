package com.epam.test_generator.services;

import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.dto.TagDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.transformers.CaseTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CaseServiceTest {

    private static final long SIMPLE_SUIT_ID = 1L;
    private static final long SIMPLE_CASE_ID = 2L;

    private Suit suit;
    private SuitDTO expectedSuit;

    private Case caze;
    private CaseDTO expetedCaze;

    private List<Case> listCases;
    private List<CaseDTO> expectedListCases;

    private List<Step> listSteps;
    private List<StepDTO> expectedListSteps;
    private Set<Tag> setTags;
    private Set<TagDTO> expectedSetTags;

    @Mock
    private SuitDAO suitDAO;

    @Mock
    private CaseDAO caseDAO;

    @Mock
    private CaseTransformer caseTransformer;

    @InjectMocks
    private CaseService caseService;

    @Before
    public void setUp() {
        listCases = new ArrayList<>();
        listCases.add(new Case(1L, "Case 1", listSteps, 1, setTags));
        listCases.add(new Case(2L, "Case 2", listSteps, 2, setTags ));

        expectedListCases = new ArrayList<>();
        expectedListCases.add(new CaseDTO(1L, "Case 1", expectedListSteps, 1, expectedSetTags));
        expectedListCases.add(new CaseDTO(2L, "Case 2", expectedListSteps, 2, expectedSetTags));

        caze = new Case(SIMPLE_CASE_ID, "Case desc", listSteps, 1, setTags);
        expetedCaze = new CaseDTO(SIMPLE_CASE_ID, "Case desc", expectedListSteps, 1, expectedSetTags);

        suit = new Suit(SIMPLE_SUIT_ID, "Suit 1", "Suit desc", listCases, 1, "tag1");
        expectedSuit = new SuitDTO(SIMPLE_SUIT_ID, "Suit 1", "Suit desc", expectedListCases, 1, "tag1");
    }

    @Test
    public void getCaseTest() throws Exception {
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(caseTransformer.toDto(any(Case.class))).thenReturn(expetedCaze);

        CaseDTO actualCase = caseService.getCase(SIMPLE_CASE_ID);
        assertEquals(expetedCaze, actualCase);

        verify(caseDAO).findOne(eq(SIMPLE_CASE_ID));
        verify(caseTransformer).toDto(any(Case.class));
    }

    @Test
    public void addCaseToSuitTest() throws Exception {
        Case newCase = new Case(3L, "Case 3", listSteps, 2, setTags);
        CaseDTO newCaseDTO = new CaseDTO(null, "Case 3", expectedListSteps, 2, expectedSetTags);

        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(caseTransformer.fromDto(any(CaseDTO.class))).thenReturn(newCase);
        when(caseDAO.save(any(Case.class))).thenReturn(newCase);

        Long actualId = caseService.addCaseToSuit(SIMPLE_SUIT_ID, newCaseDTO);
        assertEquals(newCase.getId(), actualId);

        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(caseTransformer).fromDto(any(CaseDTO.class));
        verify(caseDAO).save(any(Case.class));
    }

    @Test
    public void updateCaseTest() throws Exception {
        CaseDTO updateCaseDTO = new CaseDTO(null, "New Case desc", null, null, null);

        when(caseDAO.findOne(anyLong())).thenReturn(caze);

        caseService.updateCase(SIMPLE_CASE_ID, updateCaseDTO);

        verify(caseDAO).findOne(eq(SIMPLE_CASE_ID));
        verify(caseTransformer).mapDTOToEntity(any(CaseDTO.class), eq(caze));
        verify(caseDAO).save(eq(caze));
    }

    @Test
    public void removeCaseTest() throws Exception {
        when(suitDAO.findOne(anyLong())).thenReturn(suit);

        caseService.removeCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID);

        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(caseDAO).delete(eq(SIMPLE_CASE_ID));
    }

    @Test
    public void removeCasesTest() throws Exception {
        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        List<Long> deleteCaseIds = Arrays.asList(1L, 2L);

        caseService.removeCases(SIMPLE_SUIT_ID, deleteCaseIds);

        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(caseDAO).delete(eq(1L));
        verify(caseDAO).delete(eq(2L));
    }
}