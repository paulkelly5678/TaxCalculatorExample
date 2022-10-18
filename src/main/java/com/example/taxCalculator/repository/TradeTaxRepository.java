package com.example.taxCalculator.repository;

import com.example.taxCalculator.model.TradeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradeTaxRepository extends JpaRepository<TradeInfo, Long> {
}
