package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;
import static com.epam.test_generator.services.utils.UtilsService.suitBelongsToProject;

import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dto.StepDTO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.dto.SuitRowNumberUpdateDTO;
import com.epam.test_generator.dto.SuitUpdateDTO;
import com.epam.test_generator.entities.Project;
import com.epam.test_generator.entities.Status;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.services.exceptions.BadRequestException;
import com.epam.test_generator.transformers.SuitTransformer;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;

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

    @Autowired
    private CascadeUpdateService cascadeUpdateService;

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

    public SuitDTO getSuitDTO(long projectId, long suitId) {
        return suitTransformer.toDto(getSuit(projectId, suitId));
    }

    /**
     * Adds suit specified in suitDTO to project by id
     * @param projectId if of project where to add case
     * @param suitDTO suit info
     * @return {@link SuitDTO} of added suit
     */
    public SuitDTO addSuit(Long projectId, SuitDTO suitDTO) {
        Project project = projectService.getProjectByProjectId(projectId);
        checkNotNull(project);

        Suit suit = suitDAO.save(suitTransformer.fromDto(suitDTO));

        project.getSuits().add(suit);

        caseVersionDAO.save(suit.getCases());

        SuitDTO addedSuitDTO = suitTransformer.toDto(suit);

        return addedSuitDTO;
    }

    /**
     * Updates suit by id with info specified in suitDTO
     * @param projectId id of project
     * @param suitId id of suit to update
     * @param suitDTO info to update
     * @return {@link SuitUpdateDTO} which contains {@link SuitDTO} and {@link List<Long>}
     * (in fact id of {@link StepDTO} with FAILED {@link Status} which belong this suit)
     */
    public SuitUpdateDTO updateSuit(long projectId, long suitId, SuitDTO suitDTO) throws MethodArgumentNotValidException {
        final List<Long> failedStepIds = cascadeUpdateService.cascadeSuitCasesUpdate(projectId, suitId, suitDTO);
        Suit suit = getSuit(projectId, suitId);
        final SuitDTO simpleSuitDTO = getSimpleSuitDTO(suitDTO);

        suitTransformer.mapDTOToEntity(simpleSuitDTO, suit);
        suit = suitDAO.save(suit);

        SuitDTO updatedSuitDTO = suitTransformer.toDto(suit);
        SuitUpdateDTO updatedSuitDTOwithFailedStepIds = new SuitUpdateDTO(updatedSuitDTO, failedStepIds);

        return updatedSuitDTOwithFailedStepIds;
    }

    /**
     * Removes suit from project by id
     * @param projectId id of project where to delete case
     * @param suitId id of case to delete
     * @return {@link SuitDTO) of removed suit
     */
    public SuitDTO removeSuit(long projectId, long suitId) {
        Suit suit = getSuit(projectId, suitId);
        checkNotNull(suit);
        suitDAO.delete(suitId);

        caseVersionDAO.delete(suit.getCases());

        Project project = projectService.getProjectByProjectId(projectId);
        project.getSuits().remove(suit);
        SuitDTO removedSuitDTO = suitTransformer.toDto(suit);
        return removedSuitDTO;
    }

    public List<SuitDTO> getSuitsFromProject(Long projectId) {
        Project project = projectService.getProjectByProjectId(projectId);
        checkNotNull(project);

        return suitTransformer.toDtoList(project.getSuits());
    }

    /**
     * Updates suit's rowNumbers by suit's ids specified in List of SuitRowNumberUpdateDTOs
     * @param rowNumberUpdates List of SuitRowNumberUpdateDTOs
     * @return list of {@link SuitRowNumberUpdateDTO} to check on the frontend
     */
    public List<SuitRowNumberUpdateDTO> updateSuitRowNumber(
        long projectId, List<SuitRowNumberUpdateDTO> rowNumberUpdates) {
        if (rowNumberUpdates.isEmpty()) {
            throw new BadRequestException("The list has not to be empty");
        }

        for (SuitRowNumberUpdateDTO update : rowNumberUpdates) {
            if (Objects.isNull(update.getId()) || Objects.isNull(update.getRowNumber())) {
                throw new BadRequestException("Id or rowNumber has not to be null");
            }
        }

        final Map<Long, Integer> patch = rowNumberUpdates
            .stream()
            .collect(Collectors
                .toMap(SuitRowNumberUpdateDTO::getId, SuitRowNumberUpdateDTO::getRowNumber));

        final List<Integer> distinct = patch.values().stream().distinct()
            .collect(Collectors.toList());

        if (rowNumberUpdates.size() != distinct.size()) {
            throw new BadRequestException("One or more of the rowNumbers is a duplicate");
        }


        final Project currentProject = projectService.getProjectByProjectId(projectId);
        final List<Suit> suits = retrieveSuitsFromProjectBySuitIds(currentProject, patch.keySet());


        if (suits.size() != patch.size()) {
            throw new BadRequestException(
                "One or more of the ids is a duplicate or it does not exist in the database");
        }
        for (Suit suit : suits) {
            suit.setRowNumber(patch.get(suit.getId()));
        }

        suitDAO.save(suits);

        return rowNumberUpdates;
    }

    private List<Suit> retrieveSuitsFromProjectBySuitIds(Project currentProject,
                                                         Set<Long> suitsIds) {
        final List<Suit> neededSuits = currentProject.getSuits().stream()
            .filter(s -> suitsIds.contains(s.getId()))
            .collect(Collectors.toList());
        if (neededSuits.size() != suitsIds.size()) {
            throw new BadRequestException("Some Id of suit doesn't belong to current Project");
        }
        return neededSuits;
    }

    private SuitDTO getSimpleSuitDTO(SuitDTO suitDTO) {
        final SuitDTO snapShot = new SuitDTO();
        snapShot.setId(suitDTO.getId());
        snapShot.setName(suitDTO.getName());
        snapShot.setDescription(suitDTO.getDescription());
        snapShot.setPriority(suitDTO.getPriority());
        snapShot.setTags(suitDTO.getTags());
        snapShot.setCreationDate(suitDTO.getCreationDate());
        snapShot.setRowNumber(suitDTO.getRowNumber());
        return snapShot;
    }
}