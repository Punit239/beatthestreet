package com.example.beatthestreet.model.iex;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class IexPublicInfo {

    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("companyName")
    private String companyName;
    @JsonProperty("website")
    private String website;
    @JsonProperty("description")
    private String description;
    @JsonProperty("CEO")
    private String ceo;
    @JsonProperty("primarySicCode")
    private String primarySicCode;
    @JsonProperty("employees")
    private String employees;
    @JsonProperty("tags")
    private List<String> tags;
}
