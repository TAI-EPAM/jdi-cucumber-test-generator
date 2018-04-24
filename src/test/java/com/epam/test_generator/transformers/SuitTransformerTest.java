package com.epam.test_generator.transformers;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.when;

import com.epam.test_generator.controllers.caze.CaseDTOsTransformer;
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
import java.time.ZonedDateTime;
import java.util.ArrayList;
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
public class SuitTransformerTest {

    private static final String SUIT_DESCRIPTION = "suit description";
    private static final String SUIT_NAME = "suit name";
    private static final int SUIT_PRIORITY = 3;
    private static final ZonedDateTime SUIT_CREATION_DATE = ZonedDateTime.parse("1995-09-01T12:45Z");
    private static final String TAG_NAME_1 = "tag_1";
    private static final String TAG_NAME_2 = "tag_2";
    private static final Long SUIT_ID = 42L;
    private static final Integer SUIT_ROW_NUMBER = 1;
    private static final Status SUIT_STATUS = Status.PASSED;

    private static final Set<Tag> tags = new HashSet<>();
    private static final Set<TagDTO> tagDtos = new HashSet<>();
    private static final List<Case> cases = new ArrayList<>();
    private static final List<CaseDTO> caseDtos = new ArrayList<>();

    @Mock
    private TagTransformer tagTransformer;

    @Mock
    private CaseDTOsTransformer caseDTOsTransformer;

    @InjectMocks
    private SuitTransformer suitTransformer;

    @Before
    public void setUp() {
        tagDtos.add(new TagDTO(TAG_NAME_1));
        tagDtos.add(new TagDTO(TAG_NAME_2));

        tags.add(new Tag(TAG_NAME_1));
        tags.add(new Tag(TAG_NAME_2));

        List<TagDTO> listTagDtos = new ArrayList<>(tagDtos);
        when(tagTransformer.toListDto(anyList())).thenReturn(listTagDtos);

        List<Tag> listTags = new ArrayList<>(tags);
        when(tagTransformer.fromListDto(anyList())).thenReturn(listTags);

        CaseDTO case_dto_1 = new CaseDTO();
        case_dto_1.setId(3L);
        caseDtos.add(case_dto_1);

        Case case_1 = new Case();
        case_1.setId(3L);
        cases.add(case_1);

        when(caseDTOsTransformer.toDtoList(anyList())).thenReturn(caseDtos);
    }

    @Test
    public void createEntityFromDTO_SuitCreateDTO_Success() throws Exception {
        SuitCreateDTO dto = new SuitCreateDTO();
        dto.setDescription(SUIT_DESCRIPTION);
        dto.setName(SUIT_NAME);
        dto.setPriority(SUIT_PRIORITY);
        dto.setTags(tagDtos);

        Suit suit = suitTransformer.fromDto(dto);

        assertEquals(SUIT_DESCRIPTION, suit.getDescription());
        assertEquals(SUIT_NAME, suit.getName());
        assertEquals(SUIT_PRIORITY, suit.getPriority().longValue());
        assertEquals(tags, suit.getTags());
    }

    @Test
    public void createSuitDTOFromEntity_Suit_Success() throws Exception {
        Suit suit = new Suit();
        suit.setId(SUIT_ID);
        suit.setName(SUIT_NAME);
        suit.setDescription(SUIT_DESCRIPTION);
        suit.setRowNumber(SUIT_ROW_NUMBER);
        suit.setPriority(SUIT_PRIORITY);
        suit.setCreationDate(SUIT_CREATION_DATE);
        suit.setStatus(SUIT_STATUS);
        suit.setTags(tags);
        suit.setCases(cases);

        SuitDTO dto = suitTransformer.toDto(suit);

        assertEquals(suit.getId(), dto.getId());
        assertEquals(suit.getName(), dto.getName());
        assertEquals(suit.getDescription(), dto.getDescription());
        assertEquals(suit.getRowNumber(), dto.getRowNumber());
        assertEquals(suit.getPriority(), dto.getPriority());
        assertEquals(suit.getCreationDate().toInstant().toEpochMilli(), dto.getCreationDate());
        assertEquals(suit.getStatus(), dto.getStatus());
        assertEquals(tagDtos, dto.getTags());
        assertEquals(caseDtos, dto.getCases());
    }

    @Test
    public void updateEntityFromDTO_SuitUpdateDTO_Success() throws Exception {
        SuitUpdateDTO dto = new SuitUpdateDTO();
        dto.setName(SUIT_NAME);
        dto.setDescription(SUIT_DESCRIPTION);
        dto.setPriority(SUIT_PRIORITY);
        dto.setTags(tagDtos);

        Suit suit = new Suit();

        suitTransformer.mapDTOToEntity(dto, suit);

        assertEquals(dto.getName(), suit.getName());
        assertEquals(dto.getDescription(), suit.getDescription());
        assertEquals(dto.getPriority(), suit.getPriority());
        assertEquals(tags, suit.getTags());
    }

}