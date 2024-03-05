package br.com.engineerchallenge.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.engineerchallenge.annotations.Mandatory;
import lombok.Data;

@Data
public class Proposal {

    @Mandatory
    @JsonProperty("propostaId")
    private Long proposalId;

    @Mandatory
    @JsonProperty("cotizacionId")
    private Long quotationId;

    @Mandatory
    @JsonProperty("datadaProposta")
    private String proposalDate;

    @Mandatory
    @JsonProperty("idProduto")
    private Long productId;

    @Mandatory
    @JsonProperty("nacionalidade")
    private String nationality;

    @Mandatory
    @JsonProperty("cidade")
    private String city;

    @Mandatory
    @JsonProperty("cep")
    private Long zipCode;

    @Mandatory
    @JsonProperty("endereco")
    private String address;

    @Mandatory
    @JsonProperty("enderecoNumero")
    private String addressNumber;

    @Mandatory
    @JsonProperty("celular")
    private String cellPhone;

    @Mandatory
    @JsonProperty("email")
    private String email;
    
    @Mandatory
    @JsonProperty("name")
    private String nome;
    
    @Mandatory
    @JsonProperty("cpf")
    private String cpf;

}