package com.example.taxCalculator.exception;

public class TradeNotFoundException extends RuntimeException{
    public TradeNotFoundException(Long id){
        super("Could not find trade " + id);
    }
}
