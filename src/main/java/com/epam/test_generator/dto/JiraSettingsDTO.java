package com.epam.test_generator.dto;

import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.Email;

public class JiraSettingsDTO {

    private Long id;

    @NotNull
    @Size(min = 1, max = 255)
    private String uri;

    @NotNull
    @Size(min = 1, max = 255)
    private String login;

    @NotNull
    @Size(min = 1, max = 255)
    private String password;

    public JiraSettingsDTO(String uri, String login, String password) {
        this.uri = uri;
        this.login = login;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "JiraSettingsDTO{" +
            "id=" + id +
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
        JiraSettingsDTO that = (JiraSettingsDTO) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(uri, that.uri) &&
            Objects.equals(login, that.login) &&
            Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (uri != null ? uri.hashCode() : 0);
        return result;
    }
}
