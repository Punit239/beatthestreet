package com.example.beatthestreet.model.iex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class IexFinancialsRecord {

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
}
