package com.epam.test_generator.services.jenkins;

import com.offbytwo.jenkins.JenkinsServer;
import java.net.URI;
import java.net.URISyntaxException;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

/**
 * Class for connection establishment with jenkins server
 */
@Component
public class JenkinsServerFactoryImpl implements JenkinsServerFactory {

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
        this.defaultCredentials = new JenkinsCredentials("http://ecse00100b3d.epam.com:8888", "admin", "admin");
        //TODO consider where we save credentials
    }
}
