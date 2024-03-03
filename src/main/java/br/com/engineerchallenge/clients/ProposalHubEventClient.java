package br.com.engineerchallenge.clients;

import br.com.engineerchallenge.models.Proposal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "proposalHubEventClient", url = "${hub-event.endpoint.new-proposal}")
public interface ProposalHubEventClient {

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    void eventProposal(
            @RequestParam("key") String apiKey,
            @RequestParam() String sourceAppName,
            @RequestBody Proposal proposal
    );
}

