package com.tmooc.work.config;

import com.tmooc.work.WorkApplicationTests;
import com.tmooc.work.util.FastDFSClientWrapper;
import org.junit.Test;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.IOException;


public class BatchConfigurationTest extends WorkApplicationTests {
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job myJob;
    @Autowired
    private FastDFSClientWrapper fastDFSClientWrapper;
    @Test
    public void test1() throws JobParametersInvalidException, JobExecutionAlreadyRunningException, JobRestartException, JobInstanceAlreadyCompleteException, IOException {
            String remoteFilePath="http://140.143.0.246:8888/group1/M00/00/00/rBUADFrNnniAdipTAAGsrEYp9xM79.xlsx";
            JobParameters jobParameters=new JobParametersBuilder().addString("remoteFilePath",remoteFilePath).toJobParameters();
            JobExecution jobExecution = jobLauncher.run(myJob, jobParameters);
            System.out.println(jobExecution.getExecutionContext().size());
        }
    }
