package com.epam.test_generator.services;

import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dao.interfaces.TagDAO;
import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.transformers.CaseTransformer;
import com.epam.test_generator.entities.Suit;

import com.epam.test_generator.transformers.SuitTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static com.epam.test_generator.services.utils.UtilsService.*;

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

    public void updateCase(Long suitId, Long caseId, CaseDTO caseDTO) {
        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);

        Case caze = caseDAO.findOne(caseId);
        checkNotNull(caze);

        caseBelongsToSuit(caze, suit);

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        caseDTO.setUpdateDate(formatter.format(Calendar.getInstance().getTime()));

        caseTransformer.mapDTOToEntity(caseDTO, caze);
        caseDAO.save(caze);
    }

    public void removeCase(Long suitId, Long caseId) {
        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);

        Case caze = suit.getCaseById(caseId);
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