package com.epam.test_generator.services;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.EditCaseDTO;
import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.dto.TagDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Step;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.services.exceptions.NotFoundException;
import com.epam.test_generator.transformers.CaseTransformer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CaseServiceTest {

    private static final long SIMPLE_SUIT_ID = 1L;
    private static final long SIMPLE_CASE_ID = 2L;

    private Suit suit;

	private Case caze;
    private CaseDTO expectedCase;

	private List<Step> listSteps = new ArrayList<>();
    private List<StepDTO> expectedListSteps = new ArrayList<>();
    private Set<Tag> setTags = new HashSet<>();
    private Set<TagDTO> expectedSetTags = new HashSet<>();

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
		List<Case> listCases = new ArrayList<>();

		listCases.add(new Case(1L, "Case 1", listSteps, 1, setTags));
        listCases.add(new Case(2L, "Case 2", listSteps, 2, setTags ));

        caze = new Case(SIMPLE_CASE_ID, "Case desc", listSteps, 1, setTags);
        expectedCase = new CaseDTO(SIMPLE_CASE_ID, "Case desc", expectedListSteps, 1, expectedSetTags);
		suit = new Suit(SIMPLE_SUIT_ID, "Suit 1", "Suit desc", listCases, 1, "tag1");
    }

    @Test
    public void getCaseTest() throws Exception {
        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(caseTransformer.toDto(any(Case.class))).thenReturn(expectedCase);

        CaseDTO actualCase = caseService.getCase(SIMPLE_SUIT_ID,SIMPLE_CASE_ID);
        assertEquals(expectedCase, actualCase);

        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(caseDAO).findOne(eq(SIMPLE_CASE_ID));
        verify(caseTransformer).toDto(any(Case.class));
    }

	@Test(expected = NotFoundException.class)
	public void getCaseTest_expectNotFoundExceptionFromSuit() {
		when(suitDAO.findOne(anyLong())).thenReturn(null);

		caseService.getCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
	}

	@Test(expected = NotFoundException.class)
	public void getCaseTest_expectNotFoundExceptionFromCase() {
		when(suitDAO.findOne(anyLong())).thenReturn(suit);
		when(caseDAO.findOne(anyLong())).thenReturn(null);

		caseService.getCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
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

	@Test(expected = NotFoundException.class)
	public void addCaseToSuitTest_expectNotFoundExceptionFromSuit() {
		when(suitDAO.findOne(anyLong())).thenReturn(null);

		caseService.addCaseToSuit(SIMPLE_SUIT_ID, new CaseDTO());
	}

    @Test
    public void updateCaseTest() throws Exception {
        EditCaseDTO updateCaseDTO = new EditCaseDTO("New Case desc", null);

        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(caseDAO.findOne(anyLong())).thenReturn(caze);

        caseService.updateCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, updateCaseDTO);

        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(caseDAO).findOne(eq(SIMPLE_CASE_ID));
        verify(caseDAO).save(eq(caze));
    }

	@Test(expected = NotFoundException.class)
	public void updateCaseTest_expectNotFoundExceptionFromSuit() {
		when(suitDAO.findOne(anyLong())).thenReturn(null);

		caseService.updateCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, new EditCaseDTO());
	}

	@Test(expected = NotFoundException.class)
	public void updateCaseTest_expectNotFoundExceptionFromCase() {
		when(suitDAO.findOne(anyLong())).thenReturn(suit);
		when(caseDAO.findOne(anyLong())).thenReturn(null);

		caseService.updateCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID, new EditCaseDTO());
	}

    @Test
    public void removeCaseTest() throws Exception {
        when(suitDAO.findOne(anyLong())).thenReturn(suit);
		when(caseDAO.findOne(anyLong())).thenReturn(caze);
        doNothing().when(caseDAO).delete(caze);

        caseService.removeCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID);

        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
        verify(caseDAO).delete(eq(SIMPLE_CASE_ID));
    }

	@Test(expected = NotFoundException.class)
	public void removeCaseTest_expectNotFoundExceptionFromSuit() {
		when(suitDAO.findOne(anyLong())).thenReturn(null);

		caseService.removeCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
	}

	@Test(expected = NotFoundException.class)
	public void removeCaseTest_expectNotFoundExceptionFromCase() {
		when(suitDAO.findOne(anyLong())).thenReturn(suit);
		when(caseDAO.findOne(anyLong())).thenReturn(null);

		caseService.removeCase(SIMPLE_SUIT_ID, SIMPLE_CASE_ID);
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

	@Test(expected = NotFoundException.class)
	public void removeCasesTest_expectNotFoundExceptionFromSuit() {
		List<Long> caseIds = new ArrayList<>();

		when(suitDAO.findOne(anyLong())).thenReturn(null);

		caseIds.add(SIMPLE_CASE_ID);
		caseService.removeCases(SIMPLE_SUIT_ID, caseIds);
	}

}