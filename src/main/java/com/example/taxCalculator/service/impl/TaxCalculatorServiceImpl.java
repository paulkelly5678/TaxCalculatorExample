package com.example.taxCalculator.service.impl;

import com.example.taxCalculator.model.TradeInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.example.taxCalculator.service.TaxCalculatorService;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class TaxCalculatorServiceImpl implements TaxCalculatorService {

    @Override
    public void calculateTax(TradeInfo tradeInfo) throws Exception {
        if (null == tradeInfo.getId() || !StringUtils.hasLength(tradeInfo.getTradeName())
                || 0 >= tradeInfo.getTradeValue() || 0 >= tradeInfo.getTaxPercentage()) {
            log.error("Invalid Trade has been entered for tax calculation");
            throw new Exception("Invalid Trade has been entered for tax calculation for: " + tradeInfo.toString());
        } else {
            tradeInfo.setTaxAmount(tradeInfo.getTaxPercentage() * tradeInfo.getTradeValue() / 100);
            tradeInfo.setValueAfterTax(tradeInfo.getTradeValue() - tradeInfo.getTaxAmount());
        }
    }
}
