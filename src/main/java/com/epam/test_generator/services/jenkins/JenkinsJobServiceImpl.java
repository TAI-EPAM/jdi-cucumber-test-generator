package com.epam.test_generator.services.jenkins;

import com.epam.test_generator.controllers.jenkins.JenkinsTransformer;
import com.epam.test_generator.controllers.jenkins.response.CommonJenkinsJobDTO;
import com.epam.test_generator.controllers.jenkins.response.ExecutedJenkinsJobDTO;
import com.epam.test_generator.services.exceptions.JenkinsRuntimeInternalException;
import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Executable;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.offbytwo.jenkins.model.QueueItem;
import com.offbytwo.jenkins.model.QueueReference;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Exchanger;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 * Service class that allows getting jobs from Jenkins server and running them
 */
@Service
public class JenkinsJobServiceImpl implements JenkinsJobService {

    private JenkinsTransformer jenkinsTransformer;
    private JenkinsServerFactory jenkinsServerFactory;
    private static final long QUEUE_WAITING_PERIOD = 1000L;
    private static final long QUEUE_WAITING_DELAY = 2000L;


    public JenkinsJobServiceImpl(
        JenkinsServerFactory jenkinsServerFactory, JenkinsTransformer jenkinsTransformer) {
        this.jenkinsServerFactory = jenkinsServerFactory;
        this.jenkinsTransformer = jenkinsTransformer;
    }

    /**
     * Gets list of all jobs from jenkins server
     *
     * @return list of found jobs
     */
    @Override
    public List<CommonJenkinsJobDTO> getJobs() {
        try {
            Map<String, Job> jobs = jenkinsServerFactory.getJenkinsServer().getJobs();

            List<CommonJenkinsJobDTO> response = jobs.values().stream()
                .map(jenkinsTransformer::toCommonDto).collect(Collectors.toList());
            return response;
        } catch (IOException e) {
            throw new JenkinsRuntimeInternalException();
        }
    }

    /**
     * Runs job on jenkins server by job name
     *
     * @return json with job name, url and build url
     */
    @Override
    public ExecutedJenkinsJobDTO runJob(String jobName) {
        try {
            JenkinsServer jenkinsServer = jenkinsServerFactory.getJenkinsServer();

            JobWithDetails job = jenkinsServer.getJob(jobName);
            QueueReference queueReference = job.build(true);

            QueueItem queueItem = getExecutedQueueItem(jenkinsServer, queueReference);
            Executable executable = queueItem.getExecutable();

            ExecutedJenkinsJobDTO response = jenkinsTransformer
                .toExecutedDTO(job, queueReference, executable);

            return response;

        } catch (Exception e) {
            throw new JenkinsRuntimeInternalException();
        }
    }

    /**
     * Method for receiving response from jenkins server after job runs
     *
     * @return queue item with  executable id and url
     */
    private QueueItem getExecutedQueueItem(JenkinsServer jenkinsServer,
                                           QueueReference queueReference) throws Exception {
        Timer time = new Timer();
        Exchanger<QueueItem> exchanger = new Exchanger<>();

        TimerTask waitingTask = new TimerTask() {
            @Override
            public void run() {
                QueueItem queueItem;
                try {
                    queueItem = jenkinsServer.getQueueItem(queueReference);

                    if (queueItem != null && queueItem.getExecutable() != null) {
                        time.cancel();
                        exchanger.exchange(queueItem);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        time.schedule(waitingTask, QUEUE_WAITING_DELAY, QUEUE_WAITING_PERIOD);

        return exchanger.exchange(null);
    }

}
