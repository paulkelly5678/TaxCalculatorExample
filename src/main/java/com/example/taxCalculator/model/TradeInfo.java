package com.example.taxCalculator.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TRADE_TAX")
public class TradeInfo {
    @Id
    @Column(name = "TRADE_ID")
    private Long id;
    @Column(name = "TRADE_NAME")
    private String tradeName;
    @Column(name = "TRADE_VALUE")
    private double tradeValue;
    @Column(name = "TAX_PERCENTAGE")
    private int taxPercentage;
    @Column(name = "TAX_AMOUNT")
    private double taxAmount;
    @Column(name = "VALUE_AFTER_TAX")
    private double valueAfterTax;

}
