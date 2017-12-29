package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;

import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dto.SuitDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.file_generator.FileGenerator;
import com.epam.test_generator.transformers.CaseTransformer;
import com.epam.test_generator.transformers.SuitTransformer;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class SuitService {

    @Autowired
    private FileGenerator fileGenerator;

    @Autowired
    private SuitDAO suitDAO;

    @Autowired
    private CaseDAO caseDAO;

    @Autowired
    private SuitTransformer suitTransformer;

    @Autowired
    private CaseTransformer caseTransformer;

    @Autowired
    private CaseVersionDAO caseVersionDAO;

    public List<SuitDTO> getSuits() {
        return suitTransformer.toDtoList(suitDAO.findAll());
    }

    public SuitDTO getSuit(long suitId) {
        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);

        return suitTransformer.toDto(suit);
    }

    public Long addSuit(SuitDTO suitDTO) {
        Suit suit = suitDAO.save(suitTransformer.fromDto(suitDTO));

        caseVersionDAO.save(suit.getCases());
        return suit.getId();
    }

    public void updateSuit(long suitId, SuitDTO suitDTO) {
        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);
        suitTransformer.mapDTOToEntity(suitDTO, suit);
        suitDAO.save(suit);
    }

    public void removeSuit(long suitId) {
        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);
        suitDAO.delete(suitId);

        caseVersionDAO.delete(suit.getCases());
    }

    public String generateFile(Long suitId, List<Long> caseIds) throws IOException {
        Suit suit = suitDAO.findOne(suitId);
        List<Case> cases = caseIds.stream().map(id -> caseDAO.findOne(id))
            .collect(Collectors.toList());

        return fileGenerator
            .generate(suitTransformer.toDto(suit), caseTransformer.toDtoList(cases));
    }
}