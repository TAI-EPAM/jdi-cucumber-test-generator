package com.epam.test_generator.pojo;

public class JenkinsCredentials {

    private final String url;
    private final String password;
    private final String login;

    public JenkinsCredentials(String url, String password, String login) {
        this.url = url;
        this.password = password;
        this.login = login;
    }

    public String getUrl() {
        return url;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }
}
