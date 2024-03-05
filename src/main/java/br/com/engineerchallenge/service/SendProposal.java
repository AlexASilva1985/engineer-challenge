package br.com.engineerchallenge.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import br.com.engineerchallenge.exception.ApiException;
import br.com.engineerchallenge.models.Proposal;
import br.com.engineerchallenge.models.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SendProposal{
	
	private RestTemplate restTemplate;
	
	@Value("${api.url}")
	private String urlApi;

	public void postProposal(Proposal proposal) throws Exception {

	try {
		log.info("Envio da proposta pra gravar na base");
		final HttpEntity<?> requestEntity = createProposal(proposal);
		
		final String url = this.urlApi;
		this.restTemplate.exchange(url,  HttpMethod.POST, requestEntity, Response.class);
	} catch (HttpClientErrorException e) {
		if (e.getStatusCode().equals(HttpStatus.UNPROCESSABLE_ENTITY)) {
			throw new ApiException(1, e.getMessage(), e);
		}
	} catch (Exception e) {
		throw new ApiException(2, e.getMessage(), e);
	}
	
	}
	
	private HttpEntity<?> createProposal(Proposal proposal) {
		HttpEntity<?> requestEntity = new HttpEntity<>(proposal);
		return requestEntity;
	}
}