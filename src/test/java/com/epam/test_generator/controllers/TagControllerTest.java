package com.epam.test_generator.controllers;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.dto.TagDTO;
import com.epam.test_generator.services.CaseService;
import com.epam.test_generator.services.SuitService;
import com.epam.test_generator.services.TagService;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.services.exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class TagControllerTest {

    private static final long SIMPLE_SUIT_ID = 1L;
    private static final long SIMPLE_CASE_ID = 2L;
    private static final long SIMPLE_TAG_ID = 3L;
    private ObjectMapper mapper = new ObjectMapper();
    private MockMvc mockMvc;
    private TagDTO tagDTO;
    private Set<TagDTO> tagDTOSet;
    private CaseDTO caseDTO;
    private List<CaseDTO> caseDTOList;
    private SuitDTO suitDTO;
    @Mock
    private SuitService suitService;

    @Mock
    private CaseService caseService;

    @Mock
    private TagService tagService;

    @InjectMocks
    private TagController tagController;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(tagController)
            .setControllerAdvice(new GlobalExceptionController())
            .build();

        suitDTO = new SuitDTO();
        suitDTO.setId(SIMPLE_SUIT_ID);
        suitDTO.setName("Suit name");
        suitDTO.setPriority(1);
        suitDTO.setDescription("Suit description");

        caseDTO = new CaseDTO();
        caseDTO.setId(SIMPLE_CASE_ID);
        caseDTO.setDescription("case1");
        caseDTO.setPriority(1);
        caseDTO.setSteps(new ArrayList<>());

        caseDTOList = new ArrayList<>();
        caseDTOList.add(caseDTO);

        suitDTO.setCases(caseDTOList);

        tagDTO = new TagDTO();
        tagDTO.setId(SIMPLE_TAG_ID);
        tagDTO.setName("Simple tag");

        tagDTOSet = new HashSet<>();
        tagDTOSet.add(tagDTO);

        caseDTO.setTags(tagDTOSet);
    }

    @Test
    public void testGetAllTagsFromAllCasesInSuit_return200whenGetAllTagsFromAllCasesInSuit()
        throws Exception {
        when(tagService.getAllTagsFromAllCasesInSuit(anyLong())).thenReturn(tagDTOSet);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/tags"))
            .andDo(print())
            .andExpect(status().isOk());

        verify(tagService).getAllTagsFromAllCasesInSuit(anyLong());
    }

    @Test
    public void testGetAllTagsFromAllCasesInSuit_return404whenSuitNotExist() throws Exception {
        when(tagService.getAllTagsFromAllCasesInSuit(anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/tags"))
            .andDo(print())
            .andExpect(status().isNotFound());

        verify(tagService).getAllTagsFromAllCasesInSuit(anyLong());
    }

    @Test
    public void testGetAllTagsFromAllCasesInSuit_return500whenRuntimeException() throws Exception {
        when(tagService.getAllTagsFromAllCasesInSuit(anyLong())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/tags"))
            .andDo(print())
            .andExpect(status().isInternalServerError());

        verify(tagService).getAllTagsFromAllCasesInSuit(anyLong());
    }

    @Test
    public void testGetTags_return200whenGetTags() throws Exception {
        when(caseService.getCaseDTO(anyLong(), anyLong())).thenReturn(caseDTO);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags"))
            .andDo(print())
            .andExpect(status().isOk());

        verify(caseService).getCaseDTO(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testGetTags_return404whenSuitNotExistOrCaseNotExist() throws Exception {
        when(caseService.getCaseDTO(anyLong(), anyLong())).thenThrow(NotFoundException.class);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags"))
            .andDo(print())
            .andExpect(status().isNotFound());

        verify(caseService).getCaseDTO(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testGetTags_return400whenSuitNotContainsCase() throws Exception {
        when(caseService.getCaseDTO(anyLong(), anyLong())).thenThrow(BadRequestException.class);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags"))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(caseService).getCaseDTO(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testAddTag_return201whenAddNewTag() throws Exception {
        tagDTO.setId(null);
        when(tagService.addTagToCase(anyLong(), anyLong(), any(TagDTO.class)))
            .thenReturn(SIMPLE_TAG_ID);

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(tagDTO)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(content().string(String.valueOf(SIMPLE_TAG_ID)));

        verify(tagService).addTagToCase(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(tagDTO));
    }

    @Test
    public void testAddTag_return404whenSuitNotExistOrCaseNotExist() throws Exception {
        when(tagService.addTagToCase(anyLong(), anyLong(), any(TagDTO.class)))
            .thenThrow(NotFoundException.class);

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(tagDTO)))
            .andDo(print())
            .andExpect(status().isNotFound());

        verify(tagService).addTagToCase(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), any(TagDTO.class));
    }

    @Test
    public void testAddTag_return400whenSuitNotContainsCase() throws Exception {
        when(tagService.addTagToCase(anyLong(), anyLong(), any(TagDTO.class)))
            .thenThrow(BadRequestException.class);

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(tagDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(tagService).addTagToCase(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), any(TagDTO.class));
    }

    @Test
    public void testAddTag_return500whenRuntimeException() throws Exception {
        tagDTO.setId(null);
        when(tagService.addTagToCase(anyLong(), anyLong(), any(TagDTO.class)))
            .thenThrow(new RuntimeException());

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags")
            .contentType(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(tagDTO)))
            .andDo(print())
            .andExpect(status().isInternalServerError());

        verify(tagService).addTagToCase(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), any(TagDTO.class));
    }

    @Test
    public void testUpdateTag_return200whenUpdateTag() throws Exception {
        mockMvc.perform(
            put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags/" + SIMPLE_TAG_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(tagDTO)))
            .andDo(print())
            .andExpect(status().isOk());

        verify(tagService).updateTag(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_TAG_ID),
            any(TagDTO.class));
    }

    @Test
    public void testUpdateTag_return404whenSuitNotExistOrCaseNotExist() throws Exception {
        doThrow(NotFoundException.class).when(tagService)
            .updateTag(anyLong(), anyLong(), anyLong(), any(TagDTO.class));

        mockMvc.perform(
            put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags/" + SIMPLE_TAG_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(tagDTO)))
            .andDo(print())
            .andExpect(status().isNotFound());

        verify(tagService).updateTag(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_TAG_ID),
            any(TagDTO.class));
    }

    @Test
    public void testUpdateTag_return400whenSuitNotContainsCaseOrCaseNotContainTag()
        throws Exception {
        suitDTO.setCases(null);
        doThrow(BadRequestException.class).when(tagService)
            .updateTag(anyLong(), anyLong(), anyLong(), any(TagDTO.class));

        mockMvc.perform(
            put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags/" + SIMPLE_TAG_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(tagDTO)))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(tagService).updateTag(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_TAG_ID),
            any(TagDTO.class));
    }

    @Test
    public void testUpdateTag_return500whenRuntimeException() throws Exception {
        doThrow(RuntimeException.class).when(tagService)
            .updateTag(anyLong(), anyLong(), anyLong(), any(TagDTO.class));

        mockMvc.perform(
            put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags/" + SIMPLE_TAG_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(tagDTO)))
            .andDo(print())
            .andExpect(status().isInternalServerError());

        verify(tagService).updateTag(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_TAG_ID),
            any(TagDTO.class));
    }

    @Test
    public void testRemoveTag_return200whenRemoveTag() throws Exception {

        mockMvc.perform(delete(
            "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags/" + SIMPLE_TAG_ID))
            .andDo(print())
            .andExpect(status().isOk());

        verify(tagService).removeTag(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_TAG_ID));
    }

    @Test
    public void testRemoveTag_return404whenSuitNotExistOrCaseNotExistOrTagNotExist()
        throws Exception {
        doThrow(NotFoundException.class).when(tagService)
            .removeTag(anyLong(), anyLong(), anyLong());

        mockMvc.perform(delete(
            "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags/" + SIMPLE_TAG_ID))
            .andDo(print())
            .andExpect(status().isNotFound());

        verify(tagService).removeTag(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_TAG_ID));
    }

    @Test
    public void testRemoveTag_return400whenSuitNotContainsCasOrCaseNotContainTage()
        throws Exception {
        doThrow(BadRequestException.class).when(tagService)
            .removeTag(anyLong(), anyLong(), anyLong());

        mockMvc.perform(delete(
            "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags/" + SIMPLE_TAG_ID))
            .andDo(print())
            .andExpect(status().isBadRequest());

        verify(tagService).removeTag(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_TAG_ID));
    }

    @Test
    public void testRemoveTag_return500whenRuntimeException() throws Exception {
        doThrow(RuntimeException.class).when(tagService).removeTag(anyLong(), anyLong(), anyLong());

        mockMvc.perform(delete(
            "/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags/" + SIMPLE_TAG_ID))
            .andDo(print())
            .andExpect(status().isInternalServerError());

        verify(tagService).removeTag(eq(SIMPLE_SUIT_ID), eq(SIMPLE_CASE_ID), eq(SIMPLE_TAG_ID));
    }
}