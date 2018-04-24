package com.trendyol.ecampman.campaign.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.trendyol.ecampman.campaign.api.persistence.entity.Campaign;
import com.trendyol.ecampman.campaign.api.persistence.entity.CampaignTargetType;
import com.trendyol.ecampman.campaign.api.persistence.entity.CampaignType;
import com.trendyol.ecampman.campaign.api.persistence.repository.CampaignRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest
public class CampaignControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CampaignRepository campaignRepository;

    private JacksonTester<CampaignListResponse> campaignListJson;
    private JacksonTester<CampaignView> campaignViewJson;
    private JacksonTester<ErrorResponse> errorResponseJacksonTester;

    @Before
    public void setup() {
        JacksonTester.initFields(this, new ObjectMapper());
    }

    @Test
    public void testGetAllCampaigns() throws Exception {
        ArrayList<Campaign> mockedCampaigns = Lists.newArrayList(
                Campaign.builder()
                        .id(1L)
                        .targetType(CampaignTargetType.PRODUCT)
                        .campaignType(CampaignType.AMOUNT)
                        .discount(5L)
                        .build(),
                Campaign.builder()
                        .id(1L)
                        .targetType(CampaignTargetType.PRODUCT)
                        .campaignType(CampaignType.AMOUNT)
                        .discount(5L)
                        .build());

        ArrayList<CampaignView> expectedList = Lists.newArrayList(
                CampaignView.builder()
                        .id(1L)
                        .targetType("PRODUCT")
                        .campaignType("AMOUNT")
                        .discount(5L)
                        .build(),
                CampaignView.builder()
                        .id(1L)
                        .targetType("PRODUCT")
                        .campaignType("AMOUNT")
                        .discount(5L)
                        .build());

        when(campaignRepository.findAll()).thenReturn(mockedCampaigns);

        mockMvc.perform(get("/campaigns").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(
                        content().json(
                                campaignListJson.write(new CampaignListResponse(expectedList)).getJson())
                );
    }

    @Test
    public void testGetSingleCampaign() throws Exception {
        final long campaignId = 1L;

        Campaign mockedCampaign = Campaign.builder()
                .id(campaignId)
                .targetType(CampaignTargetType.PRODUCT)
                .campaignType(CampaignType.AMOUNT)
                .discount(5L)
                .build();

        CampaignView expectedCampaignView = CampaignView.builder()
                .id(campaignId)
                .targetType("PRODUCT")
                .campaignType("AMOUNT")
                .discount(5L)
                .build();

        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(mockedCampaign));

        mockMvc.perform(get("/campaigns/" + campaignId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(
                        content().json(
                                campaignViewJson.write(expectedCampaignView).getJson())
                );
    }

    @Test
    public void testGetSingleCampaign_ShouldReturn404_WhenCampaignNotFound() throws Exception {
        when(campaignRepository.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/campaigns/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string(""));
    }

    @Test
    public void testSaveNewCampaign() throws Exception {
        CampaignView newCampaign = CampaignView.builder()
                .id(1L)
                .targetType("PRODUCT")
                .campaignType("AMOUNT")
                .discount(10L)
                .build();

        mockMvc.perform(
                post("/campaigns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(campaignViewJson.write(newCampaign).getJson()))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateCampaign() throws Exception {
        final long campaignId = 1L;

        Campaign campaignDB = Campaign.builder()
                .id(campaignId)
                .targetType(CampaignTargetType.PRODUCT)
                .campaignType(CampaignType.AMOUNT)
                .discount(10L)
                .build();

        CampaignView updatedCampaign = CampaignView.builder()
                .id(campaignId)
                .targetType("PRODUCT")
                .campaignType("AMOUNT")
                .discount(10L)
                .build();

        Campaign returnedMockCampaign = Campaign.builder()
                .id(campaignId)
                .targetType(CampaignTargetType.PRODUCT)
                .campaignType(CampaignType.AMOUNT)
                .discount(10L)
                .build();

        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaignDB));
        when(campaignRepository.save(any(Campaign.class))).thenReturn(returnedMockCampaign);

        mockMvc.perform(
                put("/campaigns/" + campaignId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(campaignViewJson.write(updatedCampaign).getJson()))
                .andExpect(status().isOk())
                .andExpect(
                        content().json(
                                campaignViewJson.write(updatedCampaign).getJson())
                );
    }

    @Test
    public void testSaveNewCampaign_ShouldReturnErrorResponse_WithInvalidRequest() throws Exception {
        CampaignView newCampaign = CampaignView.builder()
                .id(1L)
                .targetType("PRODUCT")
                .campaignType("RATE")
                .discount(10L)
                .build();

        ErrorResponse errorResponse = new ErrorResponse("Campaign is not valid!");

        mockMvc.perform(
                post("/campaigns")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(campaignViewJson.write(newCampaign).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorResponseJacksonTester.write(errorResponse).getJson()));
    }

    @Test
    public void testUpdateCampaign_ShouldReturnErrorResponse_WithInvalidRequest() throws Exception {
        final long campaignId = 1L;

        Campaign campaignDB = Campaign.builder()
                .id(campaignId)
                .targetType(CampaignTargetType.PRODUCT)
                .campaignType(CampaignType.AMOUNT)
                .discount(10L)
                .build();

        CampaignView updatedCampaign = CampaignView.builder()
                .id(campaignId)
                .targetType("PRODUCT")
                .campaignType("RATE")
                .discount(10L)
                .build();

        when(campaignRepository.findById(campaignId)).thenReturn(Optional.of(campaignDB));

        ErrorResponse errorResponse = new ErrorResponse("Campaign is not valid!");
        mockMvc.perform(
                put("/campaigns/" + campaignId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(campaignViewJson.write(updatedCampaign).getJson()))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(errorResponseJacksonTester.write(errorResponse).getJson()));
    }

    @Test
    public void testDeleteCampaign() throws Exception {
        Campaign campaignDB = Campaign.builder()
                .id(1L)
                .targetType(CampaignTargetType.PRODUCT)
                .campaignType(CampaignType.AMOUNT)
                .discount(10L)
                .build();
        when(campaignRepository.findById(1L)).thenReturn(Optional.of(campaignDB));

        mockMvc.perform(delete("/campaigns/1")).andExpect(status().isOk());
    }
}
