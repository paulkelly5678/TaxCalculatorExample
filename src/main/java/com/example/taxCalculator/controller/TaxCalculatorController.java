package com.example.taxCalculator.controller;

import com.example.taxCalculator.exception.TradeNotFoundException;
import com.example.taxCalculator.model.TradeInfo;
import com.example.taxCalculator.repository.TradeModelAssembler;
import com.example.taxCalculator.repository.TradeTaxRepository;
import com.example.taxCalculator.service.TaxCalculatorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/v1/tax")
public class TaxCalculatorController {

    private final TradeTaxRepository repository;

    private final TradeModelAssembler assembler;

    @Autowired
    private TaxCalculatorService taxCalculatorService;

    public TaxCalculatorController(TradeTaxRepository repository, TradeModelAssembler assembler){
        this.repository = repository;
        this.assembler = assembler;
    }

    /*
    * Fetches all trades stored in TRADE_TAX table
    * */
    @GetMapping("/find-all-trades")
    public CollectionModel<EntityModel<TradeInfo>> fetchAllTrades() {

        /*
        * List of all trades are streamed
        * Each TradeInfo converted to EntityModel<TradeInfo> object
        * then added to the CollectionModel which is returned as response
        * */
        List<EntityModel<TradeInfo>> trades = repository.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(trades, linkTo(methodOn(TaxCalculatorController.class).fetchAllTrades()).withSelfRel());
    }

    /*
     * Inserts new trade into TRADE_TAX table
     * after calculating tax values
     * */
    @PostMapping("/insert-new-trade")
    ResponseEntity<?> insertNewTrade(@RequestBody TradeInfo newTrade) throws Exception {

        try {
            /*
             * The newTrade is passed to the service layer to
             * calculate the tax amount and the value after tax
             * */
            taxCalculatorService.calculateTax(newTrade);
        }catch (Exception e){
            log.error("Error thrown during tax calculation");
            throw new Exception(e.getMessage());
        }
        /*
        * The newTrade is then converted to EntityModel<TradeInfo> object
        * The repository save method is then called to insert the new trade
        * into TRADE_TAX table
        * */
        EntityModel<TradeInfo> entityModel = assembler.toModel(repository.save(newTrade));

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    /*
     * Fetches specific trade stored in TRADE_TAX table
     * using the trade id as key
     * */
    @GetMapping("/find-trade-by-id/{id}")
    public EntityModel<TradeInfo> fetchById(@PathVariable Long id) {

        /*
        * The repository calls findById method passing id as key
        * If no value is present in TRADE_TAX table and error is thrown
        * */
        TradeInfo tradeInfo = repository.findById(id)
                .orElseThrow(() -> new TradeNotFoundException(id));

        return assembler.toModel(tradeInfo);
    }

    /*
     * Updates existing trade values stored in TRADE_TAX
     * based on id
     * */
    @PutMapping("/replace-trade/{id}")
    public ResponseEntity<?> replaceTrade(@RequestBody TradeInfo newTrade, @PathVariable Long id) throws Exception {
        /*
         * The newTrade is passed to the service layer to
         * calculate the tax amount and the value after tax
         * */
        taxCalculatorService.calculateTax(newTrade);

        /*
         * The repository fetches the trade withe id passed in the method
         * if there is a trade found we update the values and save the existing TradeInfoDto
         * if there is no value fetched the repository saves the newTrade object
         * */
        TradeInfo updatedTrade = repository.findById(id)
                .map(tradeInfoDto -> {
                    tradeInfoDto.setTradeName(newTrade.getTradeName());
                    tradeInfoDto.setTradeValue(newTrade.getTradeValue());
                    tradeInfoDto.setTaxAmount(newTrade.getTaxAmount());
                    tradeInfoDto.setTaxPercentage(newTrade.getTaxPercentage());
                    tradeInfoDto.setValueAfterTax(newTrade.getValueAfterTax());
                    return repository.save(tradeInfoDto);
                })
                .orElseGet(() -> {
                    newTrade.setId(id);
                    return repository.save(newTrade);
                });
        EntityModel<TradeInfo> entityModel = assembler.toModel(updatedTrade);

        return ResponseEntity
                .created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(entityModel);
    }

    /*
     * Deletes specific trade stored in TRADE_TAX table
     * using the trade id as key
     * */
    @DeleteMapping("/delete-trade/{id}")
    public ResponseEntity<?> deleteTrade(@PathVariable Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
