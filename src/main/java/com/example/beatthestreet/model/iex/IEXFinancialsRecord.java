package com.example.beatthestreet.model.iex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IEXFinancialsRecord {

    @JsonProperty("fiscalDate")
    private String fiscalDate;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("grossProfit")
    private String grossProfit;
    @JsonProperty("totalRevenue")
    private String totalRevenue;
    @JsonProperty("netIncome")
    private String netIncome;
    @JsonProperty("longTermDebt")
    private String totalLongTermDebt;
    @JsonProperty("cashFlow")
    private String cashFlow;

    public String getFiscalDate() {
        return fiscalDate;
    }

    public String getCurrency() { return currency; }

    public String getTotalRevenue() {
        return totalRevenue;
    }

    public String getGrossProfit() {
        return grossProfit;
    }

    public String getNetIncome() { return netIncome; }

    public String getTotalLongTermDebt() {
        return totalLongTermDebt;
    }

    public String getCashFlow() {
        return cashFlow;
    }
}
