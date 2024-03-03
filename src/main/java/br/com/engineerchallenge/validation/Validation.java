package br.com.engineerchallenge.validation;

import br.com.engineerchallenge.models.ProposalFile;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class Validation {

    private Validation next;

    public static Validation link(Validation first, Validation... chain){
        Validation head = first;
        for(Validation nextChain : chain){
            head.next = nextChain;
            head = nextChain;
        }
        return first;
    }

    public abstract void performValidation(ProposalFile proposalFile);

    protected void checkNext(ProposalFile proposalFile){
        if(next != null){
            next.performValidation(proposalFile);
        }
    }
}

