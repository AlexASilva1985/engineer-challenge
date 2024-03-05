package br.com.engineerchallenge.writers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import br.com.engineerchallenge.models.Proposal;
import br.com.engineerchallenge.models.ProposalFile;
import br.com.engineerchallenge.service.SendMailService;
import br.com.engineerchallenge.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProposalItemWriter implements ItemWriter<ProposalFile> {

    @Value("${sftp.directory.done}")
    private String doneDirectoryPath;

    @Value("${sftp.directory.errors}")
    private String errorDirectoryPath;
    
    @Value("${send.mail}")
    private String sendMail;
    
    @Value("${send.subject}")
    private String sendSubject;
    
    private SendMailService sendMailService;

    @Override
    public void write(Chunk<? extends ProposalFile> items) throws IOException {
    	
    	for (ProposalFile item : items) {
            Proposal proposal = item.getProposal();
            File file = item.getFile();

            if (item.getError() == null) {
                String body = new ObjectMapper().writeValueAsString(proposal);
                log.info("Enviando proposta: " + proposal.getProposalId());
                log.info("Payload de envio: " + body);
                moveFileDone(file);
                sendMailService.senMail(sendMail, sendSubject, file.getName()+ "Procesado com sucesso");
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
        writer.append(newProposalFileValue.translateEscapes());
        writer.close();
        FileUtils.moveAndReplaceFile(file, errorDirectoryPath);

        log.info("Criado registro em pasta (error): " + file.getName());
    }
}