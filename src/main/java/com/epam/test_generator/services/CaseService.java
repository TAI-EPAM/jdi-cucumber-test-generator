package com.epam.test_generator.services;

import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dao.interfaces.TagDAO;
import com.epam.test_generator.dto.CaseDTO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.transformers.CaseTransformer;
import com.epam.test_generator.entities.Suit;

import com.epam.test_generator.transformers.SuitTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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

    public CaseDTO getCase(Long id) {

        return caseTransformer.toDto(caseDAO.getOne(id));
    }

    public Long addCaseToSuit(CaseDTO caseDTO, long suitId) {
        Suit suit = suitDAO.getOne(suitId);
        Case caze = caseTransformer.fromDto(caseDTO);

        suit.getCases().add(caze);
        caze = caseDAO.save(caze);

        return caze.getId();
    }

    public void updateCase(long caseId, CaseDTO caseDTO) {
        Case caze = caseDAO.getOne(caseId);

        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        caseDTO.setUpdateDate(formatter.format(Calendar.getInstance().getTime()));

        caseTransformer.mapDTOToEntity(caseDTO, caze);
        caseDAO.save(caze);
    }

    public void removeCase(long suitId, long caseId) {
        Suit suit = suitDAO.getOne(suitId);
        Case caze = suit.getCaseById(caseId);

        suit.getCases().remove(caze);
        caseDAO.delete(caseId);
    }

    public void removeCases(long suitId, List<Long> caseIds) {
        Suit suit = suitDAO.getOne(suitId);
        suit.getCases()
                .removeIf(caze -> caseIds.stream()
                        .anyMatch(id -> id.equals(caze.getId())));

        caseIds.forEach(caseId -> caseDAO.delete(caseId));
    }

    private void mergeTags(Case caze){
        if(caze.getTags() != null){
            caze.setTags(caze.getTags().stream()
                    .map(tag -> tagDAO.findOne(Example.of(tag))==null ? tag : tagDAO.findOne(Example.of(tag)))
                    .collect(Collectors.toSet()));
        }
    }
}