package com.epam.test_generator.services;

import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dao.interfaces.TagDAO;
import com.epam.test_generator.dto.TagDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.transformers.TagTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class TagServiceTest {

    @Mock
    private TagDAO tagDAO;

    @Mock
    private CaseDAO caseDAO;

    @Mock
    private SuitDAO suitDAO;

    @Mock
    private TagTransformer tagTransformer;

    @InjectMocks
    private TagService tagService;

    Set<Tag> expectedTagsList;
    Set<TagDTO> expectedTagsDTOList;

    Tag expectedTag;
    TagDTO expectedTagDTO;


    @Before
    public void setUp() {
        expectedTagsList = new HashSet<>();
        expectedTagsDTOList = new HashSet<>();

        expectedTag = new Tag("tag1");
        expectedTagsList.add(expectedTag);

        expectedTagDTO = new TagDTO("tag1");
        expectedTagsDTOList.add(expectedTagDTO);
    }

    @Test
    public void getAllTagsFromAllCasesInSuitTest() {
        Suit suit = new Suit(1L, "suit1", "desc1", new ArrayList<>(), 1, "tag1");
        Case caze = new Case(2L, "desc2", new ArrayList<>(), 1, expectedTagsList);

        suit.getCases().add(caze);

        when(suitDAO.findOne(anyLong())).thenReturn(suit);
        when(tagTransformer.toDto(any(Tag.class))).thenReturn(expectedTagDTO);

        Set<TagDTO> actual = tagService.getAllTagsFromAllCasesInSuit(1L);

        assertEquals(expectedTagsDTOList, actual);
    }

    @Test
    public void getTagTest() {


    }

    @Test
    public void addTagToCaseTest() {

    }

    @Test
    public void updateTagTest() {

    }

    @Test
    public void removeTagTest() {
        Case caze = new Case();
        caze.setTags(expectedTagsList);

        when(caseDAO.findOne(anyLong())).thenReturn(caze);
        when(tagDAO.findOne(anyLong())).thenReturn(expectedTag);

        tagService.removeTag(1L, 2L);
        verify(tagDAO).delete(2L);
    }
}
