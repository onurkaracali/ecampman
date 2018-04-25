package com.trendyol.ecampman.campaign.api.controller;

import com.trendyol.ecampman.campaign.api.model.Cart;
import com.trendyol.ecampman.campaign.api.service.CartDiscountManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calculateDiscounts")
@Slf4j
public class CalculateDiscountController {

    @Autowired
    private CartDiscountManager cartDiscountManager;

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public Cart calculateDiscount(@RequestBody Cart cart) {
        return cartDiscountManager.processCart(cart);
    }
}
