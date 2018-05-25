package com.epam.test_generator.entities;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import org.codehaus.jackson.annotate.JsonIgnore;

@Entity
public class JiraSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uri;

    @Column(unique = true)
    private String login;
    @JsonIgnore
    private String password;

    public JiraSettings() {
    }

    public JiraSettings(String uri, String login, String password) {
        this.uri = uri;
        this.login = login;
        this.password = password;
    }

    public Long getId() {
        return id;
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
        return "JiraSettings{" +
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
        JiraSettings that = (JiraSettings) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(uri, that.uri) &&
            Objects.equals(login, that.login) &&
            Objects.equals(password, that.password);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, uri, login, password);
    }
}
