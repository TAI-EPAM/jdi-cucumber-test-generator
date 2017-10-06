package com.epam.test_generator.controllers;

import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.services.SuitService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class SuitControllerTest {
    private ObjectMapper mapper = new ObjectMapper();

	private MockMvc mockMvc;

	private SuitDTO suitDTO;

	private static final long TEST_SUIT_ID = 1L;

	@Mock
	private SuitService suitService;

	@InjectMocks
	private SuitController suitController;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.standaloneSetup(suitController)
			.setControllerAdvice(new GlobalExceptionController())
			.build();
		suitDTO = new SuitDTO();
		suitDTO.setId(TEST_SUIT_ID);
		suitDTO.setName("Suit name");
		suitDTO.setPriority(1);
		suitDTO.setDescription("Suit description");
	}

	@Test
	public void getSuits_return200whenGetSuits() throws Exception {
		when(suitService.getSuits()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/suits"))
			.andDo(print())
			.andExpect(status().isOk());

		verify(suitService).getSuits();
	}

	@Test
	public void getSuits_return500whenGetSuits() throws Exception {
		when(suitService.getSuits()).thenThrow(new RuntimeException());

		mockMvc.perform(get("/suits"))
			.andDo(print())
			.andExpect(status().isInternalServerError());

		verify(suitService).getSuits();
	}

	@Test
	public void getSuit_return200whenGetSuit() throws Exception {
		when(suitService.getSuit(anyLong())).thenReturn(suitDTO);

		mockMvc.perform(get("/suits/" + TEST_SUIT_ID))
			.andDo(print())
			.andExpect(status().isOk());

		verify(suitService).getSuit(eq(TEST_SUIT_ID));
	}

	@Test
	public void getSuit_return404whenGetSuit() throws Exception {
		when(suitService.getSuit(anyLong())).thenReturn(null);

		mockMvc.perform(get("/suits/" + TEST_SUIT_ID))
			.andDo(print())
			.andExpect(status().isNotFound());

		verify(suitService).getSuit(eq(TEST_SUIT_ID));
	}

	@Test
	public void getSuit_return500whenGetSuit() throws Exception {
		when(suitService.getSuit(anyLong())).thenThrow(new RuntimeException());

		mockMvc.perform(get("/suits/" + TEST_SUIT_ID))
			.andDo(print())
			.andExpect(status().isInternalServerError());

		verify(suitService).getSuit(eq(TEST_SUIT_ID));
	}

	@Test
	public void editSuit_return200whenEditSuit() throws Exception {
		when(suitService.getSuit(anyLong())).thenReturn(suitDTO);

		mockMvc.perform(put("/suits/" + TEST_SUIT_ID)
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(suitDTO)))
			.andDo(print())
			.andExpect(status().isOk());

		verify(suitService).updateSuit(anyLong(), any(SuitDTO.class));
	}

	//TODO create validation
	@Ignore
	public void editSuit_return400whenEditSuitWithNullName() throws Exception {
		suitDTO.setName(null);

		mockMvc.perform(put("/suits/" + TEST_SUIT_ID)
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(suitDTO)))
			.andDo(print())
			.andExpect(status().isBadRequest());

		verify(suitService, times(0)).updateSuit(anyLong(), any(SuitDTO.class));
	}

	//TODO create validation
	@Ignore
	public void editSuit_return400whenEditWithMoreThanTheRequiredPriority() throws Exception {
		suitDTO.setPriority(6);

		mockMvc.perform(put("/suits/" + TEST_SUIT_ID)
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(suitDTO)))
			.andDo(print())
			.andExpect(status().isBadRequest());

		verify(suitService, times(0)).updateSuit(anyLong(),any(SuitDTO.class));
	}

	//TODO create validation
	@Ignore
	public void editSuit_return400whenEditWithLessThanTheRequiredPriority() throws Exception {
		suitDTO.setPriority(-1);

		mockMvc.perform(put("/suits/" + TEST_SUIT_ID)
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(suitDTO)))
			.andDo(print())
			.andExpect(status().isBadRequest());

		verify(suitService, times(0)).updateSuit(anyLong(), any(SuitDTO.class));
	}

	@Test
	public void editSuit_return201whenSuitNotExist() throws Exception {
		when(suitService.getSuit(anyLong())).thenReturn(null);

		mockMvc.perform(put("/suits/" + TEST_SUIT_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(suitDTO)))
				.andDo(print())
				.andExpect(status().isCreated());

		verify(suitService, times(0)).updateSuit(anyLong(), any(SuitDTO.class));
		verify(suitService).addSuit(any(SuitDTO.class));
	}

	@Test
	public void editSuit_return500whenEditSuit() throws Exception {
		doThrow(RuntimeException.class).when(suitService).updateSuit(anyLong(), any(SuitDTO.class));
		when(suitService.getSuit(anyLong())).thenReturn(suitDTO);

		mockMvc.perform(put("/suits/" + TEST_SUIT_ID)
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(suitDTO)))
			.andDo(print())
			.andExpect(status().isInternalServerError());

		verify(suitService).updateSuit(anyLong(), any(SuitDTO.class));
	}

	@Test
	public void removeSuit_return200whenRemoveSuit() throws Exception {
		when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
		doNothing().when(suitService).removeSuit(anyLong());

		mockMvc.perform(delete("/suits/" + TEST_SUIT_ID))
			.andDo(print())
			.andExpect(status().isOk());

		verify(suitService).removeSuit(anyLong());
	}

	@Test
	public void removeSuit_return404whenRemoveSuit() throws Exception {
		when(suitService.getSuit(anyLong())).thenReturn(null);
		doNothing().when(suitService).removeSuit(anyLong());

		mockMvc.perform(delete("/suits/" + TEST_SUIT_ID))
			.andDo(print())
			.andExpect(status().isNotFound());

		verify(suitService, times(0)).removeSuit(anyLong());
	}

	@Test
	public void removeSuit_return500whenRemoveSuit() throws Exception {
		when(suitService.getSuit(anyLong())).thenReturn(suitDTO);
		doThrow(RuntimeException.class).when(suitService).removeSuit(anyLong());

		mockMvc.perform(delete("/suits/" + TEST_SUIT_ID))
			.andDo(print())
			.andExpect(status().isInternalServerError());

		verify(suitService).removeSuit(anyLong());
	}

	@Test
	public void addSuit_return201whenAddSuit() throws Exception {
		suitDTO.setId(null);
		when(suitService.addSuit(any(SuitDTO.class))).thenReturn(TEST_SUIT_ID);

		mockMvc.perform(post("/suits")
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(suitDTO)))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(content().string(String.valueOf(TEST_SUIT_ID)));

		verify(suitService).addSuit(any(SuitDTO.class));
	}

	//TODO create validation
	@Ignore
	public void addSuit_return400whenAddSuitWithNullName() throws Exception {
		suitDTO.setId(null);
		suitDTO.setName(null);
		when(suitService.addSuit(any(SuitDTO.class))).thenThrow(new RuntimeException());

		mockMvc.perform(post("/suits")
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(suitDTO)))
			.andDo(print())
			.andExpect(status().isBadRequest());

		verify(suitService, times(0)).addSuit(any(SuitDTO.class));
	}

	//TODO create validation
	@Ignore
	public void addSuit_return400whenAddSuitWithMoreThanTheRequiredPriority() throws Exception {
		suitDTO.setId(null);
		suitDTO.setPriority(6);
		when(suitService.addSuit(any(SuitDTO.class))).thenThrow(new RuntimeException());

		mockMvc.perform(post("/suits")
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(suitDTO)))
			.andDo(print())
			.andExpect(status().isBadRequest());

		verify(suitService, times(0)).addSuit(any(SuitDTO.class));
	}

	//TODO create validation
	@Ignore
	public void addSuit_return400whenAddSuitWithLessThanTheRequiredPriority() throws Exception {
		suitDTO.setId(null);
		suitDTO.setPriority(-1);
		when(suitService.addSuit(any(SuitDTO.class))).thenThrow(new RuntimeException());

		mockMvc.perform(post("/suits")
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(suitDTO)))
			.andDo(print())
			.andExpect(status().isBadRequest());

		verify(suitService, times(0)).addSuit(any(SuitDTO.class));
	}

	@Test
	public void addSuit_return500whenAddSuit() throws Exception {
		suitDTO.setId(null);
		when(suitService.addSuit(any(SuitDTO.class))).thenThrow(new RuntimeException());

		mockMvc.perform(post("/suits")
			.contentType(MediaType.APPLICATION_JSON)
			.content(mapper.writeValueAsString(suitDTO)))
			.andDo(print())
			.andExpect(status().isInternalServerError());

		verify(suitService).addSuit(any(SuitDTO.class));
	}
}