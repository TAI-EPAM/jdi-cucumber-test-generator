package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.entities.Suit;
import com.epam.test_generator.pojo.SuitVersion;
import java.util.List;

public interface SuitVersionDAO {

    /**
     * Find all SuitVersions of Suit with caseId.
     */
    List<SuitVersion> findAll(Long suitId);

    /**
     * Find {@link Suit} by commitId.
     */
    Suit findByCommitId(Long suitId, String commitId);

    /**
     * Save new {@link SuitVersion} of current {@link Suit}.
     */
    void save(Suit suit);

    /**
     * Save new {@link SuitVersion} of each {@link Suit}
     */
    void save(Iterable<Suit> suits);

    /**
     * Save new {@link SuitVersion} of deleted {@link Suit}
     */
    void delete(Suit suit);

    /**
     * Save new {@link SuitVersion} of each deleted {@link Suit}
     */
    void delete(Iterable<Suit> suits);
}
