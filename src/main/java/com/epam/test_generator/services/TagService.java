package com.epam.test_generator.services;

import java.util.HashSet;
import java.util.Set;

import com.epam.test_generator.dao.interfaces.CaseDAO;
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

@Transactional
@Service
public class TagService {

	@Autowired
	private SuitDAO suitDAO;

	@Autowired
	private CaseDAO caseDAO;

	@Autowired
	private TagDAO tagDAO;

	@Autowired
	private SuitTransformer suitTransformer;

	@Autowired
	private TagTransformer tagTransformer;

	public Set<TagDTO> getAllTagsFromAllCasesInSuit(long suitId) {
		Set<TagDTO> allTagsFromAllCases = new HashSet<>();
		Suit suit = suitDAO.findOne(suitId);

		suit.getCases().forEach(caze -> caze.getTags()
				.forEach(tag -> allTagsFromAllCases.add(tagTransformer.toDto(tag))));

		return allTagsFromAllCases;
	}

	public TagDTO getTag(long tagId) {
		Tag tag = tagDAO.findOne(tagId);
		if (tag == null) {
			return null;
		}
		return tagTransformer.toDto(tag);
	}

//	??????? do we use it
	public Long save(TagDTO tagDTO){
		Tag tag = tagDAO.save(tagTransformer.fromDto(tagDTO));

		return tag.getId();
	}

	public Long addTagToCase(long caseId, TagDTO tagDTO) {
		Case caze = caseDAO.findOne(caseId);
		Tag tag = tagTransformer.fromDto(tagDTO);

		tag = tagDAO.save(tag);
		caze.getTags().add(tag);

		return tag.getId();
	}

	public void updateTag(long tagId, TagDTO tagDTO) {
		Tag tag = tagDAO.findOne(tagId);
		tagTransformer.mapDTOToEntity(tagDTO, tag);

		tagDAO.save(tag);
	}

	public void removeTag(long caseId, long tagId) {
		Case caze = caseDAO.findOne(caseId);
		Tag tag = tagDAO.findOne(tagId);

		caze.getTags().remove(tag);
		tagDAO.delete(tagId);
	}
}