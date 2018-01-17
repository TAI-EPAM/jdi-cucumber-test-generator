package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;

import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dto.SuitDTO;
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
    private CaseVersionDAO caseVersionDAO;

    public List<SuitDTO> getSuits() {
        return suitTransformer.toDtoList(suitDAO.findAll());
    }

    public Suit getSuit(long suitId) {
        Suit suit = suitDAO.findOne(suitId);
        checkNotNull(suit);

        return suit;
    }

    public SuitDTO getSuitDTO(long suitID){
        return suitTransformer.toDto(getSuit(suitID));
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
}