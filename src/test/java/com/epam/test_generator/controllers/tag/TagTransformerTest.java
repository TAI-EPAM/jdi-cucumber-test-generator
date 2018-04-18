package com.epam.test_generator.controllers.tag;

import com.epam.test_generator.controllers.tag.TagTransformer;
import com.epam.test_generator.controllers.tag.request.TagCreateDTO;
import com.epam.test_generator.controllers.tag.request.TagUpdateDTO;
import com.epam.test_generator.controllers.tag.response.TagDTO;
import com.epam.test_generator.entities.Tag;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TagTransformerTest {

    @InjectMocks
    private TagTransformer tagTransformer;

    private Tag tag;
    private TagDTO tagDTO;
    private static final String NAME = "NAME";
    private static final Long ID = 1L;

    @Before
    public void setUp() throws Exception {
        tag = new Tag();
        tag.setName(NAME);

        tagDTO = new TagDTO();
        tagDTO.setId(ID);
        tagDTO.setName(NAME);
    }

    @Test
    public void toDto_Tag_Success() {
        TagDTO expectedTagDto = tagDTO;

        tag.setId(ID);

        TagDTO resultTagDto = tagTransformer.toDto(tag);
        Assert.assertEquals(expectedTagDto, resultTagDto);
    }

    @Test
    public void fromDto_TagCreateDTO_Success() {
        TagCreateDTO tagCreateDTO = new TagCreateDTO();
        tagCreateDTO.setName(NAME);

        tag.setId(null);

        Tag resultTag = tagTransformer.fromDto(tagCreateDTO);
        Assert.assertEquals(resultTag, tag);
    }

    @Test
    public void updateFromDto_TagUpdateDTO_Success() {
        TagUpdateDTO tagUpdateDTO = new TagUpdateDTO();
        tagUpdateDTO.setName(NAME+ID);

        Tag expectedTag = new Tag();
        expectedTag.setName(NAME+ID);

        tag.setId(null);

        Tag resultTag = new Tag();
        resultTag = tagTransformer.updateFromDto(tagUpdateDTO, tag);
        Assert.assertEquals(resultTag, expectedTag);
    }

    @Test
    public void toListDto_Tags_Success() {
        tag.setId(ID);
        List<Tag> tags = Collections.singletonList(tag);

        List<TagDTO> expectedDTOs = Collections.singletonList(tagDTO);

        List<TagDTO> resultDTOs = tagTransformer.toListDto(tags);
        Assert.assertEquals(expectedDTOs, resultDTOs);
    }

    @Test
    public void fromListDto_TagDTOs_Success() {
        tag.setId(ID);

        List<TagDTO> DTOs = Collections.singletonList(tagDTO);
        List<Tag> expectedTags = Collections.singletonList(tag);

        List<Tag> resultTags = tagTransformer.fromListDto(DTOs);
        Assert.assertEquals(resultTags, expectedTags);
    }


}
