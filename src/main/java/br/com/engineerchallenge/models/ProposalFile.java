package br.com.engineerchallenge.models;

import lombok.Getter;
import lombok.Setter;

import java.io.File;

@Getter
public class ProposalFile {
    private Proposal proposal;
    private File file;

    @Setter
    private Error error;

    public ProposalFile(Proposal proposal, File file) {
        this.proposal = proposal;
        this.file = file;
    }

}

