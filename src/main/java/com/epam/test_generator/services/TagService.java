package com.epam.test_generator.services;

import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import java.util.HashSet;
import java.util.Set;

import com.epam.test_generator.dao.interfaces.CaseDAO;
import com.epam.test_generator.dao.interfaces.StepDAO;
import com.epam.test_generator.dao.interfaces.SuitDAO;
import com.epam.test_generator.dao.interfaces.TagDAO;
import com.epam.test_generator.dto.TagDTO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.entities.Tag;
import com.epam.test_generator.transformers.SuitTransformer;
import com.epam.test_generator.transformers.TagTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.epam.test_generator.services.utils.UtilsService.*;

@Transactional
@Service
public class TagService {

	@Autowired
	private SuitDAO suitDAO;

	@Autowired
	private CaseDAO caseDAO;

	@Autowired
	private StepDAO stepDAO;

	@Autowired
	private TagDAO tagDAO;

	@Autowired
	private SuitTransformer suitTransformer;

	@Autowired
	private TagTransformer tagTransformer;

	@Autowired
	private CaseVersionDAO caseVersionDAO;

	public Set<TagDTO> getAllTagsFromAllCasesInSuit(Long suitId) {
		Suit suit = suitDAO.findOne(suitId);
		checkNotNull(suit);

		Set<TagDTO> allTagsFromAllCases = new HashSet<>();
		suit.getCases().forEach(caze -> caze.getTags()
				.forEach(tag -> allTagsFromAllCases.add(tagTransformer.toDto(tag))));

		return allTagsFromAllCases;
	}

	public Long addTagToCase(Long suitId, Long caseId, TagDTO tagDTO) {
		Suit suit = suitDAO.findOne(suitId);
		checkNotNull(suit);

		Case caze = caseDAO.findOne(caseId);
		checkNotNull(caze);

		caseBelongsToSuit(caze,suit);
		Tag tag = tagTransformer.fromDto(tagDTO);

		tag = tagDAO.save(tag);
		caze.getTags().add(tag);

		caseVersionDAO.save(caze);

		return tag.getId();
	}

	public void updateTag(Long suitId, Long caseId, Long tagId, TagDTO tagDTO) {
		Suit suit = suitDAO.findOne(suitId);
		checkNotNull(suit);

		Case caze = caseDAO.findOne(caseId);
		checkNotNull(caze);

		caseBelongsToSuit(caze, suit);

		Tag tag = tagDAO.findOne(tagId);
		checkNotNull(tag);

		tagBelongsToCase(tag, caze);

		tagTransformer.mapDTOToEntity(tagDTO, tag);

		tagDAO.save(tag);

		caseVersionDAO.save(caze);
	}

	public void removeTag(Long suitId, Long caseId, Long tagId) {
		Suit suit = suitDAO.findOne(suitId);
		checkNotNull(suit);

		Case caze = caseDAO.findOne(caseId);
		checkNotNull(caze);

		caseBelongsToSuit(caze, suit);

		Tag tag = tagDAO.findOne(tagId);
		checkNotNull(tag);

		tagBelongsToCase(tag, caze);

		caze.getTags().remove(tag);

		tagDAO.delete(tagId);

		caseVersionDAO.save(caze);
	}
}