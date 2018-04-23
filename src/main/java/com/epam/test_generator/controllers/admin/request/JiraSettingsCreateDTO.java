package com.epam.test_generator.controllers.admin.request;

import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class JiraSettingsCreateDTO {

    @NotNull
    @Size(min = 1, max = 255)
    private String uri;

    @NotNull
    @Size(min = 1, max = 255)
    private String login;

    @NotNull
    @Size(min = 1, max = 255)
    private String password;

    public JiraSettingsCreateDTO(String uri, String login, String password) {
        this.uri = uri;
        this.login = login;
        this.password = password;
    }

    public JiraSettingsCreateDTO() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "JiraSettingsCreateDTO{" +
            ", uri='" + uri + '\'' +
            ", login='" + login + '\'' +
            ", password='" + password + '\'' +
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
        JiraSettingsCreateDTO that = (JiraSettingsCreateDTO) o;
        return Objects.equals(uri, that.uri) &&
            Objects.equals(login, that.login) &&
            Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uri, login, password);
    }
}
