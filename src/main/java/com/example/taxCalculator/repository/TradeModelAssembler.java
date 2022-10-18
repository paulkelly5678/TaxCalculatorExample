package com.example.taxCalculator.repository;

import com.example.taxCalculator.controller.TaxCalculatorController;
import com.example.taxCalculator.model.TradeInfo;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@Component
public class TradeModelAssembler implements RepresentationModelAssembler<TradeInfo, EntityModel<TradeInfo>> {
    @Override
    public EntityModel<TradeInfo> toModel(TradeInfo tradeInfo) {
        return EntityModel.of(tradeInfo, linkTo(methodOn(TaxCalculatorController.class).fetchById(tradeInfo.getId())).withSelfRel(),
                linkTo(methodOn(TaxCalculatorController.class).fetchAllTrades()).withRel("trades"));
    }
}
