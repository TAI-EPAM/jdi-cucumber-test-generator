package com.epam.test_generator.dao.interfaces;

import com.epam.test_generator.entities.JiraSettings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JiraSettingsDAO extends JpaRepository<JiraSettings, Long> {
    JiraSettings findByLogin(String login);
}
