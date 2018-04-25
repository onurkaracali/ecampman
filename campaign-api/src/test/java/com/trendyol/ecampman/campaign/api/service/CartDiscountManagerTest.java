package com.trendyol.ecampman.campaign.api.service;

import com.google.common.collect.Lists;
import com.trendyol.ecampman.campaign.api.model.Cart;
import com.trendyol.ecampman.campaign.api.model.CartItem;
import com.trendyol.ecampman.campaign.api.persistence.entity.Campaign;
import com.trendyol.ecampman.campaign.api.persistence.entity.CampaignTargetType;
import com.trendyol.ecampman.campaign.api.persistence.entity.CampaignType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartDiscountManagerTest {

    @Mock
    private CampaignService campaignService;

    private CartDiscountManager cartDiscountManager;

    @Before
    public void setup() {
        cartDiscountManager = new CartDiscountManager(campaignService);
    }

    @Test
    public void testProcessCart_WithSimpleCart_SingleProductItem() {
        // Given
        CartItem cartItemProductA = CartItem.builder()
                .productId(123L)
                .categoryId(234L)
                .price(100L)
                .build();

        Cart cart = new Cart(Lists.newArrayList(
                cartItemProductA
        ));

        Campaign campaignA = Campaign.builder()
                .targetType(CampaignTargetType.PRODUCT)
                .campaignType(CampaignType.AMOUNT)
                .name("Product A Discount Campaign")
                .discount(20L)
                .targetId(123L)
                .build();

        CartItem expectedCartItem = CartItem.builder()
                .productId(123L)
                .categoryId(234L)
                .price(100L)
                .discountedPrice(80L)
                .build();

        when(campaignService.getCampaignForTarget(CampaignTargetType.PRODUCT, 123L))
                .thenReturn(Optional.of(campaignA));

        // When
        Cart processed = cartDiscountManager.processCart(cart);

        Assert.assertThat(processed.getItems().get(0), equalTo(expectedCartItem));
    }

    @Test
    public void testProcessCart_With2CategoryCampaign() {
        // Given
        CartItem cartItemCategory1 = CartItem.builder()
                .productId(123L)
                .categoryId(234L)
                .price(100L)
                .build();

        CartItem expectedItem1 = CartItem.builder()
                .productId(123L)
                .categoryId(234L)
                .price(100L)
                .discountedPrice(100L)
                .build();

        CartItem cartItemCategory2 = CartItem.builder()
                .productId(124L)
                .categoryId(234L)
                .price(140L)
                .build();

        CartItem expectedItem2 = CartItem.builder()
                .productId(124L)
                .categoryId(234L)
                .price(140L)
                .discountedPrice(120L)
                .build();

        Cart cart = new Cart(Lists.newArrayList(
                cartItemCategory1,
                cartItemCategory2
        ));

        Campaign campaignA = Campaign.builder()
                .targetType(CampaignTargetType.CATEGORY)
                .campaignType(CampaignType.AMOUNT)
                .name("Product A Discount Campaign")
                .discount(20L)
                .targetId(234L)
                .build();

        // When
        when(campaignService.getCampaignForTarget(CampaignTargetType.CATEGORY, 234L))
                .thenReturn(Optional.of(campaignA));

        Cart processedCart = cartDiscountManager.processCart(cart);

        // Then
        Assert.assertNotNull(processedCart);
        Assert.assertTrue(processedCart.getItems().size() > 0);
        Assert.assertThat(processedCart.getItems().get(0), equalTo(expectedItem1));
        Assert.assertThat(processedCart.getItems().get(1), equalTo(expectedItem2));
    }
}