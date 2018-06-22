package com.epam.test_generator.services;

import static com.epam.test_generator.entities.StepType.GIVEN;
import static com.epam.test_generator.entities.StepType.THEN;
import static com.epam.test_generator.entities.StepType.WHEN;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import com.epam.test_generator.controllers.stepsuggestion.StepSuggestionTransformer;
import com.epam.test_generator.controllers.stepsuggestion.request.StepSuggestionCreateDTO;
import com.epam.test_generator.controllers.stepsuggestion.request.StepSuggestionUpdateDTO;
import com.epam.test_generator.dao.interfaces.StepDAO;
import com.epam.test_generator.controllers.stepsuggestion.response.StepSuggestionDTO;
import com.epam.test_generator.dao.interfaces.ProjectDAO;
import com.epam.test_generator.dao.interfaces.StepSuggestionDAO;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.StepSuggestion;
import com.epam.test_generator.services.exceptions.BadRequestException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@RunWith(MockitoJUnitRunner.class)
public class StepSuggestionServiceTest {

    private static final Long PROJECT_ID = 1L;
    private static final Long ID_1 = 1L;
    private static final Long ID_2 = 2L;
    private static final Long ID_3 = 3L;
    private static final String CONTENT_1 = "CONTENT 1";
    private static final String CONTENT_2 = "CONTENT 2";
    private static final String CONTENT_3 = "CONTENT 3";
    private static final Long VERSION = 0L;
    private static final String SEARCH_STRING = "text";
    private static final int NUMBER_OF_RETURN_PAGE = 10;
    private static final int PAGE_SIZE = 10;

    @Mock
    private Project project;

    @Mock
    private ProjectDAO projectDAO;

    @Mock
    private StepSuggestionDAO stepSuggestionDAO;

    @Mock
    private StepDAO stepDAO;

    @Mock
    private StepSuggestionTransformer stepSuggestionTransformer;

    @InjectMocks
    private StepSuggestionService stepSuggestionService;

    private List<StepSuggestionDTO> stepSuggestionDTOS = new ArrayList<>();
    private Set<StepSuggestion> stepSuggestions = new HashSet<>();
    private StepSuggestion expectedStepSuggestion;
    private StepSuggestionDTO expectedStepSuggestionDTO;

    @Before
    public void setUp() {
        expectedStepSuggestionDTO = new StepSuggestionDTO(ID_1, CONTENT_1, GIVEN);
        stepSuggestionDTOS.add(expectedStepSuggestionDTO);
        stepSuggestionDTOS.add(new StepSuggestionDTO(ID_2, CONTENT_2, WHEN));
        stepSuggestionDTOS.add(new StepSuggestionDTO(ID_3, CONTENT_3, THEN));

        expectedStepSuggestion = new StepSuggestion(CONTENT_1, GIVEN);
        expectedStepSuggestion.setId(ID_1);
        expectedStepSuggestion.setVersion(VERSION);
        StepSuggestion stepSuggestion2 = new StepSuggestion(CONTENT_2, WHEN);
        stepSuggestion2.setId(ID_2);
        StepSuggestion stepSuggestion3 = new StepSuggestion(CONTENT_3, WHEN);
        stepSuggestion3.setId(ID_3);
        stepSuggestions.add(expectedStepSuggestion);
        stepSuggestions.add(stepSuggestion2);
        stepSuggestions.add(stepSuggestion3);

        when(projectDAO.getOne(PROJECT_ID)).thenReturn(project);
    }

    @Test
    public void getStepsSuggestions_Success() {
        when(project.getStepSuggestions()).thenReturn(stepSuggestions);
        when(stepSuggestionTransformer.toDtoList(any())).thenReturn(stepSuggestionDTOS);

        List<StepSuggestionDTO> actualStepSuggestions =
            stepSuggestionService.getStepsSuggestions(PROJECT_ID);

        assertEquals(stepSuggestionDTOS, actualStepSuggestions);
    }

    @Test
    public void getStepSuggestionDTO_Success() {
        when(stepSuggestionDAO.getOne(ID_1)).thenReturn(expectedStepSuggestion);
        when(project.hasStepSuggestion(any())).thenReturn(true);
        when(stepSuggestionTransformer.toDto(any())).thenReturn(expectedStepSuggestionDTO);

        StepSuggestionDTO actualDto =
            stepSuggestionService.getStepSuggestionDTO(PROJECT_ID, ID_1);

        assertEquals(expectedStepSuggestionDTO, actualDto);
    }

    @Test(expected = BadRequestException.class)
    public void getStepSuggestionDTO_NotFound() {
        when(stepSuggestionDAO.getOne(ID_1)).thenReturn(expectedStepSuggestion);
        when(project.hasStepSuggestion(expectedStepSuggestion)).thenReturn(false);

        stepSuggestionService.getStepSuggestionDTO(PROJECT_ID, ID_1);
    }

    @Test
    public void getStepSuggestionsByType_Success() {
        when(project.getStepSuggestions()).thenReturn(stepSuggestions);
        when(stepSuggestionTransformer.toDto(any())).thenCallRealMethod();

        List<StepSuggestionDTO> expectedDTOs = stepSuggestionDTOS.stream()
            .filter(s -> s.getType().equals(GIVEN))
            .peek(s -> s.setVersion(0L))
            .collect(Collectors.toList());
        List<StepSuggestionDTO> actualDTOs =
            stepSuggestionService.getStepsSuggestionsByType(PROJECT_ID, GIVEN);

        assertEquals(expectedDTOs, actualDTOs);
    }

    @Test
    public void addStepSuggestion_Success() {
        StepSuggestionCreateDTO createDTO =
            new StepSuggestionCreateDTO(CONTENT_1, GIVEN);
        when(stepSuggestionTransformer.toDto(any(StepSuggestion.class))).thenReturn(expectedStepSuggestionDTO);
        when(stepSuggestionTransformer.fromDto(createDTO)).thenReturn(expectedStepSuggestion);
        when(stepSuggestionDAO.save(any(StepSuggestion.class)))
            .thenReturn(expectedStepSuggestion);
        Long id= stepSuggestionService
            .addStepSuggestion(PROJECT_ID, createDTO).getId();

        assertEquals(ID_1, id);
        verify(stepSuggestionDAO).save(expectedStepSuggestion);
    }

    @Test
    public void updateStepSuggestion_CorrectData_Success() {
        when(stepSuggestionDAO.getOne(ID_1)).thenReturn(expectedStepSuggestion);
        when(project.hasStepSuggestion(expectedStepSuggestion)).thenReturn(true);
        when(stepSuggestionTransformer.toDto(any())).thenCallRealMethod();
        doCallRealMethod().when(stepSuggestionTransformer).updateFromDto(any(), any());

        StepSuggestionUpdateDTO updateDTO = new StepSuggestionUpdateDTO();
        updateDTO.setContent(CONTENT_2);
        updateDTO.setVersion(0L);

        StepSuggestionDTO expectedDto = new StepSuggestionDTO(ID_1, CONTENT_2, GIVEN);
        expectedDto.setVersion(0L);

        StepSuggestionDTO actualDto =
            stepSuggestionService.updateStepSuggestion(PROJECT_ID, ID_1, updateDTO);

        assertEquals(expectedDto, actualDto);
    }

    @Test(expected = OptimisticLockingFailureException.class)
    public void updateStepSuggestion_WrongVersion_OptimisticLockingFailureException() {
        when(stepSuggestionDAO.getOne(ID_1)).thenReturn(expectedStepSuggestion);
        when(project.hasStepSuggestion(expectedStepSuggestion)).thenReturn(true);

        StepSuggestionUpdateDTO updateDTO = new StepSuggestionUpdateDTO();
        updateDTO.setContent(CONTENT_2);
        updateDTO.setVersion(100L);

        stepSuggestionService.updateStepSuggestion(PROJECT_ID, ID_1, updateDTO);
    }

    @Test
    public void removeTestSuggestion_Success() {
        when(stepSuggestionDAO.getOne(ID_1)).thenReturn(expectedStepSuggestion);
        when(project.hasStepSuggestion(expectedStepSuggestion)).thenReturn(true);

        stepSuggestionService.removeStepSuggestion(PROJECT_ID, ID_1);
    }

    @Test
    public void findStepsSuggestions_CorrectSearchString_Success() {
        List<StepSuggestion> stepSuggestionsList = Arrays.asList(stepSuggestions.toArray(new StepSuggestion[]{}));
        Page<StepSuggestion> stepSuggestionPage = new PageImpl<>(stepSuggestionsList);

        when(stepSuggestionDAO.findByProjectIdAndContentIgnoreCaseContaining(eq(PROJECT_ID),eq(SEARCH_STRING),
            any(PageRequest.class))).thenReturn(stepSuggestionPage);
        when(stepSuggestionTransformer.toDtoList(stepSuggestionsList)).thenReturn(stepSuggestionDTOS);

        List<StepSuggestionDTO> actualStepsSuggestions = stepSuggestionService
            .findStepsSuggestions(PROJECT_ID, SEARCH_STRING, NUMBER_OF_RETURN_PAGE, PAGE_SIZE);

        assertEquals(stepSuggestionDTOS, actualStepsSuggestions);

        verify(stepSuggestionDAO).findByProjectIdAndContentIgnoreCaseContaining(eq(PROJECT_ID), eq(SEARCH_STRING), any(PageRequest.class));
        verify(stepSuggestionTransformer).toDtoList(stepSuggestionsList);
    }

    @Test(expected = BadRequestException.class)
    public void findStepsSuggestions_IncorrectInputData_BadRequestException() {
        int incorrectNumberOfReturnPage = -1;
        int incorrectPageSize = -1;
        stepSuggestionService.findStepsSuggestions(PROJECT_ID, SEARCH_STRING,
            incorrectNumberOfReturnPage, incorrectPageSize);

        verifyZeroInteractions(stepSuggestionService);
    }

}