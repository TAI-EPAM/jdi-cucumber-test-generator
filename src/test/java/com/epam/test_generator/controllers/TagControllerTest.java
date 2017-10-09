package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.dto.TagDTO;
import com.epam.test_generator.services.CaseService;
import com.epam.test_generator.services.SuitService;
import com.epam.test_generator.services.TagService;
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

import java.util.*;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class TagControllerTest {

    private ObjectMapper mapper = new ObjectMapper();

    private MockMvc mockMvc;

    private TagDTO tagDTO;
    private Set<TagDTO> tagDTOSet;

    private CaseDTO caseDTO;
    private List<CaseDTO> caseDTOList;

    private SuitDTO suitDTO;

    private static final long SIMPLE_SUIT_ID = 1L;
    private static final long SIMPLE_CASE_ID = 2L;
    private static final long SIMPLE_TAG_ID = 3L;

    @Mock
    private SuitService suitService;

    @Mock
    private CaseService caseService;

    @Mock
    private TagService tagService;

    @InjectMocks
    private TagController tagController;

    @Before
    public void setUp(){
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
    public void testGetAllTagsFromAllCasesInSuit_return200whenGetAllTagsFromAllCasesInSuit() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(tagService.getAllTagsFromAllCasesInSuit(anyLong())).thenReturn(tagDTOSet);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/tags"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(tagService).getAllTagsFromAllCasesInSuit(anyLong());
    }

    @Test
    public void testGetAllTagsFromAllCasesInSuit_return404whenSuitNotExist() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(null);
        when(tagService.getAllTagsFromAllCasesInSuit(anyLong())).thenReturn(tagDTOSet);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/tags"))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(tagService, times(0)).getAllTagsFromAllCasesInSuit(anyLong());
    }

    @Test
    public void testGetAllTagsFromAllCasesInSuit_return500whenRuntimeException() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(tagService.getAllTagsFromAllCasesInSuit(anyLong())).thenThrow(new RuntimeException());

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/tags"))
                .andDo(print())
                .andExpect(status().isInternalServerError());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(tagService).getAllTagsFromAllCasesInSuit(anyLong());
    }

    @Test
    public void testGetTags_return200whenGetTags() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testGetTags_return404whenSuitNotExist() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(null);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags"))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService, times(0)).getCase(eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testGetTags_return404whenCaseNotExist() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(null);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags"))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testGetTags_return400whenSuitNotContainsCase() throws Exception {
        suitDTO.setCases(null);
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);

        mockMvc.perform(get("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags"))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testAddTag_return201whenAddNewTag() throws Exception {
        tagDTO.setId(null);
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(tagService.addTagToCase(any(TagDTO.class), anyLong())).thenReturn(SIMPLE_TAG_ID);

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(tagDTO)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(SIMPLE_TAG_ID)));

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(tagService).addTagToCase(any(TagDTO.class), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testAddTag_return404whenSuitNotExist() throws Exception {
        tagDTO.setId(null);
        when(suitService.getSuit(anyLong())).thenReturn(null);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(tagService.addTagToCase(any(TagDTO.class), anyLong())).thenReturn(SIMPLE_TAG_ID);

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(tagDTO)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService, times(0)).getCase(eq(SIMPLE_CASE_ID));
        verify(tagService, times(0)).addTagToCase(any(TagDTO.class), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testAddTag_return404whenCaseNotExist() throws Exception {
        tagDTO.setId(null);
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(null);
        when(tagService.addTagToCase(any(TagDTO.class), anyLong())).thenReturn(SIMPLE_TAG_ID);

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(tagDTO)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(tagService, times(0)).addTagToCase(any(TagDTO.class), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testAddTag_return400whenSuitNotContainsCase() throws Exception {
        tagDTO.setId(null);
        suitDTO.setCases(null);
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(tagService.addTagToCase(any(TagDTO.class), anyLong())).thenReturn(SIMPLE_TAG_ID);

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(tagDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(tagService, times(0)).addTagToCase(any(TagDTO.class), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testAddTag_return500whenRuntimeException() throws Exception {
        tagDTO.setId(null);
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(tagService.addTagToCase(any(TagDTO.class), anyLong())).thenThrow(new RuntimeException());

        mockMvc.perform(post("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(tagDTO)))
                .andDo(print())
                .andExpect(status().isInternalServerError());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(tagService).addTagToCase(any(TagDTO.class), eq(SIMPLE_CASE_ID));
    }

    @Test
    public void testUpdateTag_return200whenUpdateTag() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(tagService.getTag(anyLong())).thenReturn(tagDTO);

        mockMvc.perform(put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags/" + SIMPLE_TAG_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(tagDTO)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(tagService).getTag(eq(SIMPLE_TAG_ID));
        verify(tagService).updateTag(eq(SIMPLE_TAG_ID), any(TagDTO.class));
    }

    @Test
    public void testUpdateTag_return404whenSuitNotExist() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(null);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(tagService.getTag(anyLong())).thenReturn(tagDTO);

        mockMvc.perform(put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags/" + SIMPLE_TAG_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(tagDTO)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService, times(0)).getCase(eq(SIMPLE_CASE_ID));
        verify(tagService, times(0)).getTag(eq(SIMPLE_TAG_ID));
        verify(tagService, times(0)).updateTag(eq(SIMPLE_TAG_ID), any(TagDTO.class));
    }

    @Test
    public void testUpdateTag_return404whenCaseNotExist() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(null);
        when(tagService.getTag(anyLong())).thenReturn(tagDTO);

        mockMvc.perform(put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags/" + SIMPLE_TAG_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(tagDTO)))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(tagService, times(0)).getTag(eq(SIMPLE_TAG_ID));
        verify(tagService, times(0)).updateTag(eq(SIMPLE_TAG_ID), any(TagDTO.class));
    }

    @Test
    public void testUpdateTag_return400whenSuitNotContainsCase() throws Exception {
        suitDTO.setCases(null);
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(tagService.getTag(anyLong())).thenReturn(tagDTO);

        mockMvc.perform(put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags/" + SIMPLE_TAG_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(tagDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(tagService, times(0)).getTag(eq(SIMPLE_TAG_ID));
        verify(tagService, times(0)).updateTag(eq(SIMPLE_TAG_ID), any(TagDTO.class));
    }

    @Test
    public void testUpdateTag_return201whenTagNotExist() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(tagService.getTag(anyLong())).thenReturn(null);
        when(tagService.addTagToCase(any(TagDTO.class), anyLong())).thenReturn(SIMPLE_TAG_ID);

        mockMvc.perform(put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags/" + SIMPLE_TAG_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(tagDTO)))
                .andDo(print())
                .andExpect(status().isCreated());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(tagService).getTag(eq(SIMPLE_TAG_ID));
        verify(tagService,times(0)).updateTag(eq(SIMPLE_TAG_ID), any(TagDTO.class));
    }

    @Test
    public void testUpdateTag_return400whenCaseNotContainsTag() throws Exception {
        caseDTO.setTags(null);
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(tagService.getTag(anyLong())).thenReturn(tagDTO);

        mockMvc.perform(put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags/" + SIMPLE_TAG_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(tagDTO)))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(tagService).getTag(eq(SIMPLE_TAG_ID));
        verify(tagService,times(0)).updateTag(eq(SIMPLE_TAG_ID), any(TagDTO.class));
    }

    @Test
    public void testUpdateTag_return500whenRuntimeException() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(tagService.getTag(anyLong())).thenReturn(tagDTO);
        doThrow(RuntimeException.class).when(tagService).updateTag(anyLong(), any(TagDTO.class));

        mockMvc.perform(put("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags/" + SIMPLE_TAG_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(tagDTO)))
                .andDo(print())
                .andExpect(status().isInternalServerError());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(tagService).getTag(eq(SIMPLE_TAG_ID));
        verify(tagService).updateTag(eq(SIMPLE_TAG_ID), any(TagDTO.class));
    }

    @Test
    public void testRemoveTag_return200whenRemoveTag() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(tagService.getTag(anyLong())).thenReturn(tagDTO);

        mockMvc.perform(delete("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags/" + SIMPLE_TAG_ID))
                .andDo(print())
                .andExpect(status().isOk());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(tagService).getTag(eq(SIMPLE_TAG_ID));
        verify(tagService).removeTag(eq(SIMPLE_CASE_ID), eq(SIMPLE_TAG_ID));
    }

    @Test
    public void testRemoveTag_return404whenSuitNotExist() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(null);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(tagService.getTag(anyLong())).thenReturn(tagDTO);

        mockMvc.perform(delete("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags/" + SIMPLE_TAG_ID))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService, times(0)).getCase(eq(SIMPLE_CASE_ID));
        verify(tagService, times(0)).getTag(eq(SIMPLE_TAG_ID));
        verify(tagService, times(0)).removeTag(eq(SIMPLE_CASE_ID), eq(SIMPLE_TAG_ID));
    }

    @Test
    public void testRemoveTag_return404whenCaseNotExist() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(null);
        when(tagService.getTag(anyLong())).thenReturn(tagDTO);

        mockMvc.perform(delete("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags/" + SIMPLE_TAG_ID))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(tagService, times(0)).getTag(eq(SIMPLE_TAG_ID));
        verify(tagService, times(0)).removeTag(eq(SIMPLE_CASE_ID), eq(SIMPLE_TAG_ID));
    }

    @Test
    public void testRemoveTag_return400whenSuitNotContainsCase() throws Exception {
        suitDTO.setCases(null);
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(tagService.getTag(anyLong())).thenReturn(tagDTO);

        mockMvc.perform(delete("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags/" + SIMPLE_TAG_ID))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(tagService, times(0)).getTag(eq(SIMPLE_TAG_ID));
        verify(tagService, times(0)).removeTag(eq(SIMPLE_CASE_ID), eq(SIMPLE_TAG_ID));
    }

    @Test
    public void testRemoveTag_return404whenTagNotExist() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(tagService.getTag(anyLong())).thenReturn(null);

        mockMvc.perform(delete("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags/" + SIMPLE_TAG_ID))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(tagService).getTag(eq(SIMPLE_TAG_ID));
        verify(tagService, times(0)).removeTag(eq(SIMPLE_CASE_ID), eq(SIMPLE_TAG_ID));
    }

    @Test
    public void testRemoveTag_return400whenCaseNotContainsTag() throws Exception {
        caseDTO.setTags(null);
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(tagService.getTag(anyLong())).thenReturn(tagDTO);

        mockMvc.perform(delete("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags/" + SIMPLE_TAG_ID))
                .andDo(print())
                .andExpect(status().isBadRequest());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(tagService).getTag(eq(SIMPLE_TAG_ID));
        verify(tagService, times(0)).removeTag(eq(SIMPLE_CASE_ID), eq(SIMPLE_TAG_ID));
    }

    @Test
    public void testRemoveTag_return500whenRuntimeException() throws Exception {
        when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
        when(caseService.getCase(anyLong())).thenReturn(caseDTO);
        when(tagService.getTag(anyLong())).thenReturn(tagDTO);
        doThrow(RuntimeException.class).when(tagService).removeTag(anyLong(), anyLong());

        mockMvc.perform(delete("/suits/" + SIMPLE_SUIT_ID + "/cases/" + SIMPLE_CASE_ID + "/tags/" + SIMPLE_TAG_ID))
                .andDo(print())
                .andExpect(status().isInternalServerError());

        verify(suitService).getSuit(eq(SIMPLE_SUIT_ID));
        verify(caseService).getCase(eq(SIMPLE_CASE_ID));
        verify(tagService).getTag(eq(SIMPLE_TAG_ID));
        verify(tagService).removeTag(eq(SIMPLE_CASE_ID), eq(SIMPLE_TAG_ID));
    }
}