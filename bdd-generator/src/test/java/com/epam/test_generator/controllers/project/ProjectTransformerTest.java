package com.epam.test_generator.controllers.project;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.epam.test_generator.controllers.project.request.ProjectCreateDTO;
import com.epam.test_generator.controllers.project.request.ProjectUpdateDTO;
import com.epam.test_generator.controllers.project.response.ProjectDTO;
import com.epam.test_generator.controllers.project.response.ProjectFullDTO;
import com.epam.test_generator.controllers.suit.SuitTransformer;
import com.epam.test_generator.controllers.suit.response.SuitDTO;
import com.epam.test_generator.controllers.user.UserDTOsTransformer;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Suit;
import java.util.Collections;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ProjectTransformerTest {

    @Mock
    private UserDTOsTransformer userTransformer;

    @Mock
    private SuitTransformer suitTransformer;

    @InjectMocks
    private ProjectTransformer projectTransformer;

    private static final Long ID = 1L;
    private static final String NAME = "NAME";
    private static final String DESCRIPTION = "DESCRIPTION";
    private static final String JIRA_KEY = "KEY";

    private Suit suit;
    private SuitDTO suitDTO;
    private Project project;

    @Before
    public void setUp(){
        suit = new Suit();
        suit.setName(NAME);
        suit.setPriority(1);
        suit.setStatus(Status.NOT_DONE);
        suit.setRowNumber(1);
        suit.setDescription(DESCRIPTION);

        suitDTO = new SuitDTO();
        suitDTO.setName(NAME);
        suitDTO.setPriority(1);
        suitDTO.setDisplayedStatusName(Status.NOT_DONE.getStatusName());
        suitDTO.setRowNumber(1);
        suitDTO.setDescription(DESCRIPTION);

        project = new Project();
        project.setId(ID);
        project.setName(NAME);
        project.setDescription(DESCRIPTION);
        project.setJiraKey(JIRA_KEY);
        project.setActive(true);
        project.setUsers(Collections.emptySet());
    }

    @Test
    public void fromDTO_ProjectCreateDTO_Success() {
        Project expectedProject = new Project();
        expectedProject.setName(NAME);
        expectedProject.setDescription(DESCRIPTION);

        ProjectCreateDTO projectCreateDTO = new ProjectCreateDTO();
        projectCreateDTO.setName(NAME);
        projectCreateDTO.setDescription(DESCRIPTION);

        Project resultProject = projectTransformer.fromDto(projectCreateDTO);
        Assert.assertEquals(expectedProject, resultProject);
    }

    @Test
    public void toDTO_Project_Success() {
        ProjectDTO expectedProjectDTO = new ProjectDTO();
        expectedProjectDTO.setId(ID);
        expectedProjectDTO.setName(NAME);
        expectedProjectDTO.setDescription(DESCRIPTION);
        expectedProjectDTO.setJiraKey(JIRA_KEY);
        expectedProjectDTO.setActive(true);
        expectedProjectDTO.setUsers(Collections.emptySet());

        ProjectDTO resultProjectDTO = projectTransformer.toDto(project);
        Assert.assertEquals(expectedProjectDTO, resultProjectDTO);
    }

    @Test
    public void toFullDTO_Project_Success() {
        when(suitTransformer.toDto(any(Suit.class))).thenReturn(suitDTO);
        project.setSuits(Collections.singletonList(suit));

        ProjectFullDTO expectedProjectDTO = new ProjectFullDTO();
        expectedProjectDTO.setId(ID);
        expectedProjectDTO.setName(NAME);
        expectedProjectDTO.setDescription(DESCRIPTION);
        expectedProjectDTO.setJiraKey(JIRA_KEY);
        expectedProjectDTO.setActive(true);
        expectedProjectDTO.setUsers(Collections.emptySet());
        expectedProjectDTO.setSuits(Collections.singletonList(suitDTO));

        ProjectFullDTO resultProjectDTO = projectTransformer.toFullDto(project);
        Assert.assertEquals(expectedProjectDTO, resultProjectDTO);
    }

    @Test
    public void updateFromDto_ProjectUpdateDTO_Project_Success() {

        Project expectedProject = project;

        Project updateProject = new Project();
        updateProject.setId(ID);
        updateProject.setJiraKey(JIRA_KEY);
        updateProject.activate();

        ProjectUpdateDTO projectDTO = new ProjectUpdateDTO();
        projectDTO.setName(NAME);
        projectDTO.setDescription(DESCRIPTION);

        Project resultProject = projectTransformer.updateFromDto(projectDTO, updateProject);
        Assert.assertEquals(expectedProject, resultProject);
    }

}
