package com.epam.test_generator.controllers.admin.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class JiraSettingsUpdateDTO {

    @NotNull
    @Size(min = 1, max = 255)
    private String uri;

    @NotNull
    @Size(min = 1, max = 255)
    private String login;

    @NotNull
    @Size(min = 1, max = 255)
    private String password;

    public JiraSettingsUpdateDTO(String uri, String login, String password) {
        this.uri = uri;
        this.login = login;
        this.password = password;
    }

    public JiraSettingsUpdateDTO() {
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

        JiraSettingsUpdateDTO that = (JiraSettingsUpdateDTO) o;

        if (uri != null ? !uri.equals(that.uri) : that.uri != null) {
            return false;
        }
        if (login != null ? !login.equals(that.login) : that.login != null) {
            return false;
        }
        return password != null ? password.equals(that.password) : that.password == null;
    }

    @Override
    public int hashCode() {
        int result = uri != null ? uri.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
