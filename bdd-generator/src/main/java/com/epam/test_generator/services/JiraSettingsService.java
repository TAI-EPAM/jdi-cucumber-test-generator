package com.epam.test_generator.services;

import com.epam.test_generator.controllers.admin.JiraSettingsTransformer;
import com.epam.test_generator.controllers.admin.request.JiraSettingsCreateDTO;
import com.epam.test_generator.controllers.admin.request.JiraSettingsUpdateDTO;
import com.epam.test_generator.controllers.admin.response.JiraSettingsDTO;
import com.epam.test_generator.dao.interfaces.JiraSettingsDAO;
import com.epam.test_generator.entities.JiraSettings;
import com.epam.test_generator.services.exceptions.JiraAuthenticationException;
import com.epam.test_generator.services.exceptions.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class JiraSettingsService {

    @Autowired
    private JiraSettingsDAO jiraSettingsDAO;

    @Autowired
    private JiraSettingsTransformer jiraSettingsTransformer;

    public JiraSettings createJiraSettings(JiraSettingsCreateDTO jiraSettingsCreateDTO) throws JiraAuthenticationException {
        if (jiraSettingsDAO.findByLogin(jiraSettingsCreateDTO.getLogin()) != null) {
            throw new JiraAuthenticationException(
                    "Jira setting with such login:" + jiraSettingsCreateDTO.getLogin() + " already exist!");
        } else {
            JiraSettings jiraSettings = jiraSettingsTransformer.fromDto(jiraSettingsCreateDTO);
            jiraSettingsDAO.save(jiraSettings);
            return jiraSettings;
        }
    }

    public void updateJiraSettings(Long id, JiraSettingsUpdateDTO jiraSettingsUpdateDTO) {
        JiraSettings jiraSettings = jiraSettingsDAO.findById(id)
            .orElseThrow(NotFoundException::new);
        jiraSettingsTransformer.updateFromDto(jiraSettingsUpdateDTO, jiraSettings);
        jiraSettingsDAO.save(jiraSettings);
    }

    public List<JiraSettingsDTO> getJiraSettings() {
        return jiraSettingsDAO.findAll()
            .stream()
            .map(jiraSettingsTransformer::toDto)
            .collect(Collectors.toList());
    }

}
