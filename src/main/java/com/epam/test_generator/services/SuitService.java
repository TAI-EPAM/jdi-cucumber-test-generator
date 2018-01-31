package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;
import static com.epam.test_generator.services.utils.UtilsService.suitBelongsToProject;

import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.transformers.SuitTransformer;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class SuitService {

    @Autowired
    private SuitDAO suitDAO;

    @Autowired
    private SuitTransformer suitTransformer;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private CaseVersionDAO caseVersionDAO;

    public List<SuitDTO> getSuits() {
        return suitTransformer.toDtoList(suitDAO.findAll());
    }

    public Suit getSuit(long projectId, long suitId) {
        Project project = projectService.getProjectByProjectId(projectId);
        checkNotNull(project);

        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);

        suitBelongsToProject(project, suit);

        return suit;
    }

    public SuitDTO getSuitDTO(long projectId, long suitId){
        return suitTransformer.toDto(getSuit(projectId, suitId));
    }

    public Long addSuit(Long projectId, SuitDTO suitDTO) {
        Project project = projectService.getProjectByProjectId(projectId);
        checkNotNull(project);

        Suit suit = suitDAO.save(suitTransformer.fromDto(suitDTO));

        project.getSuits().add(suit);

        caseVersionDAO.save(suit.getCases());

        return suit.getId();
    }

    public void updateSuit(long projectId, long suitId, SuitDTO suitDTO) {
        Suit suit = getSuit(projectId, suitId);
        suitTransformer.mapDTOToEntity(suitDTO, suit);
        suitDAO.save(suit);
    }

    public void removeSuit(long projectId, long suitId) {
        Suit suit = getSuit(projectId, suitId);
        checkNotNull(suit);
        suitDAO.delete(suitId);

        caseVersionDAO.delete(suit.getCases());

        Project project = projectService.getProjectByProjectId(projectId);
        project.getSuits().remove(suit);
    }

    public List<SuitDTO> getSuitsFromProject(Long projectId){
        Project project = projectService.getProjectByProjectId(projectId);
        checkNotNull(project);

        return suitTransformer.toDtoList(project.getSuits());
    }
}