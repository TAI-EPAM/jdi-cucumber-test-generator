package com.epam.test_generator.controllers.tag;

import com.epam.test_generator.controllers.tag.request.TagCreateDTO;
import com.epam.test_generator.controllers.tag.request.TagUpdateDTO;
import com.epam.test_generator.controllers.tag.response.TagDTO;
import com.epam.test_generator.entities.Tag;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TagTransformerTest {

    @InjectMocks
    private TagTransformer tagTransformer;

    private Tag tag;
    private TagDTO tagDTO;
    private static final String NAME = "NAME";
    private static final Long ID = 1L;

    @Before
    public void setUp() {
        tag = new Tag();
        tag.setName(NAME);

        tagDTO = new TagDTO();
        tagDTO.setName(NAME);
    }

    @Test
    public void toDto_Tag_Success() {
        TagDTO expectedTagDto = tagDTO;

        TagDTO resultTagDto = tagTransformer.toDto(tag);
        Assert.assertEquals(expectedTagDto, resultTagDto);
    }

    @Test
    public void fromDto_TagCreateDTO_Success() {
        TagCreateDTO tagCreateDTO = new TagCreateDTO();
        tagCreateDTO.setName(NAME);

        Tag resultTag = tagTransformer.fromDto(tagCreateDTO);
        Assert.assertEquals(resultTag, tag);
    }

    @Test
    public void updateFromDto_TagUpdateDTO_Success() {
        TagUpdateDTO tagUpdateDTO = new TagUpdateDTO();
        tagUpdateDTO.setName(NAME+ID);

        Tag expectedTag = new Tag();
        expectedTag.setName(NAME+ID);

        Tag resultTag = tagTransformer.updateFromDto(tagUpdateDTO, tag);
        Assert.assertEquals(resultTag, expectedTag);
    }

    @Test
    public void toDtoSet_Tags_Success() {
        Set<Tag> tags = new HashSet<>(Collections.singletonList(tag));

        Set<TagDTO> expectedDTOs = new HashSet<>(Collections.singletonList(tagDTO));

        Set<TagDTO> resultDTOs = tagTransformer.toDtoSet(tags);

        Assert.assertEquals(expectedDTOs, resultDTOs);
    }

    @Test
    public void fromDtoSet_TagDTOs_Success() {
        Set<TagDTO> DTOs = new HashSet<>(Collections.singletonList(tagDTO));
        Set<Tag> expectedTags = new HashSet<>(Collections.singletonList(tag));

        Set<Tag> resultTags = tagTransformer.fromDtoSet(DTOs);
        Assert.assertEquals(resultTags, expectedTags);
    }
}
