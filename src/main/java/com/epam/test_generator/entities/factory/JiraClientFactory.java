package com.epam.test_generator.entities.factory;

import static com.epam.test_generator.services.utils.UtilsService.checkNotNull;

import com.epam.test_generator.dao.interfaces.JiraSettingsDAO;
import com.epam.test_generator.entities.JiraSettings;
import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.JiraClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JiraClientFactory {

    @Autowired
    private JiraSettingsDAO jiraSettingsDAO;

    public JiraClient getJiraClient(Long id) {
        JiraSettings jiraSettings = jiraSettingsDAO.findById(id);
        checkNotNull(jiraSettings);
        BasicCredentials creds = new BasicCredentials(jiraSettings.getLogin(),
            jiraSettings.getPassword());

        return new JiraClient(jiraSettings.getUri(), creds);
    }

}
