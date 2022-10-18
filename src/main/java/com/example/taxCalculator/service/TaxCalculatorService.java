package com.example.taxCalculator.service;

import com.example.taxCalculator.model.TradeInfo;

public interface TaxCalculatorService {
    public void calculateTax(TradeInfo tradeInfo) throws Exception;
}
