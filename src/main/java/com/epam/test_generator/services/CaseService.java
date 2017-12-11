package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.caseBelongsToSuit;
import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;

import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dao.interfaces.TagDAO;
import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.EditCaseDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.transformers.CaseTransformer;
import com.epam.test_generator.transformers.SuitTransformer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class CaseService {

    @Autowired
    private CaseTransformer caseTransformer;

    @Autowired
    private SuitTransformer suitTransformer;

    @Autowired
    private CaseDAO caseDAO;

    @Autowired
    private SuitDAO suitDAO;

    @Autowired
    private TagDAO tagDAO;

    public CaseDTO getCase(Long suitId, Long caseId) {
        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);

        Case caze = caseDAO.findOne(caseId);
        checkNotNull(caze);

        caseBelongsToSuit(caze,suit);

        return caseTransformer.toDto(caze);
    }

    public Long addCaseToSuit(Long suitId, CaseDTO caseDTO) {
        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);

        Case caze = caseTransformer.fromDto(caseDTO);

        caze = caseDAO.save(caze);
        suit.getCases().add(caze);

        return caze.getId();
    }

    public void updateCase(Long suitId, Long caseId, EditCaseDTO editCaseDTO) {
        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);

        Case caze = caseDAO.findOne(caseId);
        checkNotNull(caze);

        caseBelongsToSuit(caze, suit);

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        caze.setUpdateDate(formatter.format(Calendar.getInstance().getTime()));

        if (editCaseDTO.getDescription() != null) {
            caze.setDescription(editCaseDTO.getDescription());
        }

        if (editCaseDTO.getPriority() != null) {
            caze.setPriority(editCaseDTO.getPriority());
        }

        caseDAO.save(caze);
    }

    public void removeCase(Long suitId, Long caseId) {
        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);

        Case caze = caseDAO.findOne(caseId);
        checkNotNull(caze);

        caseBelongsToSuit(caze, suit);

        suit.getCases().remove(caze);
        caseDAO.delete(caseId);
    }

    public void removeCases(Long suitId, List<Long> caseIds) {
        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);

        suit.getCases()
                .removeIf(caze -> caseIds.stream()
                        .anyMatch(id -> id.equals(caze.getId())));

        caseIds.forEach(caseId -> caseDAO.delete(caseId));
    }

}