package com.epam.test_generator.dao.impl;

import com.epam.test_generator.dao.interfaces.CaseVersionDAO;
import com.epam.test_generator.entities.Case;
import com.epam.test_generator.pojo.CaseVersion;
import java.util.ArrayList;
import java.util.List;
import org.javers.core.Javers;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.javers.spring.auditable.AuthorProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class CaseVersionDAOImpl implements CaseVersionDAO {

    @Autowired
    private Javers javers;

    @Autowired
    private AuthorProvider authorProvider;

    @Override
    public List<CaseVersion> find(Long caseId) {
        List<Shadow<Case>> caseHistory = javers
            .findShadows(QueryBuilder.byInstanceId(caseId, Case.class).build());
        List<CaseVersion> caseVersions = new ArrayList<>(caseHistory.size());

        for (int i = 0; i < caseHistory.size(); ++i) {
            Shadow<Case> caseShadow = caseHistory.get(i);
            caseVersions.add(new CaseVersion(caseShadow.get(), (long) (caseHistory.size() - i)));
        }

        return caseVersions;
    }

    @Override
    public void save(Case caze) {
        javers.commit(authorProvider.provide(), caze);
    }

    @Override
    public void save(Iterable<Case> cases) {
        for (Case caze : cases) {
            save(caze);
        }
    }

    @Override
    public void delete(Case caze) {
        javers.commitShallowDelete(authorProvider.provide(), caze);
    }

    @Override
    public void delete(Iterable<Case> cases) {
        for (Case caze : cases) {
            delete(caze);
        }

    }
}
