package com.epam.test_generator.services.jenkins;

import com.offbytwo.jenkins.JenkinsServer;

public interface JenkinsServerFactory {

    JenkinsServer getJenkinsServer(JenkinsCredentials credentials);

    JenkinsServer getJenkinsServer();

    void updateCredentials();

    public static class JenkinsCredentials {

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
}
