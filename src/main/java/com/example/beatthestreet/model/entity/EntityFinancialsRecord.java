package com.example.beatthestreet.model.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntityFinancialsRecord {

    private String fiscalYear;
    private String fiscalQuarter;
    private String formType;
    private String totalRevenue;
    private String grossProfit;
    private String netIncome;
    private String cashFlow;
    private String totalLongTermDebt;
    private String currency;
}
