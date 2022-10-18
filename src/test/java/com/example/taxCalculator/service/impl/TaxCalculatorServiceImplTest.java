package com.example.taxCalculator.service.impl;

import com.example.taxCalculator.model.TradeInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.Assert.assertThrows;

@SpringBootTest
class TaxCalculatorServiceImplTest {

    @InjectMocks
    private TaxCalculatorServiceImpl taxCalculatorService;

    @Test
    public void testCalculateTax() throws Exception {
        TradeInfo tradeInfo = new TradeInfo();
        tradeInfo.setId(1L);
        tradeInfo.setTaxPercentage(10);
        tradeInfo.setTradeName("Trade Test");
        tradeInfo.setTradeValue(500370);
        taxCalculatorService.calculateTax(tradeInfo);
        Assertions.assertEquals(tradeInfo.getTaxAmount(), 50037);
        Assertions.assertEquals(tradeInfo.getValueAfterTax(), 450333);
    }

    @Test
    public void testCalculateTaxNullId() throws Exception {
        assertThrows(Exception.class,
                ()->{
                    TradeInfo tradeInfo = new TradeInfo();
                    tradeInfo.setId(null);
                    tradeInfo.setTaxPercentage(10);
                    tradeInfo.setTradeName("Trade Test");
                    tradeInfo.setTradeValue(500370);
                    taxCalculatorService.calculateTax(tradeInfo);
                });
    }

    @Test
    public void testCalculateTaxTradeNameNull() throws Exception {
        assertThrows(Exception.class,
                ()->{
                    TradeInfo tradeInfo = new TradeInfo();
                    tradeInfo.setId(1L);
                    tradeInfo.setTaxPercentage(10);
                    tradeInfo.setTradeName(null);
                    tradeInfo.setTradeValue(500370);
                    taxCalculatorService.calculateTax(tradeInfo);
                });
    }

    @Test
    public void testCalculateTaxTradePercentage0() throws Exception {
        assertThrows(Exception.class,
                ()->{
                    TradeInfo tradeInfo = new TradeInfo();
                    tradeInfo.setId(1L);
                    tradeInfo.setTaxPercentage(0);
                    tradeInfo.setTradeName("Trade Test");
                    tradeInfo.setTradeValue(500370);
                    taxCalculatorService.calculateTax(tradeInfo);
                });
    }

    @Test
    public void testCalculateTaxTradeAmount0() throws Exception {
        assertThrows(Exception.class,
                ()->{
                    TradeInfo tradeInfo = new TradeInfo();
                    tradeInfo.setId(1L);
                    tradeInfo.setTaxPercentage(10);
                    tradeInfo.setTradeName("Trade Test");
                    tradeInfo.setTradeValue(0);
                    taxCalculatorService.calculateTax(tradeInfo);
                });
    }
}