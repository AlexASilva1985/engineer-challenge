package br.com.engineerchallenge.scheduled;

import java.util.Date;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@EnableScheduling
public class ScheduledProposalDirectoryReader {

    @Value("${sftp.directory.received}")
    private String receveidPath;

    @Autowired
    JobLauncher jobLauncher;
    @Autowired
    Job monitorDirectoryJob;
    JobExecution execution;

    @Scheduled(cron = "${scheduled-proposal-chron}")
    public void runProposalReceivedJob() {
        log.info("Executando Job {}", receveidPath);

        JobParametersBuilder builder = new JobParametersBuilder();
        builder.addDate("date", new Date());
        builder.addString("filePath", receveidPath);
        JobParameters jobParameters = builder.toJobParameters();

        try {
                execution = jobLauncher.run(monitorDirectoryJob, jobParameters);
        } catch (Exception e) {
            log.error("Erro ao executar job {}", e.getMessage());
        }
    }
}