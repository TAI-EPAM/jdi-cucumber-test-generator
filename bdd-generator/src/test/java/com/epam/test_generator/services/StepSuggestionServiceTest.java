package com.epam.test_generator.services;

import static com.epam.test_generator.entities.StepType.GIVEN;
import static com.epam.test_generator.entities.StepType.THEN;
import static com.epam.test_generator.entities.StepType.WHEN;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.epam.test_generator.controllers.stepsuggestion.StepSuggestionTransformer;
import com.epam.test_generator.dao.interfaces.ProjectDAO;
import com.epam.test_generator.dao.interfaces.ProjectStepSuggestionDAO;
import com.epam.test_generator.controllers.stepsuggestion.request.StepSuggestionCreateDTO;
import com.epam.test_generator.controllers.stepsuggestion.response.StepSuggestionDTO;
import com.epam.test_generator.controllers.stepsuggestion.request.StepSuggestionUpdateDTO;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.StepSuggestion;
import com.epam.test_generator.services.exceptions.BadRequestException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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

    @Mock
    private Project project;

    @Mock
    private ProjectDAO projectDAO;

    @Mock
    private ProjectStepSuggestionDAO projectStepSuggestionDAO;

    @Mock
    private StepSuggestionTransformer stepSuggestionTransformer;

    @InjectMocks
    private StepSuggestionService stepSuggestionService;

    private List<StepSuggestionDTO> stepSuggestionDTOS = new ArrayList<>();
    private Set<StepSuggestion> stepSuggestions = new HashSet<>();
    private StepSuggestion expectedStepSuggestion;
    private StepSuggestionDTO expectedStepSuggestionDTO;

    @Before
    public void setUp() throws Exception {
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
    public void getStepsSuggestions_Success() throws Exception {
        when(project.getStepSuggestions()).thenReturn(stepSuggestions);
        when(stepSuggestionTransformer.toDtoList(any())).thenReturn(stepSuggestionDTOS);

        List<StepSuggestionDTO> actualStepSuggestions =
            stepSuggestionService.getStepsSuggestions(PROJECT_ID);

        assertEquals(stepSuggestionDTOS, actualStepSuggestions);
    }

    @Test
    public void getStepSuggestionDTO_Success() throws Exception {
        when(projectStepSuggestionDAO.getOne(ID_1)).thenReturn(expectedStepSuggestion);
        when(project.hasStepSuggestion(any())).thenReturn(true);
        when(stepSuggestionTransformer.toDto(any())).thenReturn(expectedStepSuggestionDTO);

        StepSuggestionDTO actualDto =
            stepSuggestionService.getStepSuggestionDTO(PROJECT_ID, ID_1);

        assertEquals(expectedStepSuggestionDTO, actualDto);
    }

    @Test(expected = BadRequestException.class)
    public void getStepSuggestionDTO_NotFound() throws Exception {
        when(projectStepSuggestionDAO.getOne(ID_1)).thenReturn(expectedStepSuggestion);
        when(project.hasStepSuggestion(expectedStepSuggestion)).thenReturn(false);

        stepSuggestionService.getStepSuggestionDTO(PROJECT_ID, ID_1);
    }

    @Test
    public void getStepSuggestionsByType_Success() throws Exception {
        when(project.getStepSuggestions()).thenReturn(stepSuggestions);
        when(stepSuggestionTransformer.toDto(any())).thenCallRealMethod();

        List<StepSuggestionDTO> expectedDTOs = stepSuggestionDTOS.stream()
            .filter(s -> s.getType().equals(GIVEN))
            .collect(Collectors.toList());
        List<StepSuggestionDTO> actualDTOs =
            stepSuggestionService.getStepsSuggestionsByType(PROJECT_ID, GIVEN);

        assertEquals(expectedDTOs, actualDTOs);
    }

    @Test
    public void addStepSuggestion_Success() {
        StepSuggestionCreateDTO createDTO =
            new StepSuggestionCreateDTO(CONTENT_1, GIVEN);

        when(stepSuggestionTransformer.fromDto(createDTO)).thenReturn(expectedStepSuggestion);
        when(projectStepSuggestionDAO.save(any(StepSuggestion.class)))
            .thenReturn(expectedStepSuggestion);

        Long id = stepSuggestionService.addStepSuggestion(PROJECT_ID, createDTO);

        assertEquals(ID_1, id);
        verify(project).addStepSuggestion(expectedStepSuggestion);
    }

    @Test
    public void updateStepSuggestion_Success() throws Exception {
        when(projectStepSuggestionDAO.getOne(ID_1)).thenReturn(expectedStepSuggestion);
        when(project.hasStepSuggestion(expectedStepSuggestion)).thenReturn(true);
        when(projectStepSuggestionDAO.save(any(StepSuggestion.class)))
            .thenAnswer(a -> a.getArgument(0));
        when(stepSuggestionTransformer.toDto(any())).thenCallRealMethod();
        doCallRealMethod().when(stepSuggestionTransformer).updateFromDto(any(), any());

        StepSuggestionUpdateDTO updateDTO = new StepSuggestionUpdateDTO();
        updateDTO.setContent(CONTENT_2);
        updateDTO.setVersion(0L);

        StepSuggestionDTO expectedDto = new StepSuggestionDTO(ID_1, CONTENT_2, GIVEN);

        StepSuggestionDTO actualDto =
            stepSuggestionService.updateStepSuggestion(PROJECT_ID, ID_1, updateDTO);

        assertEquals(expectedDto, actualDto);
    }

    @Test
    public void removeTestSuggestion_Success() throws Exception {
        when(projectStepSuggestionDAO.getOne(ID_1)).thenReturn(expectedStepSuggestion);
        when(project.hasStepSuggestion(expectedStepSuggestion)).thenReturn(true);

        stepSuggestionService.removeStepSuggestion(PROJECT_ID, ID_1);
    }

}