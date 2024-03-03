package br.com.engineerchallenge.writers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import br.com.engineerchallenge.clients.ProposalHubEventClient;
import br.com.engineerchallenge.models.Proposal;
import br.com.engineerchallenge.models.ProposalFile;
import br.com.engineerchallenge.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProposalItemWriter implements ItemWriter<ProposalFile> {

    @Value("${sftp.directory.done}")
    private String doneDirectoryPath;

    @Value("${sftp.directory.errors}")
    private String errorDirectoryPath;

    @Value("${hub-event.api-key}")
    private String apiKey;

    @Value("${hub-event.source-app-name}")
    private String sourceAppName;

    @Autowired
    private ProposalHubEventClient hubEventsClient;

    public ProposalItemWriter() {
    }

    @Override
    public void write(Chunk<? extends ProposalFile> items) throws IOException {
        for (ProposalFile item : items) {
            Proposal proposal = item.getProposal();
            File file = item.getFile();

            if (item.getError() == null || item.getError().getDetails().isEmpty()) {
                String body = new ObjectMapper().writeValueAsString(proposal);
                log.info("Enviando proposta (EventHub): " + proposal.getProposalId());
                log.info("Payload de envio (EventHub): " + body);
                hubEventsClient.eventProposal(apiKey, sourceAppName, proposal);
                moveFileDone(file);
            } else {
                moveFileError(item);
            }
        }
    }

    private void moveFileDone(File file) throws IOException {
        log.info("Concluindo processamento: " + file.getName());
        File processingFile = FileUtils.moveAndReplaceFile(file, doneDirectoryPath);
        log.info("Arquivo movido (done): " + processingFile.getName());
    }

    private void moveFileError(ProposalFile proposalFile) throws IOException {
        File file = proposalFile.getFile();
        log.info("Concluindo processamento: " + file.getName());
        log.info("Arquivo contem erros: " + file.getName());

        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String newProposalFileValue = ow.writeValueAsString(proposalFile.getError());
        BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        // TODO o content/proposal como objeto e não como string
        writer.append(newProposalFileValue.translateEscapes());
        writer.close();
        FileUtils.moveAndReplaceFile(file, errorDirectoryPath);

        log.info("Criado registro em pasta (error): " + file.getName());
    }
}
