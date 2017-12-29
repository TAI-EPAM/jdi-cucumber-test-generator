package com.epam.test_generator.services;


import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyListOf;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dto.SuitDTO;
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

    @InjectMocks
    private SuitService suitService;

    @Mock
    private CaseVersionDAO caseVersionDAO;

    @Mock
    private SuitDAO suitDAO;

    @Mock
    private SuitTransformer suitTransformer;

    private static final long SIMPLE_SUIT_ID = 1L;

    private List<Suit> expectedSuitList;
    private List<SuitDTO> expectedSuitDTOList;

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
    }

    @Test
    public void getSuitsTest() {
        when(suitDAO.findAll()).thenReturn(expectedSuitList);
        when(suitTransformer.toDtoList(anyListOf(Suit.class))).thenReturn(expectedSuitDTOList);

        List<SuitDTO> actual = suitService.getSuits();
        assertEquals(expectedSuitDTOList, actual);
    }

    @Test
    public void getSuitTest() {
        when(suitDAO.findOne(anyLong())).thenReturn(expectedSuit);
        when(suitTransformer.toDto(any(Suit.class))).thenReturn(expectedSuitDTO);

        SuitDTO actual = suitService.getSuit(SIMPLE_SUIT_ID);

        assertEquals(expectedSuitDTO, actual);
    }

    @Test(expected = NotFoundException.class)
    public void getSuitTest_expectNotFoundException() {
        when(suitDAO.findOne(anyLong())).thenReturn(null);

        suitService.getSuit(SIMPLE_SUIT_ID);
    }

    @Test
    public void addSuitTest() {
        when(suitDAO.save(any(Suit.class))).thenReturn(expectedSuit);
        when(suitTransformer.fromDto(any(SuitDTO.class))).thenReturn(expectedSuit);

        expectedSuitDTO.setId(null);
        Long actual = suitService.addSuit(expectedSuitDTO);
        assertEquals(expectedSuit.getId(), actual);
        verify(caseVersionDAO).save(eq(expectedSuit.getCases()));

    }

    @Test
    public void updateSuitTest() {
        when(suitDAO.findOne(anyLong())).thenReturn(expectedSuit);
        when(suitDAO.save(any(Suit.class))).thenAnswer(invocationOnMock -> {
			expectedSuitDTO.setName("new name");

			return null;
		});

        SuitDTO newSuitDTO = new SuitDTO(SIMPLE_SUIT_ID, "new name", "desc1");
        suitService.updateSuit(SIMPLE_SUIT_ID, newSuitDTO);
        assertEquals(expectedSuitDTO.getName(), newSuitDTO.getName());
    }

	@Test(expected = NotFoundException.class)
	public void updateSuitTest_expectNotFoundException() {
		when(suitDAO.findOne(anyLong())).thenReturn(null);

		suitService.updateSuit(SIMPLE_SUIT_ID, new SuitDTO());
	}

    @Test
    public void removeSuitTest() {
        when(suitDAO.findOne(anyLong())).thenReturn(expectedSuit);

        suitService.removeSuit(SIMPLE_SUIT_ID);

        verify(suitDAO).delete(anyLong());
        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));

        verify(caseVersionDAO).delete(eq(expectedSuit.getCases()));
    }

	@Test(expected = NotFoundException.class)
	public void removeSuitTest_expectNotFoundException() {
		when(suitDAO.findOne(anyLong())).thenReturn(null);

		suitService.removeSuit(SIMPLE_SUIT_ID);
	}

}
