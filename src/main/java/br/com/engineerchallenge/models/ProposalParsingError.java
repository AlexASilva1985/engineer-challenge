package br.com.engineerchallenge.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ProposalParsingError implements Serializable {
    private String code;
    private String message;
    private List<String> details;
    private Long externalPolicyCode;
    private String filename;
    private String content;

    public ProposalParsingError() {
        details = new ArrayList<>();
    }

}

