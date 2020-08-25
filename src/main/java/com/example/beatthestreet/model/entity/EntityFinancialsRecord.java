package com.example.beatthestreet.model.entity;

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

    public String getFiscalYear() {
        return fiscalYear;
    }

    public void setFiscalYear(String fiscalYear) {
        this.fiscalYear = fiscalYear;
    }

    public String getFiscalQuarter() {
        return fiscalQuarter;
    }

    public void setFiscalQuarter(String fiscalQuarter) {
        this.fiscalQuarter = fiscalQuarter;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(String totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public String getGrossProfit() {
        return grossProfit;
    }

    public void setGrossProfit(String grossProfit) {
        this.grossProfit = grossProfit;
    }

    public String getNetIncome() {
        return netIncome;
    }

    public void setNetIncome(String netIncome) {
        this.netIncome = netIncome;
    }

    public String getcashFlow() {
        return cashFlow;
    }

    public void setCashFlow(String cashFlow) {
        this.cashFlow = cashFlow;
    }

    public String getTotalLongTermDebt() {
        return totalLongTermDebt;
    }

    public void setTotalLongTermDebt(String totalLongTermDebt) {
        this.totalLongTermDebt = totalLongTermDebt;
    }

    public String getCurrency() { return currency; }

    public void setCurrency(String currency) { this.currency = currency; }
}
