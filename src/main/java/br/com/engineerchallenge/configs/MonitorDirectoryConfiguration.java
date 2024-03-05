package br.com.engineerchallenge.configs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.engineerchallenge.observer.FolderObserver;

@Configuration
public class MonitorDirectoryConfiguration {

	@Autowired
    FolderObserver folderObserver;

    @Value("${sftp.directory.received}")
    private String directoryPath;

    @Bean
    public Job monitorDirectoryJob(JobRepository jobRepository, Step readFilesStep) {
        return new JobBuilder("monitorDirectoryJob", jobRepository)
                .start(readFilesStep)
                .incrementer(new RunIdIncrementer())
                .build();
    }
}