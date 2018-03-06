package com.epam.test_generator.services.jenkins;

import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.when;

import com.epam.test_generator.services.jenkins.JenkinsServerFactory.JenkinsCredentials;
import com.offbytwo.jenkins.JenkinsServer;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class JenkinsServerFactoryImplTest {

    @Mock
    private JenkinsCredentials jenkinsCredentials;

    @InjectMocks
    private JenkinsServerFactoryImpl jenkinsServerFactory;

    @Test
    public void getJenkinsServer_WithCredentials_Success() throws Exception {
        JenkinsCredentials jenkinsCredentials = new JenkinsCredentials("url", "password", "login");
        JenkinsServer jenkinsServer = jenkinsServerFactory.getJenkinsServer(jenkinsCredentials);
        Assert.assertNotNull(jenkinsServer);
    }

    @Test
    public void getJenkinsServer_WithoutCredentials_Success() throws Exception {
        when(jenkinsCredentials.getUrl()).thenReturn("url");
        JenkinsServer jenkinsServer = jenkinsServerFactory.getJenkinsServer();
        Assert.assertNotNull(jenkinsServer);
    }

    @Test(expected = RuntimeException.class)
    public void getJenkinsServer_BadUrl_RuntimeException() throws Exception {
        JenkinsCredentials jenkinsCredentials = new JenkinsCredentials("u/\\;';krl", "password",
            "login");
        JenkinsServer jenkinsServer = jenkinsServerFactory.getJenkinsServer(jenkinsCredentials);
    }

    @Test
    public void updateCredentials_Success() throws Exception {
        JenkinsCredentials jenkinsCredentials = new JenkinsCredentials("", "", "");
        ReflectionTestUtils
            .setField(jenkinsServerFactory, "defaultCredentials", jenkinsCredentials);
        jenkinsServerFactory.updateCredentials();
        assertNotEquals(jenkinsCredentials,
            ReflectionTestUtils.getField(jenkinsServerFactory, "defaultCredentials"));
    }

}