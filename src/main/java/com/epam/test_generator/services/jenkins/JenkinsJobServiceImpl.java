package com.epam.test_generator.services.jenkins;

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
import java.util.concurrent.Exchanger;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import java.util.TimerTask;

/**
 * Service class that allows getting jobs from Jenkins server and running them
 */
@Service
public class JenkinsJobServiceImpl implements JenkinsJobService {

    private JenkinsServerFactory jenkinsServerFactory;
    private static final long QUEUE_WAITING_PERIOD = 1000L;
    private static final long QUEUE_WAITING_DELAY = 2000L;
    private static final long QUEUE_WAITING_COUNTER = 25;


    public JenkinsJobServiceImpl(
        JenkinsServerFactory jenkinsServerFactory) {
        this.jenkinsServerFactory = jenkinsServerFactory;
    }

    /**
     * Gets list of all jobs from jenkins server
     *
     * @return list of found jobs
     */
    @Override
    public List<CommonJenkinsJobResponse> getJobs() {
        try {
            Map<String, Job> jobs = jenkinsServerFactory.getJenkinsServer().getJobs();

            List<CommonJenkinsJobResponse> response = jobs.values().stream()
                .map(this::transform).collect(Collectors.toList());
            return response;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Runs job on jenkins server by job name
     *
     * @return json with job name, url and build url
     */
    @Override
    public ExecuteJenkinsJobResponse runJob(String jobName) {
        try {
            JenkinsServer jenkinsServer = jenkinsServerFactory.getJenkinsServer();

            JobWithDetails job = jenkinsServer.getJob(jobName);
            QueueReference queueReference = job.build(true);

            QueueItem queueItem = getExecutedQueueItem(jenkinsServer, queueReference);
            if (queueItem == null) {
                throw new RuntimeException("Executed queueItem not found");
            }
            Executable executable = queueItem.getExecutable();

            ExecuteJenkinsJobResponse response = new ExecuteJenkinsJobResponse();
            response.setJobName(job.getName());
            response.setJobUrl(job.getUrl());
            response.setQueueUrl(queueReference.getQueueItemUrlPart());
            response.setQueueExecutableId(executable.getNumber());
            response.setQueueExecutableUrl(executable.getUrl());

            return response;

        } catch (Exception e) {
            throw new RuntimeException(e);
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

            int count = 0;

            @Override
            public void run() {
                QueueItem queueItem;
                try {
                    queueItem = jenkinsServer.getQueueItem(queueReference);
                    count++;

                    boolean timeout = count >= QUEUE_WAITING_COUNTER;
                    boolean gotExecutableQueueItem =
                        queueItem != null && queueItem.getExecutable() != null;
                    if (gotExecutableQueueItem) {
                        exchanger.exchange(queueItem);
                        time.cancel();
                    }

                    if (timeout) {
                        time.cancel();
                        exchanger.exchange(null);
                    }

                } catch (Exception e) {
                    try {
                        exchanger.exchange(null);
                    } catch (InterruptedException e1) {
                    }
                    time.cancel();
                }
            }
        };

        time.schedule(waitingTask, QUEUE_WAITING_DELAY, QUEUE_WAITING_PERIOD);

        return exchanger.exchange(null);
    }

    private CommonJenkinsJobResponse transform(Job job) {
        CommonJenkinsJobResponse response = new CommonJenkinsJobResponse();
        response.setJobUrl(job.getUrl());
        response.setJobName(job.getName());
        return response;
    }
}
