package com.example.beatthestreet.model.iex;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class IEXNews {

    @JsonProperty("news")
    private List<IEXNewsRecord> iexNewsData;

    public List<IEXNewsRecord> getIexNewsData() {
        return iexNewsData;
    }
}
