package com.trendyol.ecampman.campaign.api.service;

import com.trendyol.ecampman.campaign.api.model.CartItem;
import com.trendyol.ecampman.campaign.api.persistence.entity.Campaign;
import com.trendyol.ecampman.campaign.api.persistence.entity.CampaignType;

public class DiscountCalculator {

    private CartItem cartItem;
    private Campaign campaign;

    private DiscountCalculator(CartItem cartItem, Campaign campaign) {
        this.cartItem = cartItem;
        this.campaign = campaign;
    }

    public static DiscountCalculator setup(CartItem cartItem, Campaign campaign) {
        if (cartItem == null
                || campaign == null
                || campaign.getDiscount() == null
                || cartItem.getPrice() == null) {
            throw new IllegalArgumentException("Invalid campaign or cart!");
        }

        return new DiscountCalculator(cartItem, campaign);
    }

    public long calculateDiscountedPrice() {
        long discount, price = cartItem.getPrice();

        if (CampaignType.AMOUNT.equals(campaign.getCampaignType())) {
            discount = campaign.getDiscount();
        } else {
            long discountRate = cartItem.getPrice() * campaign.getDiscount() / 100;

            discount = discountRate > campaign.getMaxDiscountAmount() ?
                    campaign.getMaxDiscountAmount() : discountRate;
        }

        return price - discount;
    }
}
