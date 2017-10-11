package com.epam.test_generator.services;


import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.transformers.SuitTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SuitServiceTest {

    @InjectMocks
    private SuitService suitService;

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

    @Test
    public void addSuitTest() {
        when(suitDAO.save(any(Suit.class))).thenReturn(expectedSuit);
        when(suitTransformer.fromDto(any(SuitDTO.class))).thenReturn(expectedSuit);

        expectedSuitDTO.setId(null);
        Long actual = suitService.addSuit(expectedSuitDTO);
        assertEquals(expectedSuit.getId(), actual);
    }

    @Test
    public void updateSuitTest() {
        when(suitDAO.findOne(anyLong())).thenReturn(expectedSuit);
        when(suitDAO.save(any(Suit.class))).thenAnswer(new Answer<Object>() {
            public Void answer(InvocationOnMock invocationOnMock) throws Throwable {
                expectedSuitDTO.setName("new name");

                return null;
            }
        });

        SuitDTO newSuitDTO = new SuitDTO(SIMPLE_SUIT_ID, "new name", "desc1");
        suitService.updateSuit(SIMPLE_SUIT_ID, newSuitDTO);
        assertEquals(expectedSuitDTO.getName(), newSuitDTO.getName());
    }

    @Test
    public void removeSuitTest() {
        when(suitDAO.findOne(anyLong())).thenReturn(expectedSuit);

        suitService.removeSuit(SIMPLE_SUIT_ID);

        verify(suitDAO).delete(anyLong());
        verify(suitDAO).findOne(eq(SIMPLE_SUIT_ID));
    }


}
