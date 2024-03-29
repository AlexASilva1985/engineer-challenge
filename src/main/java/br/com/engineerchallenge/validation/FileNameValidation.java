package br.com.engineerchallenge.validation;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.engineerchallenge.models.Error;
import br.com.engineerchallenge.models.ProposalFile;
import br.com.engineerchallenge.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileNameValidation extends Validation {
    @Override
    public void performValidation(ProposalFile proposalFile) {
        String fileName = proposalFile.getFile().getName();
        log.info("Validando nome do arquivo {}", fileName);
        File file = proposalFile.getFile();
        Pattern pattern = Pattern.compile("^[a-z]+\\.+[a-z]+$");
        Matcher matcher = pattern.matcher(fileName);
        if (!matcher.find()) {
            List<String> errorDetailList = new ArrayList<>();
            errorDetailList.add("O nome do arquivo não está de acordo com o padrão estabelecido: " + matcher);

            Error error = Error.builder()
                    .code("ERRO_01")
                    .message("Arquivo corrompido e/ou fora do padrão json")
                    .details(errorDetailList)
                    .content(FileUtils.getFileContent(file))
                    .filename(file.getName())
                    .ProposalCode(proposalFile.getProposal().getProposalId())
                    .build();

            proposalFile.setError(error);
            return;
        }
        checkNext(proposalFile);
    }
}