package br.com.engineerchallenge.processors;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.engineerchallenge.exception.ApiException;
import br.com.engineerchallenge.models.Error;
import br.com.engineerchallenge.models.Proposal;
import br.com.engineerchallenge.models.ProposalFile;
import br.com.engineerchallenge.service.SendProposal;
import br.com.engineerchallenge.utils.FileUtils;
import br.com.engineerchallenge.validation.FileNameValidation;
import br.com.engineerchallenge.validation.Validation;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProposalItemProcessor implements ItemProcessor<File, ProposalFile> {
    @Value("${sftp.directory.processing}")
    private String directoryPath;
    
    public ProposalItemProcessor() {
    }

    @Override
    public ProposalFile process(File file) throws Exception, ApiException {
        log.info("Iniciando processamento do arquivo {}", file.getName());
        File movedFile = moveFileProcessing(file);
        Proposal proposal = null;
        ProposalFile proposalFile = null;
        Validation validationChain = null;

        try {
            String content = FileUtils.getFileContent(movedFile);
            JSONObject jsonObject = new JSONObject(content);

            proposal = new ObjectMapper()
                    .readValue(jsonObject.toString(), Proposal.class);

        proposalFile = new ProposalFile(proposal, movedFile);
        validationChain = Validation.link(new FileNameValidation());
        validationChain.performValidation(proposalFile);
        
        SendProposal sendProposal = new SendProposal();
        sendProposal.postProposal(proposal);
        
        } catch (Exception e) {
        	return buildJsonFileParserError(movedFile);
        }

        return proposalFile;
    }

    private ProposalFile buildJsonFileParserError(File file) {
        ProposalFile proposalErrorFile = new ProposalFile(null, file);
        List<String> errorDetailList = new ArrayList<>();
        errorDetailList.add("O arquivo não atende a sintaxe json");
        Error error = Error.builder()
                .code("ERRO_O1")
                .message("Arquivo corrompido e/ou fora do padrão json")
                .details(errorDetailList)
                .content(FileUtils.getFileContent(file))
                .filename(file.getName())
                .build();

        proposalErrorFile.setError(error);
        return proposalErrorFile;
    }

    private File moveFileProcessing(File file) throws IOException {
        log.info("Processando arquivo: " + file.getName());
        File processingFile = FileUtils.moveAndReplaceFile(file, directoryPath);
        log.info("Arquivo movido (processing): " + processingFile.getName());
        return processingFile;
    }
}