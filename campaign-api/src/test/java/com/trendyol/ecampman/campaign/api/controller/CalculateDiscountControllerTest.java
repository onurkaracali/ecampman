package com.trendyol.ecampman.campaign.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.trendyol.ecampman.campaign.api.model.Cart;
import com.trendyol.ecampman.campaign.api.model.CartItem;
import com.trendyol.ecampman.campaign.api.persistence.entity.Campaign;
import com.trendyol.ecampman.campaign.api.persistence.entity.CampaignTargetType;
import com.trendyol.ecampman.campaign.api.persistence.entity.CampaignType;
import com.trendyol.ecampman.campaign.api.service.CampaignService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.io.IOException;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CalculateDiscountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CampaignService campaignService;

    private JacksonTester<Cart> cartJacksonTester;
    private JacksonTester<CartItem> cartItemJacksonTester;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void testCalculateDiscounts_withSingleItem() throws Exception {
        // Given
        Cart givenCart = new Cart(Lists.newArrayList(
                CartItem.builder()
                        .productId(123L)
                        .categoryId(234L)
                        .price(200L)
                        .build()
        ));

        Campaign sampleCampaign = Campaign.builder()
                .targetType(CampaignTargetType.PRODUCT)
                .campaignType(CampaignType.AMOUNT)
                .targetId(123L)
                .discount(20L)
                .build();

        Cart expected = new Cart(Lists.newArrayList(
                CartItem.builder()
                        .productId(123L)
                        .categoryId(234L)
                        .price(200L)
                        .discountedPrice(180L)
                        .build()
        ));

        Mockito.when(campaignService.getCampaignForTarget(CampaignTargetType.PRODUCT, 123L))
                .thenReturn(Optional.of(sampleCampaign));

        // When
        mockMvc.perform(
                post("/calculateDiscounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(cartJacksonTester.write(givenCart).getJson()))
                .andExpect(status().isOk())
                .andExpect(content().json(cartJacksonTester.write(expected).getJson()));
    }

    @Test
    public void testCalculateDiscounts_with2CategoryItem() throws Exception {
        // Given
        Cart givenCart = new Cart(Lists.newArrayList(
                CartItem.builder()
                        .productId(123L)
                        .categoryId(234L)
                        .price(200L)
                        .build(),
                CartItem.builder()
                        .productId(456L)
                        .categoryId(234L)
                        .price(220L)
                        .build()
        ));

        Campaign sampleCampaign = Campaign.builder()
                .targetType(CampaignTargetType.CATEGORY)
                .campaignType(CampaignType.AMOUNT)
                .targetId(234L)
                .discount(20L)
                .build();

        Cart expectedCart = new Cart(Lists.newArrayList(
                CartItem.builder()
                        .productId(123L)
                        .categoryId(234L)
                        .price(200L)
                        .discountedPrice(200L)
                        .build(),
                CartItem.builder()
                        .productId(456L)
                        .categoryId(234L)
                        .price(220L)
                        .discountedPrice(200L)
                        .build()
        ));

        Mockito.when(campaignService.getCampaignForTarget(CampaignTargetType.CATEGORY, 234L))
                .thenReturn(Optional.of(sampleCampaign));

        // When
        mockMvc.perform(
                post("/calculateDiscounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(cartJacksonTester.write(givenCart).getJson()))
                .andExpect(status().isOk())
                .andExpect(content().json(cartJacksonTester.write(expectedCart).getJson()));
    }
}