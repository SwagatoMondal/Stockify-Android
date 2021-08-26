package com.aas.stockify.entity;

public class Stock {
    private String name;
    private String symbol;
    private float price;
    private String exchange = "BSE";
    private float ltpChangePercentage;
    private float targetPrice;
    private float returns;
    private String exchangeId;
    private String instrumentId;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getLtpChangePercentage() {
        return ltpChangePercentage;
    }

    public void setLtpChangePercentage(float ltpChangePercentage) {
        this.ltpChangePercentage = ltpChangePercentage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public void setTargetPrice(float targetPrice) {
        this.targetPrice = targetPrice;
    }

    public float getTargetPrice() {
        return targetPrice;
    }

    public void setReturns(float returns) {
        this.returns = returns;
    }

    public float getReturns() {
        return returns;
    }

    public void setExchangeId(String exchangeId) {
        this.exchangeId = exchangeId;
    }

    public String getExchangeId() {
        return exchangeId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId;
    }

    public String getInstrumentId() {
        return instrumentId;
    }
}
