package com.epam.test_generator.controllers.admin;

import com.epam.test_generator.controllers.admin.request.JiraSettingsCreateDTO;
import com.epam.test_generator.controllers.admin.request.JiraSettingsUpdateDTO;
import com.epam.test_generator.controllers.admin.response.JiraSettingsDTO;
import com.epam.test_generator.entities.JiraSettings;
import org.springframework.stereotype.Component;

@Component
public class JiraSettingsTransformer {

    public JiraSettingsDTO toDto(JiraSettings settings) {
        return new JiraSettingsDTO(
            settings.getId(),
            settings.getUri(),
            settings.getLogin()
        );
    }

    public JiraSettings fromDto(JiraSettingsCreateDTO dto) {
        return new JiraSettings(
            dto.getUri(),
            dto.getLogin(),
            dto.getPassword()
        );
    }

    public void updateFromDto(JiraSettingsUpdateDTO updateDTO, JiraSettings jiraSettings) {
        if (updateDTO.getUri() != null) {
            jiraSettings.setUri(updateDTO.getUri());
        }
        if (updateDTO.getLogin() != null) {
            jiraSettings.setLogin(updateDTO.getLogin());
        }
        if (updateDTO.getPassword() != null) {
            jiraSettings.setPassword(updateDTO.getPassword());
        }
    }
}
