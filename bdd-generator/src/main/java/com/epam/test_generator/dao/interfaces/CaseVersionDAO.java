package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.entities.Case;
import com.epam.test_generator.pojo.CaseVersion;
import java.util.List;

public interface CaseVersionDAO {

    /**
     * Find all CaseVersions of Case with caseId.
     */
    List<CaseVersion> findAll(Long caseId);

    /**
     * Find {@link Case} by commitId.
     */
    Case findByCommitId(Long caseId, String commitId);

    /**
     * Save new {@link CaseVersion} of current {@link Case}.
     */
    void save(Case caze);

    /**
     * Save new {@link CaseVersion} of each {@link Case}
     */
    void save(Iterable<Case> cases);

    /**
     * Save new {@link CaseVersion} of deleted {@link Case}
     */
    void delete(Case caze);

    /**
     * Save new {@link CaseVersion} of each deleted {@link Case}
     */
    void delete(Iterable<Case> cases);
}
