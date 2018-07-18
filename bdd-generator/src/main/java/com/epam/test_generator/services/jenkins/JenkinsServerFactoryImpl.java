package com.epam.test_generator.services.jenkins;

import com.epam.test_generator.pojo.JenkinsCredentials;
import com.offbytwo.jenkins.JenkinsServer;
import java.net.URI;
import java.net.URISyntaxException;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Class for connection establishment with jenkins server
 */
@Component
public class JenkinsServerFactoryImpl implements JenkinsServerFactory {

    @Resource
    private Environment environment;

    private JenkinsCredentials defaultCredentials;

    @Override
    public JenkinsServer getJenkinsServer(JenkinsCredentials credentials) {

        try {
            return new JenkinsServer(new URI(credentials.getUrl()), credentials.getLogin(),
                credentials.getPassword());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JenkinsServer getJenkinsServer() {
        return getJenkinsServer(defaultCredentials);
    }

    @Override
    @PostConstruct
    public void updateCredentials() {
        this.defaultCredentials = new JenkinsCredentials(
            environment.getProperty("spring.jenkins.url"),
            environment.getProperty("spring.jenkins.password"),
            environment.getProperty("spring.jenkins.login"));
        //TODO consider where we save credentials
    }
}
