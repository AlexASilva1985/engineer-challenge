package br.com.engineerchallenge.validation;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.engineerchallenge.annotations.Mandatory;
import br.com.engineerchallenge.contants.ErrorCode;
import br.com.engineerchallenge.models.Proposal;
import br.com.engineerchallenge.models.ProposalFile;
import br.com.engineerchallenge.models.ProposalParsingError;
import br.com.engineerchallenge.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MandatoryFieldsValidation extends Validation {
    @Override
    public void performValidation(ProposalFile proposalFile) {
        File file = proposalFile.getFile();
        Proposal proposal = proposalFile.getProposal();

        log.info("Validando campos do arquivo de proposta {} ", file.getName());
        ProposalParsingError error = ProposalParsingError.builder()
                .code(ErrorCode.ERR_02.getDescription())
                .message("Campos requeridos não preenchidos")
                .externalPolicyCode(proposal.getProposalId())
                .content(FileUtils.getFileContent(file))
                .filename(file.getName())
                .build();


        List<String> detailErrorList = new ArrayList<>();

        for (Field field : Proposal.class.getDeclaredFields()) {
            if (field.isAnnotationPresent(Mandatory.class)) {
                String fieldName = field.getName();
                fieldName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);

                String prefix = "get";
                if (field.getType() == Boolean.class) {
                    prefix = "is";
                }

                try {
                    Method getMethod = Proposal.class.getMethod(prefix + fieldName);
                    Object objectReturned = getMethod.invoke(proposal);
                    String jsonPropertyFieldName = field.getAnnotation(JsonProperty.class).value();
                    if (objectReturned == null || objectReturned.toString().isEmpty()) {
                        detailErrorList.add("O campo '" + jsonPropertyFieldName + "' não foi informado");
                    }
                } catch (Exception e) {
                    log.error("Não foi possivel invokar o metodo {} ", prefix + fieldName);
                    log.error("{}", e.getMessage());
                }
            }
        }

        if (detailErrorList.isEmpty()) {
            checkNext(proposalFile);
        } else {
            proposalFile.setError(error);
            proposalFile.getError().setDetails(detailErrorList);
        }
    }
}

