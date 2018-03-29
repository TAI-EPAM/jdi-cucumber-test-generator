package com.epam.test_generator.services;

import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;

import com.epam.test_generator.dao.interfaces.JiraSettingsDAO;
import com.epam.test_generator.dto.JiraSettingsDTO;
import com.epam.test_generator.entities.JiraSettings;
import com.epam.test_generator.services.exceptions.JiraAuthenticationException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class JiraSettingsService {

    @Autowired
    JiraSettingsDAO jiraSettingsDAO;

    public JiraSettings createJiraSettings(JiraSettingsDTO jiraSettingsDTO) throws JiraAuthenticationException {
        if (jiraSettingsDAO.findByLogin(jiraSettingsDTO.getLogin()) != null) {
            throw new JiraAuthenticationException(
                "Jira setting with such login:" + jiraSettingsDTO.getLogin() + " already exist!");
        } else {
            final JiraSettings jiraSettings = new JiraSettings(
                jiraSettingsDTO.getUri(),
                jiraSettingsDTO.getLogin(),
                jiraSettingsDTO.getPassword());
            jiraSettingsDAO.save(jiraSettings);
            return jiraSettings;
        }
    }

    public void updateJiraSettings(Long id, JiraSettingsDTO jiraSettingsDTO) {
        JiraSettings jiraSettings = jiraSettingsDAO.findById(id);
        checkNotNull(jiraSettings);
        jiraSettings.setLogin(jiraSettingsDTO.getLogin());
        jiraSettings.setPassword( jiraSettingsDTO.getPassword());
        jiraSettings.setUri(jiraSettingsDTO.getUri());
        jiraSettingsDAO.save(jiraSettings);
    }

    public List<JiraSettings> getJiraSettings() {
        return jiraSettingsDAO.findAll();
    }

}
