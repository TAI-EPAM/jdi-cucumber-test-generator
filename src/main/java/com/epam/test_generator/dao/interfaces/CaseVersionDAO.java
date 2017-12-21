package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.entities.Case;
import com.epam.test_generator.pojo.CaseVersion;
import java.util.List;

public interface CaseVersionDAO {

    /**
     * Find all CaseVersions of Case with caseId.
     * @param caseId
     * @return
     */
    List<CaseVersion> find(Long caseId);

    /**
     * Save {@link CaseVersion} of current {@link Case}.
     * @param caze
     */
    void save(Case caze);

    /**
     * Save {@link CaseVersion} of deleted {@link Case}
     * @param caze
     */
    void delete(Case caze);
}
