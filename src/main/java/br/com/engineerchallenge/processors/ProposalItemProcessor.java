package br.com.engineerchallenge.processors;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.engineerchallenge.contants.ErrorCode;
import br.com.engineerchallenge.models.Proposal;
import br.com.engineerchallenge.models.ProposalFile;
import br.com.engineerchallenge.models.ProposalParsingError;
import br.com.engineerchallenge.utils.FileUtils;
import br.com.engineerchallenge.validation.FileNameValidation;
import br.com.engineerchallenge.validation.MandatoryFieldsValidation;
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
    public ProposalFile process(File file) throws IOException {
        log.info("Iniciando processamento do arquivo {}", file.getName());
        File movedFile = moveFileProcessing(file);
        Proposal proposal = null;

        try {
            String content = FileUtils.getFileContent(movedFile);
            JSONObject jsonObject = new JSONObject(content);
            JSONObject stAuto = jsonObject.getJSONObject("PROPOSAL");
            JSONArray data = stAuto.getJSONArray("data");

            proposal = new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false)
                    .readValue(data.get(0).toString(), Proposal.class);
        } catch (Exception e) {
            return buildJsonFileParserError(movedFile);
        }

        ProposalFile proposalFile = new ProposalFile(proposal, movedFile);
        Validation validationChain = Validation.link(new FileNameValidation(), new MandatoryFieldsValidation());
        validationChain.performValidation(proposalFile);

        return proposalFile;
    }

    private ProposalFile buildJsonFileParserError(File file) {
        ProposalFile proposalErrorFile = new ProposalFile(null, file);
        List<String> errorDetailList = new ArrayList<>();
        errorDetailList.add("O arquivo não atende a sintaxe json");
        ProposalParsingError error = ProposalParsingError.builder()
                .code(ErrorCode.ERR_01.getDescription())
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

