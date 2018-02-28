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


    public JenkinsJobServiceImpl(
        JenkinsServerFactory jenkinsServerFactory) {
        this.jenkinsServerFactory = jenkinsServerFactory;
    }

    /**
     * Gets list of all jobs from jenkins server
     * @return list of found jobs
     */
    @Override
    public List<CommonJenkinsJobResponse> getJobs() {
        try {
            Map<String, Job> jobs = jenkinsServerFactory.getJenkinsServer().getJobs();

            List<CommonJenkinsJobResponse> response = jobs.values().stream()
                .map(job -> transform(job)).collect(Collectors.toList());
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Runs job on jenkins server by job name
     * @param jobName
     * @return json with job name, url and build url
     */
    @Override
    public ExecuteJenkinsJobResponse runJob(String jobName) {
        try {
            JenkinsServer jenkinsServer = jenkinsServerFactory.getJenkinsServer();

            JobWithDetails job = jenkinsServer.getJob(jobName);
            QueueReference queueReference = job.build(true);

            QueueItem queueItem = getExecutedQueueItem(jenkinsServer, queueReference);
            Executable executable = queueItem.getExecutable();

            ExecuteJenkinsJobResponse response = new ExecuteJenkinsJobResponse();
            response.setJobName(job.getName());
            response.setJobUrl(job.getUrl());
            response.setQueueUrl(queueReference.getQueueItemUrlPart());
            response.setQueueExecutableId(executable.getNumber());
            response.setQueueExecutableUrl(executable.getUrl());

            return response;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Method for receiving response from jenkins server after job runs
     * @param jenkinsServer
     * @param queueReference
     * @return queue item with  executable id and url
     * @throws Exception
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

    private CommonJenkinsJobResponse transform(Job job) {
        CommonJenkinsJobResponse response = new CommonJenkinsJobResponse();
        response.setJobUrl(job.getUrl());
        response.setJobName(job.getName());
        return response;
    }
}
