package br.com.engineerchallenge.steps;

import java.io.File;

import org.springframework.batch.core.Step;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import br.com.engineerchallenge.models.ProposalFile;
import br.com.engineerchallenge.processors.ProposalItemProcessor;
import br.com.engineerchallenge.readers.ProposalFileReader;
import br.com.engineerchallenge.writers.ProposalItemWriter;

@Configuration
public class ReadProposalFilesStep {

    @Value("${chunk-size}")
    private Integer chunkSize;

    @Bean
    public Step readFilesStep(JobRepository jobRepository,
                                     PlatformTransactionManager transactionManager,
                                     ProposalFileReader proposalItemReader,
                                     ProposalItemProcessor proposalItemProcessor,
                                     ProposalItemWriter proposalItemWriter
    ) {
        return new StepBuilder("readFilesStep", jobRepository)
                .<File, ProposalFile>chunk(chunkSize, transactionManager)
                .reader(proposalItemReader)
                .processor(proposalItemProcessor)
                .writer(proposalItemWriter)
                .build();
    }

  }