package com.epam.test_generator.services.jenkins;

import com.epam.test_generator.pojo.JenkinsCredentials;
import com.offbytwo.jenkins.JenkinsServer;

public interface JenkinsServerFactory {

    JenkinsServer getJenkinsServer(JenkinsCredentials credentials);

    JenkinsServer getJenkinsServer();

    void updateCredentials();

}
