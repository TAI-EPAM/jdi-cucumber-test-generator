package com.epam.test_generator.controllers.admin.response;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

public class JiraSettingsDTO {

    @NotNull
    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    private String uri;

    @NotNull
    @Size(min = 1, max = 255)
    private String login;

    public JiraSettingsDTO(Long id, String uri, String login) {
        this.id = id;
        this.uri = uri;
        this.login = login;
    }

    public JiraSettingsDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String toString() {
        return "JiraSettingsCreateDTO{" +
            "id=" + id +
            ", uri='" + uri + '\'' +
            ", login='" + login + '\'' +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JiraSettingsDTO that = (JiraSettingsDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(uri, that.uri) &&
            Objects.equals(login, that.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, uri, login);
    }
}
