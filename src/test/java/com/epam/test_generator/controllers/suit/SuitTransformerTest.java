package com.epam.test_generator.controllers.suit;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.*;
import com.epam.test_generator.controllers.caze.CaseTransformer;
import com.epam.test_generator.controllers.caze.response.CaseDTO;
import com.epam.test_generator.controllers.suit.request.SuitCreateDTO;
import com.epam.test_generator.controllers.suit.request.SuitUpdateDTO;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.controllers.tag.TagTransformer;
import com.epam.test_generator.controllers.tag.response.TagDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SuitTransformerTest {

    private static final String SUIT_DESCRIPTION = "description";
    private static final String SUIT_NAME = "name";
    private static final int SUIT_PRIORITY = 3;
    private static final ZonedDateTime SUIT_CREATION_DATE = ZonedDateTime.parse("1995-09-01T12:45Z");
    private static final Tag TAG_1 = new Tag("tag_1");
    private static final Tag TAG_2 = new Tag("tag_2");
    private static final TagDTO TAG_DTO_1 = new TagDTO("tag_1");
    private static final TagDTO TAG_DTO_2 = new TagDTO("tag_2");
    private static final Set<Tag> TAGS = new HashSet<>(Collections.singletonList(TAG_1));
    private static final Set<Tag> NEW_TAGS = new HashSet<>(Arrays.asList(TAG_1, TAG_2));
    private static final Set<TagDTO> TAG_DTOS = new HashSet<>(Collections.singletonList(TAG_DTO_1));
    private static final Set<TagDTO> NEW_TAG_DTOS = new HashSet<>(Arrays.asList(TAG_DTO_1, TAG_DTO_2));
    private static final Long SUIT_ID = 42L;
    private static final Integer SUIT_ROW_NUMBER = 1;
    private static final Status SUIT_STATUS = Status.PASSED;
    private static final List<Case> CASES = new ArrayList<>();
    private static final List<CaseDTO> CASE_DTOS = new ArrayList<>();

    @Mock
    private TagTransformer tagTransformer;

    @Mock
    private CaseTransformer caseTransformer;

    @InjectMocks
    private SuitTransformer suitTransformer;

    @Test
    public void fromDto_SuitCreateDTO_Success() {
        SuitCreateDTO dto = new SuitCreateDTO();
        dto.setName(SUIT_NAME);
        dto.setPriority(SUIT_PRIORITY);
        dto.setDescription(SUIT_DESCRIPTION);
        dto.setTags(TAG_DTOS);

        when(tagTransformer.fromDtoSet(anySet())).thenReturn(TAGS);

        Suit suit = suitTransformer.fromDto(dto);

        assertEquals(SUIT_NAME, suit.getName());
        assertEquals(SUIT_PRIORITY, suit.getPriority().longValue());
        assertEquals(SUIT_DESCRIPTION, suit.getDescription());
        assertEquals(TAGS, suit.getTags());

        verify(tagTransformer).fromDtoSet(anySet());
    }

    @Test
    public void toDto_Suit_Success() {
        Suit suit = new Suit();
        suit.setId(SUIT_ID);
        suit.setName(SUIT_NAME);
        suit.setDescription(SUIT_DESCRIPTION);
        suit.setRowNumber(SUIT_ROW_NUMBER);
        suit.setPriority(SUIT_PRIORITY);
        suit.setStatus(SUIT_STATUS);
        suit.setTags(TAGS);
        suit.setCases(CASES);

        when(tagTransformer.toDtoSet(anySet())).thenReturn(TAG_DTOS);
        when(caseTransformer.toDtoList(anyList())).thenReturn(CASE_DTOS);

        SuitDTO dto = suitTransformer.toDto(suit);

        assertEquals(suit.getId(), dto.getId());
        assertEquals(suit.getName(), dto.getName());
        assertEquals(suit.getDescription(), dto.getDescription());
        assertEquals(suit.getRowNumber(), dto.getRowNumber());
        assertEquals(suit.getPriority(), dto.getPriority());
        assertEquals(suit.getStatus().getStatusName(), dto.getDisplayedStatusName());
        assertEquals(TAG_DTOS, dto.getTags());
        assertEquals(CASE_DTOS, dto.getCases());

        verify(tagTransformer).toDtoSet(anySet());
        verify(caseTransformer).toDtoList(anyList());
    }

    @Test
    public void toDtoList_Suits_Success() {
        Suit suit = new Suit();
        suit.setId(SUIT_ID);
        suit.setName(SUIT_NAME);
        suit.setDescription(SUIT_DESCRIPTION);
        suit.setRowNumber(SUIT_ROW_NUMBER);
        suit.setPriority(SUIT_PRIORITY);
        suit.setStatus(SUIT_STATUS);
        suit.setTags(TAGS);
        suit.setCases(CASES);
        List<Suit> suits = Collections.singletonList(suit);

        SuitDTO suitDto = new SuitDTO();
        suitDto.setId(SUIT_ID);
        suitDto.setName(SUIT_NAME);
        suitDto.setDescription(SUIT_DESCRIPTION);
        suitDto.setRowNumber(SUIT_ROW_NUMBER);
        suitDto.setPriority(SUIT_PRIORITY);
        suitDto.setDisplayedStatusName(SUIT_STATUS.getStatusName());
        suitDto.setTags(TAG_DTOS);
        suitDto.setCases(CASE_DTOS);
        List<SuitDTO> suitDtos = new ArrayList<>(Collections.singletonList(suitDto));

        when(tagTransformer.toDtoSet(anySet())).thenReturn(TAG_DTOS);
        when(caseTransformer.toDtoList(anyList())).thenReturn(CASE_DTOS);
        List<SuitDTO> actualSuitDTOs = suitTransformer.toDtoList(suits);

        assertEquals(suitDtos, actualSuitDTOs);

        verify(tagTransformer).toDtoSet(anySet());
        verify(caseTransformer).toDtoList(anyList());
    }

    @Test
    public void updateFromDto_EmptySuitAndSuitUpdateDTOWithNoNullValues_AllFieldInSuitUpdated() {
        Suit suit = new Suit();

        SuitUpdateDTO dto = new SuitUpdateDTO();
        dto.setName(SUIT_NAME);
        dto.setPriority(SUIT_PRIORITY);
        dto.setDescription(SUIT_DESCRIPTION);
        dto.setTags(TAG_DTOS);

        when(tagTransformer.fromDtoSet(anySet())).thenReturn(TAGS);
        Suit updatedSuit = suitTransformer.updateFromDto(dto, suit);

        assertEquals(dto.getName(), updatedSuit.getName());
        assertEquals(dto.getDescription(), updatedSuit.getDescription());
        assertEquals(dto.getPriority(), updatedSuit.getPriority());
        assertEquals(TAGS, updatedSuit.getTags());

        verify(tagTransformer).fromDtoSet(anySet());
    }

    @Test
    public void updateFromDto_SuitUpdateDTOWithNullNameAndTags_OnlyPriorityAndDescriptionUpdated() {
        Suit suit = new Suit();

        SuitUpdateDTO dto = new SuitUpdateDTO();
        dto.setPriority(SUIT_PRIORITY);
        dto.setDescription(SUIT_DESCRIPTION);

        Suit updatedSuit = suitTransformer.updateFromDto(dto, suit);

        assertEquals(suit.getName(), updatedSuit.getName());
        assertEquals(suit.getTags(), updatedSuit.getTags());
        assertEquals(dto.getDescription(), updatedSuit.getDescription());
        assertEquals(dto.getPriority(), updatedSuit.getPriority());

        verifyZeroInteractions(tagTransformer);
    }

    @Test
    public void updateFromDto_SuitWithSomeTagsAndSuitUpdateDTOWithEmptySetOfTags_TagsRemovedFromSuit() throws Exception {
        Suit suit = new Suit();
        suit.setTags(TAGS);

        SuitUpdateDTO dto = new SuitUpdateDTO();
        dto.setTags(Collections.emptySet());

        when(tagTransformer.fromDtoSet(anySet())).thenReturn(Collections.emptySet());

        Suit updatedSuit = suitTransformer.updateFromDto(dto, suit);

        assertTrue(updatedSuit.getTags().isEmpty());

        verify(tagTransformer).fromDtoSet(anySet());
    }

}