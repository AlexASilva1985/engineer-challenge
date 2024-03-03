package br.com.engineerchallenge.contants;

public enum Regex {
    PROPOSAL_FILE_NAME("proposal_proagro_\\d*_\\d{4}_\\d{1,2}_\\d{2}_\\d{2}_\\d{2}_\\d{2}_\\d*\\.json");

    Regex(String expression){
        this.expression = expression;
    }
    private String expression;

    public String getExpression() {
        return expression;
    }
}

