package com.epam.test_generator.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

	public TagDTO save(TagDTO tagDTO){
		Tag tag = tagDAO.save(tagTransformer.fromDto(tagDTO));

		return tagTransformer.toDto(tag);
	}

	public TagDTO getTag(long tagId) {

		return tagTransformer.toDto(tagDAO.getOne(tagId));
	}

	public Set<TagDTO> getAllTagsFromAllCasesInSuit(long suitId) {
		Set<TagDTO> allTagsFromAllCases = new HashSet<>();
		Suit suit = suitDAO.getOne(suitId);

		suit.getCases().forEach(caze -> caze.getTags()
				.forEach(tag -> allTagsFromAllCases.add(tagTransformer.toDto(tag))));

		return allTagsFromAllCases;
	}

	public Long addTagToCase(TagDTO tagDTO, long caseId) {
		Case caze = caseDAO.getOne(caseId);
		Tag tag = tagTransformer.fromDto(tagDTO);

		caze.getTags().add(tag);
		tag = tagDAO.save(tag);

		return tag.getId();
	}

	public void updateTag(long caseId, long tagId, TagDTO tagDTO) {
		Case caze = caseDAO.getOne(caseId);
		Tag tag = tagDAO.getOne(tagId);

		//TODO create update logic

	}

	public void removeTag(long caseId, long tagId) {
		Case caze = caseDAO.getOne(caseId);
		Tag tag = tagDAO.getOne(tagId);

		caze.getTags().remove(tag);
		caseDAO.save(caze);
	}
}