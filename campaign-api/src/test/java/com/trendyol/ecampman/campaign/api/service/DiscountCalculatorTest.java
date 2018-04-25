package com.trendyol.ecampman.campaign.api.service;

import com.trendyol.ecampman.campaign.api.model.CartItem;
import com.trendyol.ecampman.campaign.api.persistence.entity.Campaign;
import com.trendyol.ecampman.campaign.api.persistence.entity.CampaignTargetType;
import com.trendyol.ecampman.campaign.api.persistence.entity.CampaignType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DiscountCalculatorTest {

    @Test
    public void testCalculateDiscountedPrice_amountCampaign() {
        final Campaign productCampaign = Campaign.builder()
                .targetType(CampaignTargetType.PRODUCT)
                .targetId(123L)
                .campaignType(CampaignType.AMOUNT)
                .discount(50L)
                .build();

        final CartItem cartItem = CartItem.builder()
                .productId(123L)
                .categoryId(234L)
                .price(100L)
                .build();

        long discountedPrice = DiscountCalculator.setup(cartItem, productCampaign).calculateDiscountedPrice();

        assertEquals(50L, discountedPrice);
    }

    @Test
    public void testCalculateDiscountedPrice_withRateCampaign_discountBiggerThanMaxDiscountLimit() {
        final Campaign productCampaign = Campaign.builder()
                .targetType(CampaignTargetType.PRODUCT)
                .targetId(123L)
                .campaignType(CampaignType.RATE)
                .discount(40L)
                .maxDiscountAmount(20L)
                .build();

        final CartItem cartItem = CartItem.builder()
                .productId(123L)
                .categoryId(234L)
                .price(100L)
                .build();

        long discountedPrice = DiscountCalculator.setup(cartItem, productCampaign).calculateDiscountedPrice();

        assertEquals(80L, discountedPrice);
    }

    @Test
    public void testCalculateDiscountedPrice_withRateCampaign_discountLessThanMaxDiscountLimit() {
        final Campaign productCampaign = Campaign.builder()
                .targetType(CampaignTargetType.PRODUCT)
                .targetId(123L)
                .campaignType(CampaignType.RATE)
                .discount(40L)
                .maxDiscountAmount(50L)
                .build();

        final CartItem cartItem = CartItem.builder()
                .productId(123L)
                .categoryId(234L)
                .price(100L)
                .build();

        long discountedPrice = DiscountCalculator.setup(cartItem, productCampaign).calculateDiscountedPrice();

        assertEquals(60L, discountedPrice);
    }
}